package uk.co.gencoreoperative.ai;

import static java.text.MessageFormat.format;

/**
 *
 * @param response
 * @param context
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
