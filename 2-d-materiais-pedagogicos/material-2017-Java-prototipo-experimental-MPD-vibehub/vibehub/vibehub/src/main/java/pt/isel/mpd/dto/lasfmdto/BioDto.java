package pt.isel.mpd.dto.lasfmdto;

import com.google.gson.JsonObject;
import pt.isel.mpd.dto.DtoUtils;

public class BioDto {

    public final String content;

    public String getContent() {
        return content;
    }

    public BioDto(String content) {
        this.content = content;
    }

    public static BioDto fromJson(JsonObject obj) {
        return new BioDto(DtoUtils.stringFromJson(obj, "content"));
    }
}
