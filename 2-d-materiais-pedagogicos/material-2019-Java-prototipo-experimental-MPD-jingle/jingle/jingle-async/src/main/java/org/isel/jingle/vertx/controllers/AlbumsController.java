 package org.isel.jingle.vertx.controllers;

import io.reactivex.Observable;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.isel.jingle.JingleService;
import org.isel.jingle.model.Track;
import org.isel.jingle.parsers.HttpRequestParser;
import org.isel.jingle.vertx.model.searchContext.AlbumTracksSearchContext;
import org.isel.jingle.vertx.model.searchContext.ViewContext;
import org.isel.jingle.vertx.view.View;

public class AlbumsController extends BaseController {
    private static final String BASE_PATH = "/albums";
    private static final String ALBUM_MBID_PARAM = "/:id";
    private static final long MAX_COUNT = 100;

    private final View<Observable<Track>, AlbumTracksSearchContext> albumTracksView;

    private final HttpRequestParser httpRequestParser = new HttpRequestParser();

    public AlbumsController(Router router, JingleService service, View<Observable<Track>, AlbumTracksSearchContext> albumTracksView) {
        super(router, service);
        this.albumTracksView = albumTracksView;
    }


    @Override
    protected void addRoutes(Router router) {
        router.route(BASE_PATH + ALBUM_MBID_PARAM + "/tracks").handler(this::queryAlbumTracks);
    }

    //Path:     ../albums/:id/tracks
    private void queryAlbumTracks(RoutingContext ctx) {
        HttpServerRequest request = ctx.request();

        String id = request.getParam("id");
        String mbid = httpRequestParser.removeWhitespaces(id);

        Observable<Track> albumTracks = service.getAlbumTracks(mbid).take(MAX_COUNT);

        albumTracksView.write(ctx.response(), new ViewContext<>(albumTracks, new AlbumTracksSearchContext(mbid)));
    }
}
