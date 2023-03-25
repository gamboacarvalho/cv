package org.isel.jingle.vertx.controllers;

import io.reactivex.Observable;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.isel.jingle.JingleService;
import org.isel.jingle.model.Album;
import org.isel.jingle.model.Artist;
import org.isel.jingle.model.Track;
import org.isel.jingle.parsers.HttpRequestParser;
import org.isel.jingle.vertx.model.searchContext.ArtistAlbumSearchContext;
import org.isel.jingle.vertx.model.searchContext.ArtistSearchContext;
import org.isel.jingle.vertx.model.searchContext.ArtistTracksSearchContext;
import org.isel.jingle.vertx.model.searchContext.ViewContext;
import org.isel.jingle.vertx.view.View;

public class ArtistController extends BaseController<Observable<Artist>> {
    private static final String BASE_PATH = "/artists";
    private static final String ARTIST_QUERY_PARAM = "name";
    private static final String ARTIST_MBID_PARAM = "/:id";
    private static final long MAX_COUNT = 500;

    private final HttpRequestParser httpRequestParser = new HttpRequestParser();

    private final View<Observable<Artist>, ArtistSearchContext> artistsView;
    private final View<Observable<Album>, ArtistAlbumSearchContext> artistAlbumsView;
    private final View<Observable<Track>, ArtistTracksSearchContext> artistTracksView;


    public ArtistController(Router router, JingleService service,
                            View<Observable<Artist>, ArtistSearchContext> artistsView,
                            View<Observable<Album>, ArtistAlbumSearchContext> artistAlbumsView,
                            View<Observable<Track>, ArtistTracksSearchContext> artistTracksView) {

        super(router, service);
        this.artistsView = artistsView;
        this.artistAlbumsView = artistAlbumsView;
        this.artistTracksView = artistTracksView;
    }


    @Override
    protected void addRoutes(Router router) {
        router.route(BASE_PATH).handler(this::queryArtist);
        router.route(BASE_PATH + ARTIST_MBID_PARAM + "/albums").handler(this::queryArtistAlbums);
        router.route(BASE_PATH + ARTIST_MBID_PARAM + "/tracks").handler(this::queryArtistTracks);
    }

    //Path:     ../artists?name=
    private void queryArtist(RoutingContext ctx) {
        HttpServerRequest request = ctx.request();

        String requestParam = request.getParam(ARTIST_QUERY_PARAM);
        String artist = httpRequestParser.removeWhitespaces(requestParam);

        Observable<Artist> artists = service.searchArtist(artist).take(MAX_COUNT);

        artistsView.write(ctx.response(), new ViewContext<>(artists, new ArtistSearchContext(requestParam, (int) MAX_COUNT)));
    }

    //Path:     ../artists/:id/albums
    private void queryArtistAlbums(RoutingContext ctx) {
        HttpServerRequest request = ctx.request();

        String id = request.getParam("id");
        String mbid = httpRequestParser.removeWhitespaces(id);

        Observable<Album> albums = service.getAlbums(mbid).take(MAX_COUNT);

        artistAlbumsView.write(ctx.response(), new ViewContext<>(albums, new ArtistAlbumSearchContext(id, (int) MAX_COUNT)));
    }

    //Path:     ../artists/:id/tracks
    private void queryArtistTracks(RoutingContext ctx) {
        HttpServerRequest request = ctx.request();

        String id = request.getParam("id");
        String mbid = httpRequestParser.removeWhitespaces(id);

        Observable<Track> artistTracks = service.getArtistTracks(mbid).take(MAX_COUNT);

        artistTracksView.write(ctx.response(), new ViewContext<>(artistTracks, new ArtistTracksSearchContext(id, (int) MAX_COUNT)));
    }
}
