package be.sgerard.kafka.service;

import be.sgerard.kafka.configuration.OpensearchProperties;
import be.sgerard.kafka.model.dto.WikimediaEventDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.action.bulk.BulkRequest;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.client.indices.CreateIndexRequest;
import org.opensearch.client.indices.GetIndexRequest;
import org.opensearch.common.xcontent.XContentType;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class WikimediaRecordService {

    private final RestHighLevelClient client;
    private final OpensearchProperties properties;

    public void record(List<WikimediaEventDto> events) throws IOException {
        if (events.isEmpty()) {
            return;
        }

        final BulkRequest bulkRequest = new BulkRequest();

        events.forEach(event -> bulkRequest.add(
                new IndexRequest(properties.getIndex())
                        .source(events, XContentType.JSON)
                        .id(event.getId())
        ));

        client.bulk(bulkRequest, RequestOptions.DEFAULT);

        log.info("{} records have been inserted.", events.size());
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
