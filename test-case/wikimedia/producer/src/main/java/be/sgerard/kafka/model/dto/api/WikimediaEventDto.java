package be.sgerard.kafka.model.dto.api;

public record WikimediaEventDto(
        String id,
        String title,
        String user,
        boolean bot,
        String serverName,
        String comment
) {
}
