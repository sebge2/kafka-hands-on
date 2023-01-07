package be.sgerard.kafka.model;

import com.mashape.unirest.http.Headers;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.Instant;

/**
 * Limit of the access to GitHub.
 */
@RequiredArgsConstructor
@Getter
@ToString
public class GitHubAccessLimit {

    public static GitHubAccessLimit fromHeaders(Headers headers) {
        return new GitHubAccessLimit(
                Integer.valueOf(headers.getFirst("X-RateLimit-Limit")),
                Integer.valueOf(headers.getFirst("X-RateLimit-Remaining")),
                Instant.ofEpochSecond(Long.parseLong(headers.getFirst("X-RateLimit-Reset")))
        );
    }

    /**
     * The rate call limit.
     */
    private final Integer rateLimit;

    /**
     * Remaining calls.
     */
    private final Integer rateRemaining;

    /**
     * Time when the limit reset.
     */
    private final Instant rateReset;
}
