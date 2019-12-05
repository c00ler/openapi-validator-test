package io.github.avenderov.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.plugin.json.JavalinJackson;

public final class Application {

    private final ObjectMapper mapper = new ObjectMapper();
    private final int port;
    private final Javalin app;

    Application(int port) {
        JavalinJackson.configure(mapper);

        this.port = port;
        this.app = Javalin.create();
    }

    public static void main(String[] args) {
        new Application(7000).run();
    }

    void run() {
        app.get("/users/1", this::getUserHandler);
        app.get("/users/2", this::getUserWithoutEmailHandler);
        app.start(port);
    }

    void stop() {
        app.stop();
    }

    private void getUserHandler(final Context ctx) {
        ctx.json(
            mapper.createObjectNode()
                .put("firstName", "John")
                .put("lastName", "Doe")
                .put("email", "john.doe@mail.com"));
    }

    private void getUserWithoutEmailHandler(final Context ctx) {
        ctx.json(
            mapper.createObjectNode()
                .put("firstName", "John")
                .put("lastName", "Doe"));
    }
}
