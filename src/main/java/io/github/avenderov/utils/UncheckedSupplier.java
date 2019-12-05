package io.github.avenderov.utils;

import java.io.IOException;
import java.io.UncheckedIOException;

public interface UncheckedSupplier<T> {

    T get() throws IOException;

    static <T> T get(final UncheckedSupplier<T> delegate) {
        try {
            return delegate.get();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
