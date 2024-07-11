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
     * If the function prints to {@code STDERR}, then we will notify the caller by throwning a {@link RuntimeException}
     * with the error message.
     * <p>
     * When working with functions that output to stdout and stderr, there is the likelihood that the function will
     * also call {@link System#exit(int)}. In these cases, the best we can do is to capture the error output and
     * throw this as an exception before the shutdown continues.
     * <p>
     * <b>Note:</b> This method is not thread-safe.
     * <p>
     * @param runnable A non-null {@link Runnable} to execute.
     * @return The output from running the function will be returned.
     * @throws RuntimeException If there was an unexpected error thrown from the function or if the function printed
     * output to the stderr.
     */
    public static String executeWithRedirect(@Nonnull Runnable runnable) throws RuntimeException {
        PrintStream previousOut = System.out;
        PrintStream previousErr = System.err;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream(); ByteArrayOutputStream error = new ByteArrayOutputStream()) {
            System.setOut(new PrintStream(out, true));
            System.setErr(new PrintStream(error, true));

            /*
            This shutdown hook is not going to contribute to the response from this method
            It will only be used when the function calls System.exit(int)
            In this case, there is nothing we can do, but print the output and error
            so that the user can do something with it.
             */
            Thread hook = new Thread(() -> {
                previousOut.println(out);
                if (error.size() > 0) {
                    previousErr.println(format("Error: {0}", error.toString()));
                }
            });
            Runtime.getRuntime().addShutdownHook(hook);

            try {
                runnable.run();
                out.flush();
                error.flush();
            } catch (Exception e) {
                throw new RuntimeException(format("Failed to execute function: {0}", e.getMessage()), e);
            } finally {
                Runtime.getRuntime().removeShutdownHook(hook);
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
