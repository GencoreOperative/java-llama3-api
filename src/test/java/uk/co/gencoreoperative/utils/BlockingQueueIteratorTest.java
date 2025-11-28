package uk.co.gencoreoperative.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.forgerock.cuppa.Cuppa.beforeEach;
import static org.forgerock.cuppa.Cuppa.describe;
import static org.forgerock.cuppa.Cuppa.it;
import static org.forgerock.cuppa.Cuppa.when;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.forgerock.cuppa.junit.CuppaRunner;
import org.junit.runner.RunWith;

@RunWith(CuppaRunner.class)
public class BlockingQueueIteratorTest {
    private BlockingQueueIterator iterator;

    {
        describe(BlockingQueueIterator.class.getSimpleName(), () -> {
            beforeEach(() -> {
                iterator = new BlockingQueueIterator();
            });
            when("there is a queue of items", () -> {
                beforeEach(() -> {
                    iterator.getQueue().add("Hello");
                    iterator.getQueue().add("World");
                    iterator.setEnd();
                });
                it("iterates over each of them", () -> {
                    assertThat(iterator).hasSize(2);
                });
            });

            when("the queue is empty", () -> {
                beforeEach(() -> {
                    iterator.setEnd();
                });
                it("iterates over each of them", () -> {
                    assertThat(iterator).isEmpty();
                });
            });
        });
    }
}