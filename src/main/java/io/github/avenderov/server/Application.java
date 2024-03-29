package io.github.avenderov.server;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.plugin.json.JavalinJackson;
import io.swagger.v3.core.jackson.SwaggerModule;
import io.swagger.v3.parser.OpenAPIV3Parser;

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
        app.get("/users/999", this::getUserHandler);
        app.get("/users/1000", this::getUserWithoutEmailHandler);
        app.get("/openapi.yaml", this::getOpenApiSpecHandler);

        app.after(ctx ->
            ctx
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, OPTIONS, HEAD, PUT, POST"));

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

    private void getOpenApiSpecHandler(final Context ctx) throws Exception {
        final var api = new OpenAPIV3Parser().read("openapi.yaml");

        final var mapper =
            new ObjectMapper(new YAMLFactory())
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .registerModule(new SwaggerModule());

        ctx.contentType("text/x-yaml")
            .result(mapper.writeValueAsString(api));
    }
}
