package pt.isel.mpd.dto.setlistdto.venuesdto;

import com.google.gson.JsonObject;
import pt.isel.mpd.dto.DtoUtils;

public class VenueDto {

    private final String id;
    private final String name;

    public VenueDto(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public static VenueDto fromJson(JsonObject obj) {
        if(obj == null)
            return new VenueDto("", "");
        String id = DtoUtils.stringFromJson(obj, "@id");
        String name = DtoUtils.stringFromJson(obj, "@name");
        return new VenueDto(id, name);
    }
}
