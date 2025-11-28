package uk.co.gencoreoperative.utils;

import static java.util.Spliterator.ORDERED;
import static java.util.Spliterators.spliteratorUnknownSize;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * An {@link Iterator} that will iterate over the contents of the provided
 * {@link BlockingQueue}.
 * <p>
 * Once the end of the queue has been identified, then the {@link #setEnd()}
 * signal can be made. This will result in the remaining elements in the queue
 * being consumed.
 */
public class BlockingQueueIterator implements Iterator<String> {
    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();
    private final String END_SIGNAL = "END_SIGNAL";
    private String next = null;

    public Queue<String> getQueue() {
        return queue;
    }

    public void setEnd() {
        queue.add(END_SIGNAL);
    }

    @Override
    public boolean hasNext() {
        if (next == null) {
            try {
                next = queue.take();
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

    public static Stream<String> createStream(BlockingQueueIterator iterator) {
        return StreamSupport.stream(spliteratorUnknownSize(iterator, ORDERED), false);
    }
}
