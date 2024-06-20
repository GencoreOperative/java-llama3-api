package uk.co.gencoreoperative.utils;

import static java.text.MessageFormat.format;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import javax.annotation.Nonnull;

public class StdOutUtils {
    /**
     * Execute the provided {@link Runnable} and capture the {@code STDOUT}.
     * <p>
     * <b>Note:</b> This method is not thread-safe.
     * @param runnable A non-null {@link Runnable} to execute.
     * @return The output from running the function will be returned.
     * @throws RuntimeException If there was an unexpected error thrown from the function.
     */
    public static String executeWithRedirect(@Nonnull Runnable runnable) throws RuntimeException {
        PrintStream previousOut = System.out;
        PrintStream previousErr = System.err;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream(); ByteArrayOutputStream error = new ByteArrayOutputStream()) {
            System.setOut(new PrintStream(out, true));
            System.setErr(new PrintStream(error, true));
            try {
                runnable.run();
            } catch (Exception e) {
                throw new RuntimeException(format("Failed to execute function: {0}", e.getMessage()), e);
            }
            return out.toString();
        } catch (IOException e) {
            throw new RuntimeException(format("Unexpected IO Error: {0}", e.getMessage()), e);
        } finally {
            System.setOut(previousOut);
            System.setErr(previousErr);
        }
    }
}
