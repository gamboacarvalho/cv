package pt.isel.mpd.services;

import pt.isel.mpd.dto.setlistdto.eventsdto.EventDto;
import pt.isel.mpd.dto.setlistdto.eventsdto.EventsDto;
import pt.isel.mpd.dto.setlistdto.venuesdto.VenueDto;
import pt.isel.mpd.dto.setlistdto.venuesdto.VenuesDto;
import pt.isel.mpd.util.IRequest;

import java.util.concurrent.CompletableFuture;

public class SetListApi extends Api {

    //private static final String KEY = "1787da51-798d-4d13-b4ac-c215899c8e1f";
    private static final String HOST = "https://api.setlist.fm";
    private static final String VENUES = "/rest/0.1/search/venues.json?cityName=%s&p=%d";
    private static final String EVENTS = "/rest/0.1/venue/%s/setlists.json?p=%d";
    private static final String EVENT = "/rest/0.1/setlist/%s.json";
    private static final String VENUES_FIELD = "venues";
    private static final String EVENTS_FIELD = "setlists";
    private static final String EVENT_FIELD = "setlist";

    public SetListApi(IRequest req){
        super(req);
    }



    public CompletableFuture<VenuesDto> getVenue(String city) {
        return get(HOST, VENUES, "", VENUES_FIELD, VenuesDto::fromJson, city, 1);
    }
/*
    public CompletableFuture<VenueDto[]> getVenues(String city) {
        return get(HOST, VENUES, "", VENUES_FIELD, obj -> VenuesDto.fromJson(obj).getVenue(), city, 1);
    }
*/
    public CompletableFuture<VenueDto[]> getVenues(String city, int n) {
        return get(HOST, VENUES, "", VENUES_FIELD, obj -> VenuesDto.fromJson(obj).getVenue(), city, n);
    }



    public CompletableFuture<EventDto> getEvent(String venue) {
        return get(HOST, EVENT, "", EVENT_FIELD, EventDto::fromJson, venue);
    }

    public CompletableFuture<EventsDto> getEvents(String venue) {
        return get(HOST, EVENTS, "", EVENTS_FIELD, EventsDto::fromJson, venue, 1);
    }

    public CompletableFuture<EventDto[]> getEvents(String venue, int n) {
        return get(HOST, EVENTS, "", EVENTS_FIELD, obj -> EventsDto.fromJson(obj).getSetlist(), venue, n);
    }
}
