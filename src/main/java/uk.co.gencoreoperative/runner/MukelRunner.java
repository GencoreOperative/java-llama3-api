package uk.co.gencoreoperative.runner;

import static uk.co.gencoreoperative.runner.ModelType.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Path;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mukel.qwen2.Qwen2;
import uk.co.gencoreoperative.Config;
import uk.co.gencoreoperative.ai.ContextWindow;
import uk.co.gencoreoperative.ai.Response;
import uk.co.gencoreoperative.ai.Run;
import uk.co.gencoreoperative.utils.BlockingQueueIterator;
import uk.co.gencoreoperative.utils.QueuingOutputStream;
import uk.co.gencoreoperative.utils.RunAsync;
import uk.co.gencoreoperative.utils.StdOutUtils;
import mukel.llama3.Llama3;

/**
 * Based on the excellent work by <a href="https://github.com/mukel/llama3.java>mukel</a>,
 * this class runs the Llama3 model with the provided prompt.
 * <p>
 * The code provided was intended for use on the command line. This class adapts this by
 * capturing the output of the model and returning it as a string.
 * <p>
 * The Llama3.java library we are using is best invoked using its {@link Llama3#main(String[])}
 * method. This call will generate stdout and stderr that will be re-diverted to simplify the
 * experience for the caller. From the caller's perspective, we are only interested in the output
 * of the model.
 */
public class MukelRunner implements Run {
    private final Path modelPath;
    private final ModelType modelType;

    public MukelRunner(Path modelPath) {
        modelType = validateModelPath(modelPath);
        this.modelPath = modelPath;
    }

    /**
     * @return The name of the model provided to this runner.
     */
    public String getModelName() {
        return modelType.name();
    }

    /**
     * Validate the provided LLM model to ensure it is likely to be runnable by {@link Qwen2} or {@link Llama3}.
     * @param modelPath The {@link Path} to the model.
     * @return The {@link ModelType} of the model based on the path.
     */
    private @Nonnull ModelType validateModelPath(@Nonnull Path modelPath) {
        String lowerCaseName = modelPath.getFileName().toString().toLowerCase();
        if (!lowerCaseName.contains("q4_0")) throw new IllegalArgumentException("Model must be a Q4_0 model");
        if (!lowerCaseName.endsWith(".gguf")) throw new IllegalArgumentException("Model must be a .gguf file");

        if (lowerCaseName.contains(QWEN2.getSearchString())) {
            return QWEN2;
        } else if (lowerCaseName.contains(LLAMA3.getSearchString())) {
            return LLAMA3;
        } else {
            throw new IllegalArgumentException("Model must be a Qwen2 or Llama3 model");
        }
    }

