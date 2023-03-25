package routes;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.templ.HandlebarsTemplateEngine;
import io.vertx.ext.web.templ.TemplateEngine;
import movasync.MovService;
import movasync.MovWebApi;
import util.HttpRequest;

import java.util.stream.Collectors;

public class MovieRouter {
    private final MovService mov;
    private final TemplateEngine engine = HandlebarsTemplateEngine.create();

    public MovieRouter(MovService mov) {
        this.mov = mov;
    }

    public static Router router(Vertx vertx) {
        return router(vertx, new MovService(new MovWebApi(new HttpRequest())));
    }

    public static Router router(Vertx vertx, MovService mov) {
        Router router = Router.router(vertx);
        MovieRouter handlers = new MovieRouter(mov);
        router.route("/movies").handler(handlers::moviesHandler);
        router.route("/movies/:id").handler(handlers::movieHandler);
        router.route("/movies/:id/credits").handler(handlers::movieCreditsHandler);
        router.route("/person/:id").handler(handlers::personHandler);
        router.route("/person/:id/movies").handler(handlers::personMoviesHandler);
        return router;
    }

    private void personMoviesHandler(RoutingContext ctx) {
        HttpServerRequest req = ctx.request();
        HttpServerResponse resp = ctx.response();
        resp.putHeader("content-type", "text/html");
        resp.setChunked(true);
        mov
                .getActorCreditsCast(Integer.parseInt(req.getParam("id")))
                .thenApply(searchItemStream -> searchItemStream.collect(Collectors.toList()))
                .thenAccept(arr -> {
                    ctx.put("movies",arr);
                    engine.render(ctx,"templates", "/movies.hbs", view -> {
                        if(view.succeeded())
                            resp.end(view.result());
                        else
                            ctx.fail(view.cause());
                    });
                });
    }

    private void movieCreditsHandler(RoutingContext ctx) {
        HttpServerRequest req = ctx.request();
        HttpServerResponse resp = ctx.response();
        resp.putHeader("content-type", "text/html");
        resp.setChunked(true);
        mov
                .getMovieCast(Integer.parseInt(req.getParam("id")))
                .thenApply(searchItemStream -> searchItemStream.collect(Collectors.toList()))
                .thenAccept(arr -> {
                    ctx.put("movieCast",arr);
                    engine.render(ctx,"templates", "/movieCast.hbs", view -> {
                        if(view.succeeded())
                            resp.end(view.result());
                        else
                            ctx.fail(view.cause());
                    });
                });
    }

    private void personHandler(RoutingContext ctx) {
        HttpServerRequest req = ctx.request();
        HttpServerResponse resp = ctx.response();
        resp.putHeader("content-type", "text/html");
        mov
                .getActor(Integer.parseInt(req.getParam("id")),"")
                .thenAccept(arr -> {
                    ctx.put("person",arr);
                    engine.render(ctx,"templates", "/person.hbs", view -> {
                        if(view.succeeded())
                            resp.end(view.result());
                        else
                            ctx.fail(view.cause());
                    });
                });
    }

    private void movieHandler(RoutingContext ctx) {
        HttpServerRequest req = ctx.request();
        HttpServerResponse resp = ctx.response();
        resp.putHeader("content-type", "text/html");
        mov
                .getMovie(Integer.parseInt(req.getParam("id")))
                .thenAccept(arr -> {
                    ctx.put("movie",arr);
                    engine.render(ctx,"templates", "/movie.hbs", view -> {
                        if(view.succeeded())
                            resp.end(view.result());
                        else
                            ctx.fail(view.cause());
                    });
                });
    }

    private void moviesHandler(RoutingContext ctx) {
        HttpServerRequest req = ctx.request();
        HttpServerResponse resp = ctx.response();
        resp.putHeader("content-type", "text/html");
        resp.setChunked(true);
        mov
                .search(req.getParam("name"))
                .thenApply(searchItemStream -> searchItemStream.collect(Collectors.toList()))
                .thenAccept(arr -> {
                    ctx.put("movies",arr);
                    engine.render(ctx,"templates", "/movies.hbs", view -> {
                        if(view.succeeded())
                            resp.end(view.result());
                        else
                            ctx.fail(view.cause());
                    });
                });
    }

}
