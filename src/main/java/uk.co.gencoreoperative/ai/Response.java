package uk.co.gencoreoperative.ai;

import static java.text.MessageFormat.format;

import java.time.Duration;

import javax.annotation.Nonnull;

/**
 * A {@link Response} from the LLM invocation.
 * <p>
 * When the LLM is invoked, this object will contain metrics about the request and response.
 *
 * @param response The {@link String} response from the LLM which may contain new line characters.
 * @param context {@link ContextWindow} containing response statistics.
 * @param duration The time taken to generate the response as a {@link Duration}.
 */
public record Response(String response, ContextWindow context, Duration duration) {
    public int getRemainingWindow() {
        return context.total() - context.used();
    }

    @Override
    public @Nonnull String toString() {
        return format("{0}\nTime: {3}s\nContext: {1} of {2}",
                response, context.used(), context.total(), duration.toSeconds());
    }
}

