package org.isel.jingle.vertx;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import org.isel.jingle.JingleService;

public class WebApp {
    private static final int PORT = 3000;

    private static final JingleService service = new JingleService();

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);

        TemplateHolder templateHolder = new TemplateHolder();

        if (!templateHolder.initializeTemplates()) {
            System.out.println("\n \n --Failed to initialize all templates for the server! Server will now shutdown. -- \n \n");
            return;
        }

        new RouterInitializer(router, service, templateHolder).registerRoutes();

        startServer(server, router);
    }

    private static void startServer(HttpServer server, Router router) {
        System.out.println("Starting server up...");

        server.requestHandler(router).listen(PORT);

        System.out.println("Server started up! Connect with: http://localhost:" + PORT);
    }

}
