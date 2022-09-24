package be.sgerard.kafka.model.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class IssueDto {

    private Integer id;

    private String url;

    private Integer number;

    private String state;

    private String title;

    private UserDto user;

    @JsonProperty("pull_request")
    private PullRequestDto pullRequest;

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("updated_at")
    private Instant updatedAt;

}
