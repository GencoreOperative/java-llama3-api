package uk.co.gencoreoperative.ai;

import static java.text.MessageFormat.format;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import javax.annotation.Nonnull;

import uk.co.gencoreoperative.runner.ContextParser;

/**
 * Indication of the amount of input context that is used by the request to the LLM.
 *
 * @param used The amount of context window that was used in the request.
 * @param total The total amount of context window that was available.
 */
public record ContextWindow(int used, int total) {
    @Override
    public @Nonnull String toString() {
        return format("ContextWindow(used={0}, total={1})", used, total);
    }

    /**
     * Parse the given text and search for the single line that contains the Context Window information.
     * <p>
     * If this is not found then an empty {@link ContextWindow} will be returned.
     * @param text Non-null, possibly empty.
     * @return Non-null {@link ContextWindow}.
     */
    public static ContextWindow parse(@Nonnull String text) {
        try (BufferedReader reader = new BufferedReader(new StringReader(text))) {
            return reader.lines()
                    .filter(ContextParser::isContextLine)
                    .findFirst()
                    .map(ContextParser::parseContext)
                    .orElseGet(() -> new ContextWindow(0,0));
        } catch (IOException e) {
            throw new IllegalStateException();
        }
    }
}
