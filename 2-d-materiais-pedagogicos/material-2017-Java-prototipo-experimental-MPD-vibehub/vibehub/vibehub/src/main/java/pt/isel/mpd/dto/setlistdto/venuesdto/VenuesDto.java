package pt.isel.mpd.dto.setlistdto.venuesdto;

import com.google.gson.JsonObject;
import pt.isel.mpd.dto.DtoUtils;
import pt.isel.mpd.dto.setlistdto.PageDto;

public class VenuesDto extends PageDto {

    private final VenueDto[] venue;

    public VenueDto[] getVenue() {
        return venue;
    }

    public VenuesDto(int itemsPerPage, int total, VenueDto[] venue) {
        super(itemsPerPage, total);
        this.venue = venue;
    }

    public static VenuesDto fromJson(JsonObject obj) {
        if(obj == null)
            return new VenuesDto(0, 0, new VenueDto[0]);
        int itemsPerPage = DtoUtils.intFromJson(obj, "@itemsPerPage");
        int total = DtoUtils.intFromJson(obj, "@total");
        VenueDto[] venues = DtoUtils.arrayFromJson(obj, "venue", VenueDto::fromJson, VenueDto.class);
        return new VenuesDto(itemsPerPage, total, venues);
    }
}
