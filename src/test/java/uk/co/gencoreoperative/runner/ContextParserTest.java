package uk.co.gencoreoperative.runner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.forgerock.cuppa.Cuppa.beforeEach;
import static org.forgerock.cuppa.Cuppa.describe;
import static org.forgerock.cuppa.Cuppa.it;
import static org.forgerock.cuppa.Cuppa.when;

import org.forgerock.cuppa.junit.CuppaRunner;
import org.junit.runner.RunWith;

import uk.co.gencoreoperative.ai.ContextWindow;

@RunWith(CuppaRunner.class)
public class ContextParserTest {
    String FIRST = "context: 19/512 prompt: 19.24 tokens/s (17) generation: 34.80 tokens/s (2)";
    String SECOND = "context: 31/512 prompt: 40.37 tokens/s (25) generation: 25.46 tokens/s (6)";

    private ContextWindow result;

    {
        describe(ContextParser.class.getSimpleName(), () -> {
            when("parsing FIRST", () -> {
                beforeEach(() -> {
                    result = ContextParser.parseContext(FIRST);
                });
                it("matches 19/512", () -> {
                    assertThat(result.used()).isEqualTo(19);
                    assertThat(result.total()).isEqualTo(512);
                });
            });

            when("parsing SECOND", () -> {
                beforeEach(() -> {
                    result = ContextParser.parseContext(SECOND);
                });
                it("matches 31/512", () -> {
                    assertThat(result.used()).isEqualTo(31);
                    assertThat(result.total()).isEqualTo(512);
                });
            });
        });
    }
}

