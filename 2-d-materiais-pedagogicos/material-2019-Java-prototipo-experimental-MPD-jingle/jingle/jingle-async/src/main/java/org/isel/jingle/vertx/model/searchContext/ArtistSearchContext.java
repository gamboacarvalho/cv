package org.isel.jingle.vertx.model.searchContext;

public class ArtistSearchContext {
    private final String searchedArtist;
    private final long count;


    public ArtistSearchContext(String searchedArtist, int count) {
        this.searchedArtist = searchedArtist;
        this.count = count;
    }

    public String getSearchedArtist() {
        return searchedArtist;
    }

    public long getCount() {
        return count;
    }
}
