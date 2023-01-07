package be.sgerard.kafka.model.external;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class MilestoneDto {

    private String url;
    private String htmlUrl;
    private String labelsUrl;
    private Integer id;
    private Integer number;
    private String state;
    private String title;
    private String description;
    private CreatorDto creator;
    private Integer openIssues;
    private Integer closedIssues;
    private String createdAt;
    private String updatedAt;
    private String closedAt;
    private String dueOn;
    private Map<String, Object> additionalProperties = new HashMap<>();
}
