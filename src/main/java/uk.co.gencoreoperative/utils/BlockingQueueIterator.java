package uk.co.gencoreoperative.utils;

import static java.util.Spliterator.ORDERED;
import static java.util.Spliterators.spliteratorUnknownSize;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.Nonnull;

/**
 * An {@link Iterator} that is capable of iterating over a {@link BlockingQueue}.
 * <p>
 * This iterator is somewhat specialised in that it will read from its {@link BlockingQueue}
 * until it has been signalled to stop using {@link #setEnd()}. At which point it will
 * continue to iterate over the remaining items in the {@link BlockingQueue} before
 * indicating there is no more elements to iterate over.
 * <p>
 * In order to add items to the {@link BlockingQueue}, use the {@link #getQueue()}
 * method to retrieve the queue.
 */
public class BlockingQueueIterator implements Iterator<String> {
    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();
    private final String END_SIGNAL = "END_SIGNAL";
    private String next = null;


    /**
     * Returns the underlying {@link Queue} used by this iterator for adding elements.
     *
     * @return A non-null {@link Queue} instance.
     */
    public @Nonnull Queue<String> getQueue() {
        return queue;
    }

    /**
     * Signals that no more elements will be added to the queue.
     * After calling this method, the iterator will finish iterating
     * over any remaining elements and then terminate.
     */
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

    /**
     * Utility method that will generate a {@link Stream} over the given {@link Iterator}
     * @param iterator Required iterator to wrap.
     * @return A non-null, possibly empty stream.
     */
    public static @Nonnull Stream<String> createStream(@Nonnull Iterator<String> iterator) {
        return StreamSupport.stream(spliteratorUnknownSize(iterator, ORDERED), false);
    }
}
