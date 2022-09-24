package be.sgerard.kafka.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.connect.source.SourceTaskContext;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static be.sgerard.kafka.model.GitHubSchemas.NEXT_PAGE_FIELD;
import static be.sgerard.kafka.model.GitHubSchemas.UPDATED_AT_FIELD;
import static be.sgerard.kafka.model.GitHubSourceConnectorConfig.GITHUB_OWNER;
import static be.sgerard.kafka.model.GitHubSourceConnectorConfig.GITHUB_REPOSITORY;
import static be.sgerard.kafka.utils.TimeUtils.takeLatest;

/**
 * Current state of a GitHub polling.
 */
@Getter
@Setter
public class GitHubPollingState {

    public static GitHubPollingState fromContext(SourceTaskContext context, GitHubSourceConnectorConfig config) {
        final Map<String, Object> lastSourceOffset = context.offsetStorageReader().offset(toSourcePartition(config));

        if (lastSourceOffset == null) {
            return new GitHubPollingState(config, config.getSince());
        } else {
            return new GitHubPollingState(
                    config,
                    Instant.parse((String) lastSourceOffset.get(UPDATED_AT_FIELD)),
                    Integer.valueOf((String) lastSourceOffset.get(NEXT_PAGE_FIELD))
            );
        }
    }

    private final GitHubSourceConnectorConfig config;

    private GitHubAccessLimit accessLimit = GitHubAccessLimit.unknown();
    private Instant nextQuerySince;
    private Integer nextPageToVisit;

    private GitHubPollingState(GitHubSourceConnectorConfig config,
                               Instant nextQuerySince) {
        this(config, nextQuerySince, null);
    }

    private GitHubPollingState(GitHubSourceConnectorConfig config,
                               Instant nextQuerySince,
                               Integer nextPageToVisit) {
        this.config = config;
        this.nextQuerySince = nextQuerySince;
        this.nextPageToVisit = nextPageToVisit;

        if (this.nextPageToVisit == null) {
            resetPageToVisit();
        }
    }

    /**
     * Increments the next page to visit.
     */
    @SuppressWarnings("UnusedReturnValue")
    public Integer incrementPageToVisit() {
        return ++nextPageToVisit;
    }

    /**
     * Resets the next page to visit to the initial value.
     */
    @SuppressWarnings("UnusedReturnValue")
    public Integer resetPageToVisit() {
        return nextPageToVisit = 1;
    }

    /**
     * Returns the source partition.
     */
    public Map<String, String> toSourcePartition() {
        return toSourcePartition(config);
    }

    /**
     * Returns the offset that will be used to reset this state.
     */
    public Map<String, String> toSourceOffset(Instant updatedAt) {
        final Map<String, String> map = new HashMap<>();
        map.put(UPDATED_AT_FIELD, takeLatest(updatedAt, nextQuerySince).toString());
        map.put(NEXT_PAGE_FIELD, nextPageToVisit.toString());

        return map;
    }

    /**
     * Returns the source partition.
     */
    private static Map<String, String> toSourcePartition(GitHubSourceConnectorConfig config) {
        final Map<String, String> map = new HashMap<>();
        map.put(GITHUB_OWNER, config.getGithubOwner());
        map.put(GITHUB_REPOSITORY, config.getGithubRepository());

        return map;
    }
}

