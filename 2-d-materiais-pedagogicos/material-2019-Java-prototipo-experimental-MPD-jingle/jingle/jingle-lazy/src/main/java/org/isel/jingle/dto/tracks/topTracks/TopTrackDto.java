package org.isel.jingle.dto.tracks.topTracks;

import com.google.gson.annotations.SerializedName;

public class TopTrackDto {
    private String name;
    private String url;
    private int duration;

    @SerializedName("@attr")
    private TopTrackAttributeDto attribute;


    public TopTrackAttributeDto getAttribute() {
        return attribute;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public int getDuration() {
        return duration;
    }

    public int getRank() {
        return attribute.getRank();
    }
}