    private Consumer<String[]> getConsumer(ModelType type) {
        switch (type) {
            case QWEN2 -> {
                return args -> {
                    try {
                        Qwen2.main(args);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                };
            }
            case LLAMA3 -> {
                return args -> {
                    try {
                        Llama3.main(args);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                };
            }
            default -> throw new IllegalArgumentException("Model must be a Qwen2 or Llama3 model");
        }
    }

    /**
     * Perform a single AI request with the provided system and user prompts and return a {@link Response} object.
     * <p>
     * This method will provide more information about the LLM invocation for the caller.
     *
     * @param system A non-null text that guides the intention of the LLM request.
     * @param user The text provided from the user that the LLM is to respond to.
     * @return A {@link Response} object containing the model's output and metadata.
     * @throws RuntimeException if the model invocation fails or an error occurs during processing.
     */
    @Override
    public Response runWithResponse(@Nonnull Config options) throws RuntimeException {

        BlockingQueueIterator output = new BlockingQueueIterator();
        BlockingQueueIterator error = new BlockingQueueIterator();
        String[] args = buildArgs(options.systemPrompt, options.prompt, options.temperature);

        long start = System.currentTimeMillis();
        getInvoker(args, output, error).run();
        Duration duration = Duration.of(System.currentTimeMillis() - start, ChronoUnit.MILLIS);

        String completeError = BlockingQueueIterator.createStream(error).collect(Collectors.joining());
        String completeOutput = BlockingQueueIterator.createStream(output).collect(Collectors.joining());

        return new Response(completeOutput, parseContextWindow(completeError), duration);
    }

    private static ContextWindow parseContextWindow(String error) {
        try (BufferedReader reader = new BufferedReader(new StringReader(error))) {
            return reader.lines()
                    .filter(ContextParser::isContextLine)
                    .findFirst()
                    .map(ContextParser::parseContext)
                    .orElseGet(() -> new ContextWindow(0,0));
        } catch (IOException e) {
            throw new IllegalStateException();
        }
    }

    /**
     * Perform a single AI request with the provided system and user prompts.
     *
     * @param system A non-null text that guides the intention of the LLM request.
     * @param user The text provided from the user that the LLM is to respond to.
     * @return The output of the model if any.
     */
    public String run(@Nonnull Config options) {
        BlockingQueueIterator output = new BlockingQueueIterator();
        BlockingQueueIterator error = new BlockingQueueIterator();
        String[] args = buildArgs(options.systemPrompt, options.prompt, options.temperature);
        RunAsync.runAsync(getInvoker(args, output, error));
        return BlockingQueueIterator.createStream(output).collect(Collectors.joining());
    }

    public Stream<String> runAsStream(@Nonnull Config options) {
        BlockingQueueIterator output = new BlockingQueueIterator();
        BlockingQueueIterator error = new BlockingQueueIterator();
        String[] args = buildArgs(options.systemPrompt, options.prompt, options.temperature);
        RunAsync.runAsync(getInvoker(args, output, error));
        return BlockingQueueIterator.createStream(output);
    }

    private Runnable getInvoker(String[] args, BlockingQueueIterator output, BlockingQueueIterator error) {
        if (modelType == LLAMA3) {
            return () -> {
                Llama3.STDOUT = QueuingOutputStream.createStream(output.getQueue());
                Llama3.STDERR = QueuingOutputStream.createStream(error.getQueue());
                try {
                    Llama3.main(args);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    output.setEnd();
                    error.setEnd();
                }
            };
        } else if (modelType == QWEN2) {
            return () -> {
                Qwen2.STDOUT = QueuingOutputStream.createStream(output.getQueue());
                Qwen2.STDERR = QueuingOutputStream.createStream(error.getQueue());
                try {
                    Qwen2.main(args);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    output.setEnd();
                    error.setEnd();
                }
            };
        }
        throw new IllegalStateException();
    }

    private String[] buildArgs(@Nullable String system, String user, float temperature) {
        List<String> args = new ArrayList<>();
        args.add("--model");
        args.add(modelPath.toString());
        args.add("--temperature");
        args.add(Float.toString(temperature));
        args.add("--instruct");
        if (system != null) {
            args.add("--system-prompt");
            args.add(system);
        }
        args.add("--prompt");
        args.add(user);
        return args.toArray(new String[0]);
    }

    /**
     * Run the Llama3 Model and parse the response to extract the context window information.
     * @param args The {@link String} arguments to pass to the {@link Llama3#main(String[])} method.
     * @return A {@link Response} object containing the model's output and metadata.
     */
    private Response invoke(String[] args) {
        Consumer<String[]> invocation = getConsumer(modelType);

        // Time the run for reporting to the caller
        long start = System.currentTimeMillis();
        StdOutUtils.Result result = StdOutUtils.executeWithRedirect(() -> {
            invocation.accept(args);
        });
        Duration duration = Duration.of(System.currentTimeMillis() - start, ChronoUnit.MILLIS);

        // Parse the STDERR to extract the context window information.
        ContextWindow window = result.err()
                .lines()
                .filter(ContextParser::isContextLine)
                .findFirst()
                .map(ContextParser::parseContext)
                .orElseGet(() -> new ContextWindow(0,0));

        return new Response(result.out(), window, duration);
    }
}
