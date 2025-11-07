package uk.co.gencoreoperative.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.co.gencoreoperative.ai.ContextWindow;

/**
 * Utility class for parsing the Llama3 context window information.
 * <p>
 * This class provides methods to identify and extract context window usage from strings
 * typically formatted as "context: <used>/<total> ...". The extracted values are returned
 * as {@link uk.co.gencoreoperative.ai.ContextWindow} objects.
 */
public class ContextParser {

    private static final String CONTEXT = "context:";
    // Precompile the regex for efficiency
    private static final Pattern CONTEXT_PATTERN = Pattern.compile("context:\\s*(\\d+)/(\\d+)");

    /**
     * Checks if the given line contains context window information.
     * <p>
     * A context line is expected to contain the substring {@link #CONTEXT}.
     *
     * @param line the input string to check
     * @return true if the line contains context window information, false otherwise
     */
    public static boolean isContextLine(String line) {
        return line.contains(CONTEXT);
    }

    /**
     * Parses the context window usage from the given input string.
     *
     * @param input the input string containing context window information
     * @return a {@link ContextWindow} object with the used and total context values
     * @throws IllegalStateException if the input does not contain valid context window information
     */
    public static ContextWindow parseContext(String input) throws IllegalStateException {
        Matcher matcher = CONTEXT_PATTERN.matcher(input);
        if (matcher.find()) {
            int contextRemaining = Integer.parseInt(matcher.group(1));
            int contextTotal = Integer.parseInt(matcher.group(2));
            return new ContextWindow(contextRemaining, contextTotal);
        } else {
            throw new IllegalStateException("Failed to parse: " + input);
        }
    }
}
