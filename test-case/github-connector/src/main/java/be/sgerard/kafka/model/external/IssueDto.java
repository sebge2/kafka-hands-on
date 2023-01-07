package be.sgerard.kafka.model.external;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class IssueDto {

    private Integer id;
    private String url;
    private String repositoryUrl;
    private String labelsUrl;
    private String commentsUrl;
    private String eventsUrl;
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
    private PullRequestDto pullRequest;
    private Object closedAt;
    private Instant createdAt;
    private Instant updatedAt;
    private List<AssigneeDto> assignees = null;
    private Map<String, Object> additionalProperties = new HashMap<>();

}
