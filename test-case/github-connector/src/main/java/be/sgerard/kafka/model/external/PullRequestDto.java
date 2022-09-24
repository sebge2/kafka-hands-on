package be.sgerard.kafka.model.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PullRequestDto {

    private String url;

    @JsonProperty("html_url")
    private String htmlUrl;

    @JsonProperty("diff_url")
    private String diffUrl;

    @JsonProperty("patch_url")
    private String patchUrl;

}
