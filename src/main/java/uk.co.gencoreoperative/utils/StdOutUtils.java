package uk.co.gencoreoperative.utils;

import static java.text.MessageFormat.format;
import static java.util.Spliterator.ORDERED;
import static java.util.Spliterators.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.Nonnull;

public class StdOutUtils {
    /**
     * Execute the provided {@link Runnable} and capture the {@code STDOUT}.
     * <p>
     * When working with functions that output to stdout and stderr, there is the likelihood that the function will
     * also call {@link System#exit(int)}. In these cases, the best we can do is to capture the error output and
     * throw this as an exception before the shutdown continues.
     * <p>
     * <b>Note:</b> This method is not thread-safe.
     * <p>
     * @param runnable A non-null {@link Runnable} to execute.
     * @return The output from running the function will be returned.
     * @throws RuntimeException If there was an unexpected error thrown from the function.
     */
    public static Result executeWithRedirect(@Nonnull Runnable runnable) throws RuntimeException {
        PrintStream previousOut = System.out;
        PrintStream previousErr = System.err;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream(); ByteArrayOutputStream error = new ByteArrayOutputStream()) {
            System.setOut(new PrintStream(out, true));
            System.setErr(new PrintStream(error, true));

            /*
            This shutdown hook is not going to contribute to the response from this method
            It will only be used when the {@code runnable} calls System.exit(int)
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
            return new Result(out.toString(), error.toString());
        } catch (IOException e) {
            throw new RuntimeException(format("Unexpected IO Error: {0}", e.getMessage()), e);
        } finally {
            System.setOut(previousOut);
            System.setErr(previousErr);
        }
    }

    public record Result(String out, String err){}

    // TODO - this code is neat, but it is a bit messy with the STDOUT manipulation.
    // TODO - we can simplify all of this if we provide Llama3 / Qwen2 with a dedicated
    // TODO - PrintStream that they are to output to. This then lets us control exactly
    // TODO - how we manipulate them.
    public static Stream<String> executeWithStreamingRedirect(Runnable runnable) {
        Iterator<String> wordStream = new Iterator<>() {
            private final BlockingQueue<String> letters = new LinkedBlockingQueue<>();
            private final String END_SIGNAL = "END_SIGNAL";
            private final Future<?> future = startThread();
            private String next = null;

            private Future<?> startThread() {
                ExecutorService service = Executors.newSingleThreadExecutor();
                return service.submit(new Wrapper(service));
            }

            private class Wrapper implements Runnable {
                private final ExecutorService service;
                public Wrapper(ExecutorService service) {
                    this.service = service;
                }
                @Override
                public void run() {
                    PrintStream oldOut = System.out;
                    try {
                        System.setOut(QueuingOutputStream.createStream(letters));
                        runnable.run();
                    } finally {
                        System.setOut(oldOut);
                        letters.add(END_SIGNAL);
                        service.shutdown();
                    }
                }
            }

            @Override
            public boolean hasNext() {
                if (next == null) {
                    try {
                        next = letters.take();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                return !END_SIGNAL.equals(next);
            }

            @Override
            public String next() {
                if (next == null) throw new IllegalStateException();
                if (END_SIGNAL.equals(next)) throw new IllegalStateException();

                String value = next;
                next = null;
                return value;
            }
        };
        return StreamSupport.stream(spliteratorUnknownSize(wordStream, ORDERED), false);
    }
}
