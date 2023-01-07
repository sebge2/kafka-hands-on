package be.sgerard.kafka.model;

import be.sgerard.kafka.model.external.IssueDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class GitHubIssueResponse {

    /**
     * All {@link IssueDto issues} retrieved from GitHub.
     */
    private final List<IssueDto> issues;

    /**
     * {@link GitHubAccessLimit Access} limit to the GitHub API.
     */
    private final GitHubAccessLimit accessLimit;
}
