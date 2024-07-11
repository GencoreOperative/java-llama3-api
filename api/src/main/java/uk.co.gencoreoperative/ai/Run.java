package uk.co.gencoreoperative.ai;

import javax.annotation.Nonnull;

/**
 * Represents the ability to run an AI request and get the response from that request.
 */
public interface Run {
    /**
     * Perform a single AI request with the provided prompt.
     * @param prompt A non-null, possibly empty prompt.
     * @throws RuntimeException If there was an error performing the request.
     */
    String run(@Nonnull String prompt) throws RuntimeException;
}
