package pt.isel.mpd.dto.setlistdto.eventsdto;

import com.google.gson.JsonObject;
import pt.isel.mpd.dto.DtoUtils;

public class SongDto {

    public final String name;

    public SongDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static SongDto fromJson(JsonObject obj) {
        return new SongDto(DtoUtils.stringFromJson(obj,"@name"));
    }
}
