package io.github.avenderov.server;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.atlassian.oai.validator.report.ValidationReport;
import io.github.avenderov.utils.PortUtils;
import io.github.avenderov.validator.adapter.ApacheHttpRequest;
import io.github.avenderov.validator.adapter.ApacheHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class GetUserByIdTest {

    private static final int PORT = PortUtils.randomFreePort();

    private static Application application;

    private static CloseableHttpClient httpClient;

    private final OpenApiInteractionValidator validator =
        OpenApiInteractionValidator.createFor("openapi.yaml").build();

    @BeforeAll
    static void beforeAll() {
        application = new Application(PORT);
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
        final var request = getUserWithId(999);
        try (final var response = httpClient.execute(request)) {
            final var validationResult =
                validator.validate(new ApacheHttpRequest(request), new ApacheHttpResponse(response));

            assertThat(validationResult.hasErrors()).isFalse();
            assertThat(response.getStatusLine().getStatusCode()).isEqualTo(200);
        }
    }

    @Test
    void shouldThrowIfNoEmailInTheResponse() throws IOException {
        final var request = getUserWithId(1000);
        try (final var response = httpClient.execute(request)) {
            final var validationResult =
                validator.validate(new ApacheHttpRequest(request), new ApacheHttpResponse(response));

            assertThat(validationResult.getMessages().stream().map(ValidationReport.Message::getMessage))
                .containsOnly("Object has missing required properties ([\"email\"])");
        }
    }

    @Test
    void shouldThrowIfUserIdIsNotInt() throws IOException {
        final var request = getUserWithId("test");
        try (final var response = httpClient.execute(request)) {
            final var validationResult =
                validator.validate(new ApacheHttpRequest(request), new ApacheHttpResponse(response));

            assertThat(validationResult.getMessages().stream().map(ValidationReport.Message::getMessage))
                .contains("Instance type (string) does not match any allowed primitive type (allowed: [\"integer\"])");
        }
    }

    private static HttpUriRequest getUserWithId(final int id) {
        return getUserWithId(Integer.toString(id));
    }

    private static HttpUriRequest getUserWithId(final String id) {
        return new HttpGet(String.format("http://localhost:%d/users/%s", PORT, id));
    }
}
