package org.isel.jingle.vertx;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class TemplateHolder {
    //When user search gives no results
    static final String NO_SEARCH_RESULT = "emptySearch";
    //Homepage
    static final String HOMEPAGE_TEMPLATE = "homepage";
    //Web page title
    static final String JINGLE_TITLE = "jingleTitle";
    //Search artists
    static final String SEARCH_ARTISTS_HEADER = "searchArtistsHeader";
    static final String SEARCH_ARTISTS_BODY = "searchArtistsBody";
    //Artist albums
    static final String ARTIST_ALBUMS_HEADER = "artistAlbumsHeader";
    static final String ARTIST_ALBUMS_BODY = "artistAlbumsBody";
    //Artist tracks
    static final String ARTIST_TRACKS_HEADER = "artistTracksHeader";
    static final String ARTIST_TRACKS_BODY = "artistTrackBody";
    //Album tracks
    static final String ALBUM_TRACKS_HEADER = "albumTracksHeader";
    static final String ALBUM_TRACKS_BODY = "albumTracksBody";

    private final Handlebars handlebars = new Handlebars();
    private final Map<String, Template> templateMap = new HashMap<>();

    public boolean initializeTemplates() {
        try {
            fetchTemplateFor(NO_SEARCH_RESULT);

            fetchTemplateFor(HOMEPAGE_TEMPLATE);

            fetchTemplateFor(JINGLE_TITLE);

            fetchTemplateFor(SEARCH_ARTISTS_HEADER);
            fetchTemplateFor(SEARCH_ARTISTS_BODY);

            fetchTemplateFor(ARTIST_ALBUMS_HEADER);
            fetchTemplateFor(ARTIST_ALBUMS_BODY);

            fetchTemplateFor(ARTIST_TRACKS_HEADER);
            fetchTemplateFor(ARTIST_TRACKS_BODY);

            fetchTemplateFor(ALBUM_TRACKS_HEADER);
            fetchTemplateFor(ALBUM_TRACKS_BODY);

        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public Template getTemplate(String template) {
        return templateMap.get(template);
    }


    private void fetchTemplateFor(String templateLocation) throws IOException {
        System.out.println("Fetching template: " + templateLocation);
        templateMap.put(templateLocation, handlebars.compile(templateLocation));
    }


}
