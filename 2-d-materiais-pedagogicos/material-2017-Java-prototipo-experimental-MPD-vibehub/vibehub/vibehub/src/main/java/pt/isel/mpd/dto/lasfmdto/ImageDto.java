package pt.isel.mpd.dto.lasfmdto;

import com.google.gson.JsonObject;
import pt.isel.mpd.dto.DtoUtils;

public class ImageDto {

    public final String text;

    public ImageDto(String text) {
        this.text = text;
    }

    public static ImageDto fromJson(JsonObject obj) {
        return new ImageDto(DtoUtils.stringFromJson(obj, "#text"));
    }
}
