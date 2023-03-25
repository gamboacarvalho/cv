package org.isel.jingle.vertx.model.searchContext;

public class ArtistAlbumSearchContext {
    private final String mbid;
    private final int count;

    public ArtistAlbumSearchContext(String mbid, int count) {
        this.mbid = mbid;
        this.count = count;
    }


    public int getCount() {
        return count;
    }

    public String getMbid() {
        return mbid;
    }
}
