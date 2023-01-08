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

    /**
     * The remaining call under which a sleep is needed.
     */
    public static final int SLEEP_THRESHOLD = 10;

    /**
     * Returns limits based on the current response headers.
     */
    public static GitHubAccessLimit fromHeaders(Headers headers) {
        return new GitHubAccessLimit(
                Integer.valueOf(headers.getFirst("X-RateLimit-Limit")),
                Integer.valueOf(headers.getFirst("X-RateLimit-Remaining")),
                Instant.ofEpochSecond(Long.parseLong(headers.getFirst("X-RateLimit-Reset")))
        );
    }

    /**
     * Returns unknown limits.
     */
    public static GitHubAccessLimit unknown() {
        return new GitHubAccessLimit(
                9999,
                9999,
                Instant.now()
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

    /**
     * Returns whether sleeping is needed.
     */
    public boolean isSleepNeeded() {
        return (rateRemaining <= SLEEP_THRESHOLD) && (rateRemaining > 0);
    }

    /**
     * Returns the number of milliseconds to sleep.
     */
    public long getSleepingTimeInMs() {
        return (long) Math.ceil((double) (rateReset.getEpochSecond() - Instant.now().getEpochSecond()) / rateRemaining) * 1000L;
    }
}
