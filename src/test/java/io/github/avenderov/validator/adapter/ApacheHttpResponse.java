package io.github.avenderov.validator.adapter;

import com.atlassian.oai.validator.model.Response;
import com.atlassian.oai.validator.model.SimpleResponse;
import io.github.avenderov.utils.UncheckedSupplier;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Optional;

public final class ApacheHttpResponse implements Response {

    private final Response delegate;

    public ApacheHttpResponse(final HttpResponse response) {
        final var entity = response.getEntity();
        final var responseBody = UncheckedSupplier.get(() -> EntityUtils.toString(entity, StandardCharsets.UTF_8));

        final var builder = new SimpleResponse.Builder(response.getStatusLine().getStatusCode()).withBody(responseBody);
        for (final var header : response.getAllHeaders()) {
            builder.withHeader(header.getName(), header.getValue());
        }

        this.delegate = builder.build();
    }

    @Override
    public int getStatus() {
        return delegate.getStatus();
    }

    @Nonnull
    @Override
    public Optional<String> getBody() {
        return delegate.getBody();
    }

    @Nonnull
    @Override
    public Collection<String> getHeaderValues(String name) {
        return delegate.getHeaderValues(name);
    }
}
