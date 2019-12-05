package io.github.avenderov.server;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class GetUserByIdTest {

    private static Application application;

    private static CloseableHttpClient httpClient;

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
            final var responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

            assertThat(response.getStatusLine().getStatusCode()).isEqualTo(200);
        }
    }
}
