package be.sgerard.kafka.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WikimediaEventDto(Integer id,
                                String title,
                                String user,
                                boolean bot,
                                @JsonProperty("server_name") String serverName,
                                @JsonProperty("parsedcomment") String comment) {
}
