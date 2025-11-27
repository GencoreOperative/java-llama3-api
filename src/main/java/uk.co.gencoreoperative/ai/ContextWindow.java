package uk.co.gencoreoperative.ai;

import static java.text.MessageFormat.format;

import javax.annotation.Nonnull;

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
}
