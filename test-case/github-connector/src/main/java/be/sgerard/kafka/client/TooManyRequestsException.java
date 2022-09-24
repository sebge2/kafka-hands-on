package be.sgerard.kafka.client;

import be.sgerard.kafka.model.GitHubAccessLimit;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Exception thrown when the calling rate is out of limits.
 */
@RequiredArgsConstructor
@Getter
public class TooManyRequestsException extends RuntimeException {

    /**
     * {@link GitHubAccessLimit Access} limit to the GitHub API.
     */
    private final GitHubAccessLimit accessLimit;

}
