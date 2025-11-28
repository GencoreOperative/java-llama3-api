package uk.co.gencoreoperative.utils;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Queue;

import javax.annotation.Nonnull;

/**
 * An {@link OutputStream} that captures the characters written to it to a {@link Queue}.
 */
public class QueuingOutputStream extends OutputStream {
    private final Queue<String> output;
    private final byte[] buffer = new byte[1];
        public QueuingOutputStream(Queue<String> output) {
        this.output = output;
    }
    @Override
    public void write(int b) throws IOException {
        buffer[0] = (byte) b;
        output.add(new String(buffer, UTF_8));
    }

    /**
     * Create a {@link PrintWriter} that can be used for outputting a character streams which will then be
     * captured into the provided {@link Queue}.
     * @param output A queue that will capture each character output to the stream.
     * @return
     */
    public static PrintStream createStream(@Nonnull Queue<String> output) {
        return new PrintStream(new QueuingOutputStream(output), true);
    }
}
