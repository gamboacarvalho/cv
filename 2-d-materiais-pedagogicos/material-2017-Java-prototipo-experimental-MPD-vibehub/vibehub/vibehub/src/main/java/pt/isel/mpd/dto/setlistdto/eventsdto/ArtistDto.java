package pt.isel.mpd.dto.setlistdto.eventsdto;

import com.google.gson.JsonObject;
import pt.isel.mpd.dto.DtoUtils;

public class ArtistDto {

    private final String mbid;
    private final String name;
    private final String url;

    public ArtistDto(String mbid, String name, String url) {
        this.mbid = mbid;
        this.name = name;
        this.url = url;
    }

    public String getMbid() {
        return mbid;
    }
    public String getName() {
        return name;
    }
    public String getUrl() {
        return url;
    }

    public static ArtistDto fromJson(JsonObject obj) {
        if(obj == null)
            return new ArtistDto("", "", "");
        String mbid = DtoUtils.stringFromJson(obj, "@mbid");
        String name = DtoUtils.stringFromJson(obj, "@name");
        String url = DtoUtils.stringFromJson(obj, "url");
        return new ArtistDto(mbid, name, url);
    }
}
