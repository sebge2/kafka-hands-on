package be.sgerard.kafka.service;

import be.sgerard.kafka.configuration.OpensearchProperties;
import be.sgerard.kafka.model.dto.WikimediaEventDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.client.indices.CreateIndexRequest;
import org.opensearch.client.indices.GetIndexRequest;
import org.opensearch.common.xcontent.XContentType;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
@AllArgsConstructor
@Slf4j
public class WikimediaRecordService {

    private final RestHighLevelClient client;
    private final OpensearchProperties properties;
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    public void record(String event) throws IOException {
        final WikimediaEventDto eventDto = objectMapper.readValue(event, WikimediaEventDto.class);

        final IndexRequest indexRequest = new IndexRequest(properties.getIndex())
                .source(event, XContentType.JSON)
                .id(eventDto.id());

        client.index(indexRequest, RequestOptions.DEFAULT);

        log.info("Record inserted.");
    }

    @PostConstruct
    public void setup() throws IOException {
        if (client.indices().exists(new GetIndexRequest(properties.getIndex()), RequestOptions.DEFAULT)) {
            log.info("The index [{}] already exists.", properties.getIndex());
            return;
        }

        client.indices().create(new CreateIndexRequest(properties.getIndex()), RequestOptions.DEFAULT);

        log.info("The index [{}] has been created.", properties.getIndex());
    }
}
