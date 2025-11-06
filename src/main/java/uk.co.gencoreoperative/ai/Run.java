package uk.co.gencoreoperative.ai;

import javax.annotation.Nonnull;

/**
 * Represents the ability to run an AI request and get the response from that request.
 */
public interface Run {
    /**
     * Perform a single AI request with the provided prompt.
     * @param prompt A non-null, possibly empty prompt.
     * @return A possibly empty response from the LLM.
     * @throws RuntimeException If there was an error performing the request.
     */
    String run(@Nonnull String prompt) throws RuntimeException;

    /**
     * Perform a single AI request with the provided system and user prompts.
     * @param system A non-null text that guides the intention of the LLM request.
     * @param user The text provided from the user that the LLM is to respond to.
     * @return A possibly empty response from the LLM.
     * @throws RuntimeException If there was an error performing the request.
     */
    String run(@Nonnull String system, @Nonnull String user) throws RuntimeException;

}
