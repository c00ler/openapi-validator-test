package io.github.avenderov.server;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.atlassian.oai.validator.report.ValidationReport;
import io.github.avenderov.validator.model.ApacheHttpRequest;
import io.github.avenderov.validator.model.ApacheHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class GetUserByIdTest {

    private static Application application;

    private static CloseableHttpClient httpClient;

    private final OpenApiInteractionValidator validator =
        OpenApiInteractionValidator.createFor("openapi.yaml").build();

    @BeforeAll
    static void beforeAll() {
        application = new Application(7000);
        application.run();

        httpClient = HttpClients.createDefault();
    }

    @AfterAll
    static void afterAll() throws IOException {
        httpClient.close();
        application.stop();
    }

    @Test
    void shouldGetUserById() throws IOException {
        final var request = new HttpGet("http://localhost:7000/users/1");
        try (final var response = httpClient.execute(request)) {
            final var validationResult =
                validator.validate(ApacheHttpRequest.of(request), ApacheHttpResponse.of(response));

            assertThat(validationResult.hasErrors()).isFalse();
            assertThat(response.getStatusLine().getStatusCode()).isEqualTo(200);
        }
    }

    @Test
    void shouldThrowIfNoEmailInTheResponse() throws IOException {
        final var request = new HttpGet("http://localhost:7000/users/2");
        try (final var response = httpClient.execute(request)) {
            final var validationResult =
                validator.validate(ApacheHttpRequest.of(request), ApacheHttpResponse.of(response));

            assertThat(validationResult.getMessages().stream().map(ValidationReport.Message::getMessage))
                .containsOnly("Object has missing required properties ([\"email\"])");
        }
    }
}
