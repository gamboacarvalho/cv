package org.isel.jingle.vertx.model.searchContext;

public class AlbumTracksSearchContext {
    private final String mbid;

    public AlbumTracksSearchContext(String mbid) {
        this.mbid = mbid;
    }

    public String getMbid() {
        return mbid;
    }

}
