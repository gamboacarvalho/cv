package org.isel.jingle.dto.artists;


import com.google.gson.annotations.SerializedName;

public class ArtistSearchResultDto {
    @SerializedName("opensearch:itemsPerPage")
    private String itemsPerPage;
    private ArtistsContainerDto artistmatches;

    public String getItemsPerPage() {
        return itemsPerPage;
    }

    public ArtistsContainerDto getArtistContainer() {
        return artistmatches;
    }
}
