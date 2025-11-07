package uk.co.gencoreoperative.ai;

/**
 * Indication of the amount of input context that is used by the request to the LLM.
 *
 * @param used The amount of context window that was used in the request.
 * @param total The total amount of context window that was available.
 */
public record ContextWindow(int used, int total) {
    @Override
    public String toString() {
        return "ContextWindow{" +
                "used=" + used +
                ", total=" + total +
                '}';
    }
}
