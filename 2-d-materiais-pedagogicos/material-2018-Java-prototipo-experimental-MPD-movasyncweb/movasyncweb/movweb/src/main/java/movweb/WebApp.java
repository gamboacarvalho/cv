package movweb;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import routes.MovieRouter;

public class WebApp {
    public static void main(String[] args) throws Exception {

        Vertx vertx = Vertx.vertx();
        Router router = MovieRouter.router(vertx);

        vertx
                .createHttpServer()
                .requestHandler(router::accept)
                .listen(3000);
    }
}
