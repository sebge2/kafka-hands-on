package be.sgerard.kafka.model.external;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class LabelDto {

    private Integer id;
    private String url;
    private String name;
    private String color;
    private Boolean _default;
    private Map<String, Object> additionalProperties = new HashMap<>();


}
