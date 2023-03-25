package pt.isel.mpd.dto.setlistdto.eventsdto;

import com.google.gson.JsonObject;
import pt.isel.mpd.dto.DtoUtils;

public class EventDto {

    private final String eventDate;
    private final String id;
    private final String tour;
    private final ArtistDto artist;
    private final SetsDto sets;

    public String getEventDate() {
        return eventDate;
    }
    public String getId() {
        return id;
    }
    public String getTour() {
        return tour;
    }
    public ArtistDto getArtist() {
        return artist;
    }
    public SetsDto getSets() {
        return sets;
    }

    public EventDto(String eventDate, String id, String tour, ArtistDto artist, SetsDto sets) {
        this.eventDate = eventDate;
        this.id = id;
        this.tour = tour;
        this.artist = artist;
        this.sets = sets;
    }

    public static EventDto fromJson(JsonObject obj) {
        if(obj == null)
            return new EventDto("", "", "",
                                new ArtistDto("", "", ""),
                                new SetsDto(new SetDto[0]));
        String eventDate = DtoUtils.stringFromJson(obj, "@eventDate");
        String id = DtoUtils.stringFromJson(obj, "@id");
        String tour = DtoUtils.stringFromJson(obj, "@tour");
        ArtistDto artist = DtoUtils.objectFromJson(obj, "artist", ArtistDto::fromJson);
        SetsDto sets = DtoUtils.objectFromJson(obj, "sets", SetsDto::fromJson);
        return new EventDto(eventDate, id, tour, artist, sets);
    }
}
