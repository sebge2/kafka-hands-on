package be.sgerard.kafka.client;

import be.sgerard.kafka.model.GitHubSourceConnectorConfig;
import be.sgerard.kafka.model.GitHubAccessLimit;
import be.sgerard.kafka.model.GitHubIssueResponse;
import be.sgerard.kafka.model.external.IssueDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.errors.RetriableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.List;

public class GitHubClient {

    private static final Logger log = LoggerFactory.getLogger(GitHubClient.class);

    private final GitHubSourceConnectorConfig config;
    private final ObjectMapper objectMapper;

    public GitHubClient(GitHubSourceConnectorConfig config) {
        this.config = config;
        this.objectMapper = new ObjectMapper().findAndRegisterModules();
    }

    /**
     * Returns all {@link GitHubIssueResponse issues} created after the specified date and for the specified page.
     */
    @SuppressWarnings("DuplicateThrows")
    public GitHubIssueResponse getIssues(Integer page, Instant since) throws TooManyRequestsException, RetriableException, ConnectException {
        final String url = config.buildGitHubIssuesUrl(page, since);
        final HttpResponse<InputStream> response = callApi(url);
        final GitHubAccessLimit limit = GitHubAccessLimit.fromHeaders(response.getHeaders());

        switch (response.getStatus()) {
            case 200:
                return new GitHubIssueResponse(mapIssues(response.getBody()), limit);
            case 401:
                throw new ConnectException("Bad GitHub credentials provided, please edit your config.");
            case 403:
                throw new TooManyRequestsException(limit);
            default:
                log.error(
                        "Got unexpected answer response status " + response.getStatus() + " from GitHub with body [" +
                                response.getBody() + "] and headers [" + response.getHeaders() + "] url [" + url + "]."
                );

                throw new RetriableException("Got unexpected answer response status " + response.getStatus() + " from GitHub.");
        }
    }

    /**
     * Calls the specified URL and returns the stream.
     */
    private HttpResponse<InputStream> callApi(String url) {
        try {
            GetRequest unirest = Unirest.get(url);

            if ((config.getAuthenticationUsername() != null) && (config.getAuthenticationUsername() != null)) {
                unirest = unirest.basicAuth(config.getAuthenticationUsername(), config.getAuthenticationPassword());
            }

            return unirest.asBinary();
        } catch (UnirestException e) {
            throw new RetriableException("Error while calling GitHub on [" + url + "].", e);
        }
    }

    /**
     * Maps issues from the specified stream that contains JSON content.
     */
    private List<IssueDto> mapIssues(InputStream stream) {
        try {
            return objectMapper.readValue(stream, new TypeReference<List<IssueDto>>() {
            });
        } catch (IOException e) {
            throw new RuntimeException("Error while reading issues from JSON content.", e);
        }
    }
}