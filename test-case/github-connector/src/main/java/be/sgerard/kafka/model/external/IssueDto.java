package be.sgerard.kafka.model.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class IssueDto {

    private Integer id;

    private String url;

    @JsonProperty("repository_url")
    private String repositoryUrl;

    private String labelsUrl;

    @JsonProperty("comments_url")
    private String commentsUrl;

    @JsonProperty("events_url")
    private String eventsUrl;

    @JsonProperty("html_url")
    private String htmlUrl;

    private Integer number;

    private String state;

    private String title;

    private String body;

    private UserDto user;

    private List<LabelDto> labels = null;

    private AssigneeDto assignee;

    private MilestoneDto milestone;

    private Boolean locked;

    private Integer comments;

    @JsonProperty("pull_request")
    private PullRequestDto pullRequest;

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("updated_at")
    private Instant updatedAt;

    private List<AssigneeDto> assignees = null;

}
