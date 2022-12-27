package be.sgerard.kafka.configuration;

import be.sgerard.kafka.utils.UnsafeX509ExtendedTrustManager;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestClientBuilder;
import org.opensearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

@Configuration
public class OpensearchConfiguration {

    private final OpensearchProperties properties;

    public OpensearchConfiguration(OpensearchProperties properties) {
        this.properties = properties;
    }

    @Bean(destroyMethod = "close")
    RestHighLevelClient opensearchClient() {
        final RestClientBuilder builder = RestClient.builder(new HttpHost(properties.getHostname(), properties.getPort(), properties.getScheme()));

        builder.setHttpClientConfigCallback(this::setupClient);

        return new RestHighLevelClient(builder);
    }

    private HttpAsyncClientBuilder setupClient(HttpAsyncClientBuilder httpClientBuilder) {
        if ((properties.getUsername() != null) && (properties.getPassword() != null)) {
            final BasicCredentialsProvider provider = new BasicCredentialsProvider();

            provider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(properties.getUsername(), properties.getPassword()));

            httpClientBuilder.setDefaultCredentialsProvider(provider);
        }

        if (properties.isInsecure()) {
            final SSLContext sslContext = setupSslContext();

            httpClientBuilder
                    .setSSLContext(sslContext)
                    .setSSLHostnameVerifier((host, session) -> true);
        }

        return httpClientBuilder
                .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());
    }

    private SSLContext setupSslContext() {
        try {
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{UnsafeX509ExtendedTrustManager.INSTANCE}, null);

            return sslContext;
        } catch (Exception e) {
            throw new RuntimeException("Error while initializing SSL context.", e);
        }
    }
}