package io.github.avenderov.validator.adapter;

import com.atlassian.oai.validator.model.Request;
import com.atlassian.oai.validator.model.SimpleRequest;
import org.apache.http.client.methods.HttpUriRequest;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public final class ApacheHttpRequest implements Request {

    private final Request delegate;

    public ApacheHttpRequest(final HttpUriRequest request) {
        final var builder = new SimpleRequest.Builder(request.getMethod(), request.getURI().getPath());

        for (final var header : request.getAllHeaders()) {
            builder.withHeader(header.getName(), header.getValue());
        }

        // TODO: support query parameters

        this.delegate = builder.build();
    }

    @Nonnull
    @Override
    public String getPath() {
        return delegate.getPath();
    }

    @Nonnull
    @Override
    public Method getMethod() {
        return delegate.getMethod();
    }

    @Nonnull
    @Override
    public Optional<String> getBody() {
        return delegate.getBody();
    }

    @Nonnull
    @Override
    public Collection<String> getQueryParameters() {
        return delegate.getQueryParameters();
    }

    @Nonnull
    @Override
    public Collection<String> getQueryParameterValues(String name) {
        return delegate.getQueryParameterValues(name);
    }

    @Nonnull
    @Override
    public Map<String, Collection<String>> getHeaders() {
        return delegate.getHeaders();
    }

    @Nonnull
    @Override
    public Collection<String> getHeaderValues(String name) {
        return delegate.getHeaderValues(name);
    }
}
