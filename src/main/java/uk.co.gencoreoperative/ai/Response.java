package uk.co.gencoreoperative.ai;

import static java.text.MessageFormat.format;

/**
 * A {@link Response} from the LLM invocation.
 * <p>
 * When the LLM is invoked, this object will contain metrics about the request and response.
 *
 * @param response The {@link String} response from the LLM which may contain new line characters.
 * @param context {@link ContextWindow} containing response statistics.
 */
public record Response(String response, ContextWindow context) {
    public int getRemainingWindow() {
        return context.total() - context.used();
    }

    @Override
    public String toString() {
        return format("{0} ({1} of {2} context window",
                response, context.used(), context.total());
    }
}
