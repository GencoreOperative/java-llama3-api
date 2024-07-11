package uk.co.gencoreoperative.llama3;

import static java.text.MessageFormat.format;

import java.io.IOException;
import java.nio.file.Path;

import javax.annotation.Nonnull;

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
     * The Llama3.java library we are using is best invoked using its {@link mukel.Llama3#main(String[])}
     * method. This call will generate stdout and stderr that will be re-diverted to simplify the
     * experience for the caller.
     * <p>
     * From the caller's perspective, we are only interested in the output of the model.
     *
     * @param prompt A non-null, possibly empty prompt.
     * @return The output of the model if any.
     */
    @Override
    public String run(@Nonnull String prompt) {
        String[] args = {"--model", modelPath.toString(), "--prompt", prompt, "--instruct"};
        return StdOutUtils.executeWithRedirect(() -> {
            try {
                mukel.Llama3.main(args);
            } catch (IOException e) {
                throw new RuntimeException(format("Failed to run Llama3: {0}", e.getMessage()), e);
            }
        });
    }
}
