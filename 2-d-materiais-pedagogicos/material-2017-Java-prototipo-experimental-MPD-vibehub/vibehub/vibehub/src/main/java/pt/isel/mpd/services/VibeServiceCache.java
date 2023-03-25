package pt.isel.mpd.services;

import pt.isel.mpd.Artist;
import pt.isel.mpd.Event;
import pt.isel.mpd.Track;
import pt.isel.mpd.Venue;
import pt.isel.mpd.dto.setlistdto.eventsdto.EventsDto;
import pt.isel.mpd.dto.setlistdto.venuesdto.VenuesDto;
import pt.isel.mpd.util.IRequest;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class VibeServiceCache extends VibeService {

    private final Map<String, Map<Integer,List<Venue>>> venuesCache = new HashMap<>();
    private final Map<String, Map<Integer,List<Event>>> eventsCache = new HashMap<>();
    private final Map<String, Event> eventsIdCache = new HashMap<>();
    private final Map<String, Artist> artistsCache = new HashMap<>();
    private final Map<String, Map<String, Track>> tracksCache = new HashMap<>();

    public VibeServiceCache(SetListApi set, LastFmApi last) {
        super(set, last);
    }

    public VibeServiceCache(IRequest req) {
        super(req);
    }

    public Supplier<Stream<Venue>> searchVenues(String city) {
        CompletableFuture<VenuesDto> cf = set.getVenue(city);

        return () -> {
            VenuesDto dto = cf.join();
            int pages = numberOfPages(dto);
            Map<Integer,List<Venue>> cache = venuesCache.computeIfAbsent(city, k -> new HashMap<>());
            return IntStream
                    .rangeClosed(1, pages)
                    .mapToObj(p -> {
                        int size = p < pages ? dto.getItemsPerPage() : (dto.getTotal() % dto.getItemsPerPage());
                        List<Venue> venues = cache.computeIfAbsent(p, k -> new ArrayList<>());
                        if(venues.size() == size)
                            return venues.stream();

                        return Arrays.stream(set.getVenues(city, p).join())
                                .map(this::dtoToVenue)
                                .peek(venues::add);
                    }).flatMap(s -> s);
        };
    }

    public Supplier<Stream<Event>> getEvents(String venue) {
        CompletableFuture<EventsDto> cf = set.getEvents(venue);

        // CompletableFuture<Stream<Event>> aux = cf.thenApply(dto -> null );
        // return aux::join;

        return () -> {
            EventsDto dto = cf.join();
            int pages = numberOfPages(dto);
            Map<Integer,List<Event>> cache = eventsCache.computeIfAbsent(venue, k -> new HashMap<>());
            return IntStream
                    .rangeClosed(1, pages)
                    .mapToObj(p -> {
                        int size = p < pages ? dto.getItemsPerPage() : (dto.getTotal() % dto.getItemsPerPage());
                        List<Event> events = cache.computeIfAbsent(p, k -> new ArrayList<>());
                        if(events.size() == size)
                            return events.stream();

                        return Arrays.stream(set.getEvents(venue, p).join())
                                .map(this::dtoToEvent)
                                .peek(events::add)
                                .peek(e -> eventsIdCache.put(e.setListId(),e));
                    }).flatMap(s -> s);
        };
    }

    public CompletableFuture<Event> getEvent(String id){
        if (eventsIdCache.containsKey(id))
            return CompletableFuture.completedFuture(eventsIdCache.get(id));
        return set
                .getEvent(id)
                .thenApply(e -> eventsIdCache.computeIfAbsent(id, k -> dtoToEvent(e)));
    }

    public CompletableFuture<Artist> getArtist(String mbid) {
        if (artistsCache.containsKey(mbid))
            return CompletableFuture.completedFuture(artistsCache.get(mbid));
        return last
                .getArtistInfo(mbid)
                .thenApply(artistInfo ->
                        artistsCache.computeIfAbsent(mbid, k ->
                                new Artist(artistInfo.getName(),
                                        artistInfo.getBio().content,
                                        artistInfo.getUrl(),
                                        imageDtoToStringArray(artistInfo.getImage()),
                                        artistInfo.getMbid()))
                );
    }

    public CompletableFuture<Track> getTrack(String artist, String track) {
        if (tracksCache.containsKey(artist) && tracksCache.get(artist).containsKey(track))
            return CompletableFuture.completedFuture(tracksCache.get(artist).get(track));
        return last
                .getTrackInfo(artist, track)
                .thenApply(trackInfo ->
                        tracksCache
                                .computeIfAbsent(artist, k -> new HashMap<>())
                                .computeIfAbsent(track, k ->
                                        new Track(trackInfo.getName(),
                                                trackInfo.getArtist().getName(),
                                                trackInfo.getAlbum().getTitle(),
                                                trackInfo.getTrackUrl(),
                                                imageDtoToStringArray(trackInfo.getAlbum().getImage()),
                                                trackInfo.getAlbum().getUrl(),
                                                trackInfo.getDuration()))
                );
    }

}