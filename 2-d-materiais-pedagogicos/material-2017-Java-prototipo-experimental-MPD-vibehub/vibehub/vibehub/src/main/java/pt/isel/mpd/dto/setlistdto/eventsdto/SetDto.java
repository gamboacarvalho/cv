package pt.isel.mpd.dto.setlistdto.eventsdto;

import com.google.gson.JsonObject;
import pt.isel.mpd.dto.DtoUtils;

public class SetDto {

    private final SongDto[] song;

    public SongDto[] getSong() {
        return song;
    }

    public SetDto(SongDto[] song) {
        this.song = song;
    }

    public static SetDto fromJson(JsonObject obj) {
        if(obj == null)
            return new SetDto(new SongDto[0]);
        SongDto[] songs = DtoUtils.arrayFromJson(obj, "song", SongDto::fromJson, SongDto.class);
        return new SetDto(songs);
    }
}
