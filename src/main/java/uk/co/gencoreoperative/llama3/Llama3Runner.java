package uk.co.gencoreoperative.llama3;

import static java.text.MessageFormat.format;
import static mukel.Llama3.selectSampler;

import java.io.IOException;
import java.nio.file.Path;

import javax.annotation.Nonnull;

import mukel.Llama;
import mukel.Llama3;
import mukel.ModelLoader;
import mukel.Options;
import mukel.Sampler;
import uk.co.gencoreoperative.ai.Run;
import uk.co.gencoreoperative.utils.StdOutUtils;

/**
 * Based on the excellent work by <a href="https://github.com/mukel/llama3.java>mukel</a>,
 * this class runs the Llama3 model with the provided prompt.
 * <p>
 * The code provided was intended for use on the command line. This class adapts this by
 * capturing the output of the model and returning it as a string.
 */
public class Llama3Runner implements Run {
    private final Path modelPath;

    public Llama3Runner(Path modelPath) {
        this.modelPath = modelPath;
    }

    /**
     * Perform a single AI request with the provided prompt.
     * <p>
     * The model loading code will generate {@code StdErr} output and the
     * {@link Llama3#runInstructOnce(Llama, Sampler, Options)} method will generate {@code StdOut}.
     * <p>
     * From the caller's perspective, we will only be interested in the output of the model.
     *
     * @param prompt A non-null, possibly empty prompt.
     * @return The output of the model if any.
     */
    @Override
    public String run(@Nonnull String prompt) {
        return StdOutUtils.executeWithRedirect(() -> {
            Options options = getOptions(prompt);
            Llama model = loadModel(options);
            Sampler sampler = getSampler(model, options);
            Llama3.runInstructOnce(model, sampler, options);
        });
    }

    private Options getOptions(String prompt) {
        return new Options(modelPath, prompt, "", false,
                Options.DEFAULT_TEMPERATURE, Options.DEFAULT_TOPP, Options.DEFAULT_SEED,
                Options.DEFAULT_MAX_TOKENS, false, false);
    }

    private static Sampler getSampler(Llama model, Options options) {
        return selectSampler(model.configuration().vocabularySize,
                options.temperature(),
                options.topp(),
                options.maxTokens());
    }

    private static Llama loadModel(Options options) {
        try {
            return ModelLoader.loadModel(options.modelPath(), options.maxTokens());
        } catch (IOException e) {
            throw new RuntimeException(format("Failed to load model: {0}", e.getMessage()), e);
        }
    }
}
