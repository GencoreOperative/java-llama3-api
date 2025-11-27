package uk.co.gencoreoperative.runner;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import uk.co.gencoreoperative.ai.ContextWindow;

public class ContextParserTest {
    @Test
    public void shouldParseFirstExample() {
        String input = "context: 19/512 prompt: 19.24 tokens/s (17) generation: 34.80 tokens/s (2)";
        ContextWindow result = ContextParser.parseContext(input);
        assertThat(result.used()).isEqualTo(19);
        assertThat(result.total()).isEqualTo(512);
    }

    @Test
    public void shouldParseExampleTwo() {
        String input = "context: 31/512 prompt: 40.37 tokens/s (25) generation: 25.46 tokens/s (6)";
        ContextWindow result = ContextParser.parseContext(input);
        assertThat(result.used()).isEqualTo(31);
        assertThat(result.total()).isEqualTo(512);
    }
}

