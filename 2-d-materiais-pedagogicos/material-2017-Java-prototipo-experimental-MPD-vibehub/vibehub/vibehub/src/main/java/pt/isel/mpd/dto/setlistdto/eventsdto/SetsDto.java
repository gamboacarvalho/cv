package pt.isel.mpd.dto.setlistdto.eventsdto;

import com.google.gson.JsonObject;
import pt.isel.mpd.dto.DtoUtils;

public class SetsDto {

    public final SetDto[] set;

    public SetsDto(SetDto[] set) {
        this.set = set;
    }

    public SetDto[] getSet() {
        return set;
    }

    public static SetsDto fromJson(JsonObject obj) {
        if(obj == null)
            return new SetsDto(new SetDto[0]);
        SetDto[] set = DtoUtils.arrayFromJson(obj, "set", SetDto::fromJson, SetDto.class);
        return new SetsDto(set);
    }
}
