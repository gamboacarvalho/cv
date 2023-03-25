package pt.isel.mpd.dto.setlistdto.eventsdto;

import com.google.gson.JsonObject;
import pt.isel.mpd.dto.DtoUtils;
import pt.isel.mpd.dto.setlistdto.PageDto;

public class EventsDto extends PageDto {

    private final EventDto[] setlist;

    public EventDto[] getSetlist() {
        return setlist;
    }

    public EventsDto(int itemsPerPage, int total, EventDto[] setlist) {
        super(itemsPerPage, total);
        this.setlist = setlist;
    }

    public static EventsDto fromJson(JsonObject obj){
        if(obj == null)
            return new EventsDto(0, 0, new EventDto[0]);
        int itemsPerPage = DtoUtils.intFromJson(obj, "@itemsPerPage");
        int total = DtoUtils.intFromJson(obj, "@total");
        EventDto[] events = DtoUtils.arrayFromJson(obj, "setlist", EventDto::fromJson, EventDto.class);
        return new EventsDto(itemsPerPage, total, events);
    }

}
