package be.sgerard.kafka;

import be.sgerard.kafka.client.GitHubClient;
import be.sgerard.kafka.client.TooManyRequestsException;
import be.sgerard.kafka.model.GitHubIssueResponse;
import be.sgerard.kafka.model.GitHubPollingState;
import be.sgerard.kafka.model.GitHubSourceConnectorConfig;
import be.sgerard.kafka.model.external.IssueDto;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static be.sgerard.kafka.model.GitHubSchemas.*;
import static be.sgerard.kafka.model.GitHubSourceConnectorConfig.*;

/**
 * {@link SourceTask Task} polling GitHub issues.
 */
public class GitHubSourceTask extends SourceTask {

    private static final Logger log = LoggerFactory.getLogger(GitHubSourceTask.class);

    private final IssueMapper issueMapper = new IssueMapper();
    private GitHubClient client;
    private GitHubPollingState currentState;

    public GitHubSourceTask() {
    }

    @Override
    public String version() {
        return VersionUtil.getVersion();
    }

    @Override
    public void start(Map<String, String> map) {
        log.info("Starting task.");

        final GitHubSourceConnectorConfig config = new GitHubSourceConnectorConfig(map);

        client = new GitHubClient(config);
        currentState = GitHubPollingState.fromContext(context, config);
    }

    @Override
    public List<SourceRecord> poll() throws InterruptedException {
        log.debug("Polling issues.");

        sleepIfNeeded();

        final List<SourceRecord> records = new ArrayList<>();

        final GitHubIssueResponse issues = getNextIssues();
        currentState.setAccessLimit(issues.getAccessLimit());

        for (IssueDto issue : issues.getIssues()) {
            records.add(createSourceRecord(issue));
            currentState.setNextQuerySince(issue.getUpdatedAt());
        }

        if (issues.getIssues().size() >= DEFAULT_BATCH_SIZE_VALUE) {
            currentState.incrementPageToVisit();
        } else {
            // nextQuerySince = lastUpdatedAt.plusSeconds(1); TODO unsure of this in the original code?

            currentState.resetPageToVisit();

            sleep();
        }
        return records;
    }

    @Override
    public void stop() {
        log.info("Stopping task.");
    }

    /**
     * Returns the next GitHub issues.
     */
    private GitHubIssueResponse getNextIssues() throws InterruptedException {
        try {
            final GitHubIssueResponse issues = client.getIssues(currentState.getNextPageToVisit(), currentState.getNextQuerySince());

            log.info(String.format("Fetched %s ìssues(s), current page=%s, next query since=%s", issues.getIssues().size(), currentState.getNextPageToVisit(), currentState.getNextQuerySince()));

            return issues;
        } catch (TooManyRequestsException e) {
            this.currentState.setAccessLimit(e.getAccessLimit());

            this.sleep();

            return getNextIssues();
        }
    }

    /**
     * Creates a {@link SourceRecord record} from the specified {@link IssueDto issue}.
     */
    private SourceRecord createSourceRecord(IssueDto issue) {
        return new SourceRecord(
                currentState.toSourcePartition(),
                currentState.toSourceOffset(issue.getUpdatedAt()),
                currentState.getConfig().getTopic(),
                null, // partition will be inferred by the framework
                KEY_SCHEMA,
                issueMapper.mapToKey(issue, currentState.getConfig()),
                VALUE_SCHEMA,
                issueMapper.mapToRecord(issue),
                issue.getUpdatedAt().toEpochMilli()
        );
    }

    /**
     * Sleeps in order to not reach the limit.
     */
    private void sleep() throws InterruptedException {
        final long sleepTime = currentState.getAccessLimit().getSleepingTimeInMs();

        log.debug(String.format("Sleeping for %s ms.", sleepTime));

        Thread.sleep(sleepTime);
    }

    /**
     * Sleeps if the rate limit is reached or approaching.
     */
    private void sleepIfNeeded() throws InterruptedException {
        if (!currentState.getAccessLimit().isSleepNeeded()) {
            return;
        }

        log.debug(String.format("Approaching limit soon, you have %s requests left.", currentState.getAccessLimit().getRateRemaining()));
        sleep();
    }

}