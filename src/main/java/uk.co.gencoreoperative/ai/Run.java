package uk.co.gencoreoperative.ai;

import java.util.stream.Stream;

import javax.annotation.Nonnull;

/**
 * Represents the ability to run an AI request and get the response from that request.
 */
public interface Run {
    /**
     * Runs an AI request with the provided user prompts and returns a {@link Stream}
     * of response from the LLM as it is produced.
     *
     * @param prompt A non-null, possibly empty prompt.
     * @return A stream of response lines from the LLM.
     * @throws RuntimeException If there was an error performing the request.
     */
    Stream<String> runAsStream(@Nonnull String prompt) throws RuntimeException;

    /**
     * Runs an AI request with the provided system and user prompts and returns
     * a {@link Stream} of response from the LLM as it is produced.
     *
     * @param system A non-null text that guides the intention of the LLM request.
     * @param prompt The text provided from the user that the LLM is to respond to.
     * @return A stream of response lines from the LLM.
     * @throws RuntimeException If there was an error performing the request.
     */
    Stream<String> runAsStream(@Nonnull String system, @Nonnull String prompt) throws RuntimeException;

    /**
     * Perform a single AI request with the provided prompt.
     *
     * @param prompt A non-null, possibly empty prompt.
     * @return A possibly empty response from the LLM.
     * @throws RuntimeException If there was an error performing the request.
     */
    String run(@Nonnull String prompt) throws RuntimeException;

    /**
     * Perform a single AI request with the provided prompt which will generate a {@link Response}.
     *
     * @param prompt Non null prompt.
     * @return A {@link Response} payload that contains the response from the LLM as well as context information.
     * @throws RuntimeException If there was an error invoking the LLM
     */
    Response runWithResponse(@Nonnull String prompt) throws RuntimeException;

    /**
     * Perform a single AI request with the provided system and user prompts.
     *
     * @param system A non-null text that guides the intention of the LLM request.
     * @param user The text provided from the user that the LLM is to respond to.
     * @return A possibly empty response from the LLM.
     * @throws RuntimeException If there was an error performing the request.
     */
    String run(@Nonnull String system, @Nonnull String user) throws RuntimeException;

    /**
     * Perform a single AI request with the provided system and user prompts and generates a {@link Response}.
     *
     * @param system A non-null text that guides the intention of the LLM request.
     * @param user The text provided from the user that the LLM is to respond to.
     * @return A {@link Response} that contains the context of the response from the LLM.
     * @throws RuntimeException If there was an error performing the request.
     */
    Response runWithResponse(@Nonnull String system, @Nonnull String user) throws RuntimeException;
}
