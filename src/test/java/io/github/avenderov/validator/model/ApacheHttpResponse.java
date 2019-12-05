package io.github.avenderov.validator.model;

import com.atlassian.oai.validator.model.Response;
import com.atlassian.oai.validator.model.SimpleResponse;
import io.github.avenderov.utils.UncheckedSupplier;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;

public final class ApacheHttpResponse {

    private ApacheHttpResponse() {
    }

    @Nonnull
    public static Response of(final HttpResponse response) {
        final var entity = response.getEntity();
        final var responseBody = UncheckedSupplier.get(() -> EntityUtils.toString(entity, StandardCharsets.UTF_8));

        final var builder = new SimpleResponse.Builder(response.getStatusLine().getStatusCode()).withBody(responseBody);
        for (final var header : response.getAllHeaders()) {
            builder.withHeader(header.getName(), header.getValue());
        }

        return builder.build();
    }
}
