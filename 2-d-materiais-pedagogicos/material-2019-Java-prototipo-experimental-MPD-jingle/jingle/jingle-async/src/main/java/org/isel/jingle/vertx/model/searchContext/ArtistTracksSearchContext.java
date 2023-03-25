package org.isel.jingle.vertx.model.searchContext;

public class ArtistTracksSearchContext {
    private final String mbid;
    private final int count;

    public ArtistTracksSearchContext(String mbid, int count) {
        this.mbid = mbid;
        this.count = count;
    }

    public String getMbid() {
        return mbid;
    }

    public int getCount() {
        return count;
    }
}
