package be.sgerard.kafka;

import be.sgerard.kafka.model.GitHubSourceConnectorConfig;
import be.sgerard.kafka.model.external.IssueDto;
import be.sgerard.kafka.model.external.PullRequestDto;
import be.sgerard.kafka.model.external.UserDto;
import org.apache.kafka.connect.data.Struct;

import java.util.Optional;

import static be.sgerard.kafka.model.GitHubSchemas.*;

public class IssueMapper {

    public Struct mapToKey(IssueDto issue, GitHubSourceConnectorConfig config) {
        return new Struct(KEY_SCHEMA)
                .put(OWNER_FIELD, config.getGithubOwner())
                .put(REPOSITORY_FIELD, config.getGithubRepository())
                .put(NUMBER_FIELD, issue.getNumber());
    }

    public Struct mapToRecord(IssueDto issue) {
        final Struct valueStruct = new Struct(VALUE_SCHEMA)
                .put(URL_FIELD, issue.getUrl())
                .put(TITLE_FIELD, issue.getTitle())
                .put(CREATED_AT_FIELD, issue.getCreatedAt().toEpochMilli())
                .put(UPDATED_AT_FIELD, issue.getUpdatedAt().toEpochMilli())
                .put(NUMBER_FIELD, issue.getNumber())
                .put(STATE_FIELD, issue.getState())
                .put(USER_FIELD, mapToRecord(issue.getUser()));

        Optional
                .ofNullable(issue.getPullRequest())
                .map(this::mapToRecord)
                .ifPresent(struct -> valueStruct.put(PR_FIELD, struct));

        return valueStruct;
    }

    private Struct mapToRecord(UserDto user) {
        return new Struct(USER_SCHEMA)
                .put(USER_URL_FIELD, user.getUrl())
                .put(USER_ID_FIELD, user.getId())
                .put(USER_LOGIN_FIELD, user.getLogin());
    }

    private Struct mapToRecord(PullRequestDto pullRequest) {
        return new Struct(PR_SCHEMA)
                .put(PR_URL_FIELD, pullRequest.getUrl())
                .put(PR_HTML_URL_FIELD, pullRequest.getHtmlUrl());
    }

}
