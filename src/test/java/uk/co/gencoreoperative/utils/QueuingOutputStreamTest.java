package uk.co.gencoreoperative.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.forgerock.cuppa.Cuppa.beforeEach;
import static org.forgerock.cuppa.Cuppa.describe;
import static org.forgerock.cuppa.Cuppa.it;
import static org.forgerock.cuppa.Cuppa.when;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.Queue;

import org.forgerock.cuppa.junit.CuppaRunner;
import org.junit.runner.RunWith;

@RunWith(CuppaRunner.class)
public class QueuingOutputStreamTest {
    private final Queue<String> characters = new LinkedList<>();
    private final PrintStream stream = QueuingOutputStream.createStream(characters);

    {
        describe(QueuingOutputStream.class.getSimpleName(), () -> {
            beforeEach(characters::clear);
            when("outputting characters", () -> {
                beforeEach(() -> {
                    stream.println("Hello World!");
                });

                it("captures all the characters", () -> {
                    assertThat(characters).hasSize(13);
                });

                it("starts with H", () -> {
                    assertThat(characters).startsWith("H");
                });
            });
        });
    }
}