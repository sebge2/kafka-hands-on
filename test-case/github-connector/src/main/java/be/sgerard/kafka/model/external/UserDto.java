package be.sgerard.kafka.model.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {

    private String login;

    private Integer id;

    private String url;
}
