package io.github.avenderov.utils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.ServerSocket;

public final class PortUtils {

    private PortUtils() {
    }

    public static int randomFreePort() {
        try (final var socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
