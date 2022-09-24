package be.sgerard.kafka;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.junit.jupiter.api.Test;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestClientBuilder;
import org.opensearch.client.RestHighLevelClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@SpringBootTest
class WikimediaConsumerApplicationTest {

    @Test
    void contextLoads() {
    }

    @Configuration
    static class TestConfiguration {

        @Bean
        @Primary
        RestHighLevelClient opensearchClient() {
            final RestClientBuilder builder = RestClient.builder(new HttpHost("localhost"));

            builder.setHttpClientConfigCallback(httpBuilder -> {
                httpBuilder.setHttpProcessor(new HttpProcessor() {
                    @Override
                    public void process(HttpRequest httpRequest, HttpContext httpContext) {
                    }

                    @Override
                    public void process(HttpResponse httpResponse, HttpContext httpContext) {
                    }
                });

                return httpBuilder;
            });

            return new RestHighLevelClient(builder);
        }
    }

}
