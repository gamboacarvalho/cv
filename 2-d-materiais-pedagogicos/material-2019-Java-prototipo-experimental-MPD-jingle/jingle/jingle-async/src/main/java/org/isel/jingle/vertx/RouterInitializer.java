package org.isel.jingle.vertx;

import com.github.jknack.handlebars.Template;
import io.vertx.ext.web.Router;
import org.isel.jingle.JingleService;
import org.isel.jingle.vertx.controllers.AlbumsController;
import org.isel.jingle.vertx.controllers.ArtistController;
import org.isel.jingle.vertx.controllers.HomepageController;
import org.isel.jingle.vertx.printStream.ResponsePrintStream;
import org.isel.jingle.vertx.view.albums.AlbumTracksTextView;
import org.isel.jingle.vertx.view.artists.ArtistAlbumsTextView;
import org.isel.jingle.vertx.view.artists.ArtistTextView;
import org.isel.jingle.vertx.view.artists.ArtistTracksTextView;
import org.isel.jingle.vertx.view.homepage.HomepageTextView;

public class RouterInitializer {
    private final Router router;
    private final JingleService service;
    private final TemplateHolder holder;
    private final ResponsePrintStream printStream = new ResponsePrintStream();

    public RouterInitializer(Router router, JingleService service, TemplateHolder holder) {
        this.router = router;
        this.service = service;
        this.holder = holder;
    }


    public void registerRoutes() {
        System.out.println("\n\nRegistering routes...");

        Template titleHeader = holder.getTemplate(TemplateHolder.JINGLE_TITLE);
        Template noSearchResult = holder.getTemplate(TemplateHolder.NO_SEARCH_RESULT);

        new HomepageController(router, service,
                new HomepageTextView(printStream, titleHeader, holder.getTemplate(TemplateHolder.HOMEPAGE_TEMPLATE)));

        new ArtistController(router, service,
                new ArtistTextView(printStream, titleHeader, holder.getTemplate(TemplateHolder.SEARCH_ARTISTS_HEADER), holder.getTemplate(TemplateHolder.SEARCH_ARTISTS_BODY), noSearchResult),
                new ArtistAlbumsTextView(printStream, titleHeader, holder.getTemplate(TemplateHolder.ARTIST_ALBUMS_HEADER), holder.getTemplate(TemplateHolder.ARTIST_ALBUMS_BODY), noSearchResult),
                new ArtistTracksTextView(printStream, titleHeader, holder.getTemplate(TemplateHolder.ARTIST_TRACKS_HEADER), holder.getTemplate(TemplateHolder.ARTIST_TRACKS_BODY), noSearchResult)
        );
        new AlbumsController(router, service,
                new AlbumTracksTextView(printStream, titleHeader, holder.getTemplate(TemplateHolder.ALBUM_TRACKS_HEADER), holder.getTemplate(TemplateHolder.ALBUM_TRACKS_BODY), noSearchResult)
        );

        System.out.println("Finished registering!");
    }
}
