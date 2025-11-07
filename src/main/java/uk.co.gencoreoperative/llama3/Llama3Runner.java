package uk.co.gencoreoperative.llama3;

import static java.text.MessageFormat.format;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import javax.annotation.Nonnull;

import uk.co.gencoreoperative.ai.ContextWindow;
import uk.co.gencoreoperative.ai.Response;
import uk.co.gencoreoperative.ai.Run;
import uk.co.gencoreoperative.utils.ContextParser;
import uk.co.gencoreoperative.utils.StdOutUtils;
import mukel.Llama3;

/**
 * Based on the excellent work by <a href="https://github.com/mukel/llama3.java>mukel</a>,
 * this class runs the Llama3 model with the provided prompt.
 * <p>
 * The code provided was intended for use on the command line. This class adapts this by
 * capturing the output of the model and returning it as a string.
 * <p>
 * The Llama3.java library we are using is best invoked using its {@link mukel.Llama3#main(String[])}
 * method. This call will generate stdout and stderr that will be re-diverted to simplify the
 * experience for the caller. From the caller's perspective, we are only interested in the output
 * of the model.
 */
public class Llama3Runner implements Run {
    private final Path modelPath;
    private final boolean showErrorOutput;

    public Llama3Runner(Path modelPath) {
        this(modelPath, false);
    }

    public Llama3Runner(Path modelPath, boolean showError) {
        this.modelPath = modelPath;
        this.showErrorOutput = showError;
    }

    /**
     * Perform a single AI request with the provided prompt.
     *
     * @param prompt A non-null, possibly empty prompt.
     * @return The output of the model if any.
     */
    @Override
    public String run(@Nonnull String prompt) {
        return runWithResponse(prompt).response();
    }

    /**
     * Perform a single AI request with the provided prompt and return a {@link Response} object.
     * <p>
     * This method will provide more information about the LLM invocation for the caller.
     *
     * @param prompt A non-null, possibly empty prompt to send to the model.
     * @return A {@link Response} object containing the model's output and metadata.
     * @throws RuntimeException if the model invocation fails or an error occurs during processing.
     */
    @Override
    public Response runWithResponse(@Nonnull String prompt) throws RuntimeException {
        String[] args = {
                "--model", modelPath.toString(),
                "--instruct",
                "--prompt", prompt
        };
        return llama3(args);
    }

    /**
     * Perform a single AI request with the provided system and user prompts.
     *
     * @param system A non-null text that guides the intention of the LLM request.
     * @param user The text provided from the user that the LLM is to respond to.
     * @return The output of the model if any.
     */
    public String run(@Nonnull String system, @Nonnull String user) {
        return runWithResponse(system, user).response();
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
    public Response runWithResponse(@Nonnull String system, @Nonnull String user) throws RuntimeException {
        String[] args = {
                "--model", modelPath.toString(),
                "--instruct",
                "--system-prompt", system,
                "--prompt", user
        };
        return llama3(args);
    }

    /**
     * Run the Llama3 Model and parse the response to extract the context window information.
     * @param args The {@link String} arguments to pass to the {@link Llama3#main(String[])} method.
     * @return A {@link Response} object containing the model's output and metadata.
     */
    private Response llama3(String[] args) {
        Runnable runnable = () -> {
            try {
                Llama3.main(args);
            } catch (IOException e) {
                throw new RuntimeException(format("Failed to run Llama3: {0}", e.getMessage()), e);
            }
        };
        StdOutUtils.Result result = StdOutUtils.executeWithRedirect(runnable);

        // Parse the STDERR to extract the context window information.
        ContextWindow window = result.err()
                .lines()
                .filter(ContextParser::isContextLine)
                .findFirst()
                .map(ContextParser::parseContext)
                .orElseGet(() -> new ContextWindow(0,0));

        return new Response(result.out(), window);
    }
}
