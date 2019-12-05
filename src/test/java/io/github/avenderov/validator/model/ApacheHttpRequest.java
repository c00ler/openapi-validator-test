package io.github.avenderov.validator.model;

import com.atlassian.oai.validator.model.Request;
import com.atlassian.oai.validator.model.SimpleRequest;
import org.apache.http.client.methods.HttpUriRequest;

import javax.annotation.Nonnull;

public final class ApacheHttpRequest {

    private ApacheHttpRequest() {
    }

    @Nonnull
    public static Request of(final HttpUriRequest request) {
        final var builder = new SimpleRequest.Builder(request.getMethod(), request.getURI().getPath());

        for (final var header : request.getAllHeaders()) {
            builder.withHeader(header.getName(), header.getValue());
        }

        // TODO: support query parameters

        return builder.build();
    }
}
