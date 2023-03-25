package pt.isel.mpd.services;

import pt.isel.mpd.Artist;
import pt.isel.mpd.Event;
import pt.isel.mpd.Track;
import pt.isel.mpd.Venue;
import pt.isel.mpd.dto.lasfmdto.ImageDto;
import pt.isel.mpd.dto.setlistdto.PageDto;
import pt.isel.mpd.dto.setlistdto.eventsdto.EventDto;
import pt.isel.mpd.dto.setlistdto.eventsdto.SetDto;
import pt.isel.mpd.dto.setlistdto.eventsdto.SongDto;
import pt.isel.mpd.dto.setlistdto.venuesdto.VenueDto;
import pt.isel.mpd.util.IRequest;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class VibeService {

    protected final SetListApi set;
    protected final LastFmApi last;

    public VibeService(SetListApi set, LastFmApi last){
        this.set = set;
        this.last = last;
    }

    public VibeService(IRequest req){
        this.set = new SetListApi(req);
        this.last = new LastFmApi(req);
    }

    public Supplier<Stream<Venue>> searchVenues(String city){
        CompletableFuture<Integer> pages = set
                .getVenue(city)
                .thenApply(this::numberOfPages);

        return () -> IntStream.rangeClosed(1, pages.join())
                .mapToObj(p -> set.getVenues(city,p).join())
                .flatMap(Arrays::stream)
                .map(this::dtoToVenue);
    }

    public Supplier<Stream<Event>> getEvents(String venue){
        CompletableFuture<Integer> pages = set
                .getEvents(venue)
                .thenApply(this::numberOfPages);

        return () -> IntStream.rangeClosed(1, pages.join())
                .mapToObj(p -> set.getEvents(venue,p).join())
                .flatMap(Arrays::stream)
                .map(this::dtoToEvent);
    }

    protected int numberOfPages(PageDto dto) {
        return dto.getTotal() / dto.getItemsPerPage() +
                (dto.getTotal() % dto.getItemsPerPage() > 0 ? 1 : 0);
    }

    public CompletableFuture<Event> getEvent(String id){
        return set
                .getEvent(id)
                .thenApply(this::dtoToEvent);
    }

    public CompletableFuture<Artist> getArtist(String mbid){
        return last
                .getArtistInfo(mbid)
                .thenApply(artistInfo ->
                        new Artist(artistInfo.getName(),
                                artistInfo.getBio().content,
                                artistInfo.getUrl(),
                                imageDtoToStringArray(artistInfo.getImage()),
                                artistInfo.getMbid())
                );
    }

    public CompletableFuture<Track> getTrack(String artist, String track){
        return last
                .getTrackInfo(artist, track)
                .thenApply(trackInfo ->
                        new Track(trackInfo.getName(),
                                trackInfo.getArtist().getName(),
                                trackInfo.getAlbum().getTitle(),
                                trackInfo.getTrackUrl(),
                                imageDtoToStringArray(trackInfo.getAlbum().getImage()),
                                trackInfo.getAlbum().getUrl(),
                                trackInfo.getDuration())
                );
    }


    protected Venue dtoToVenue(VenueDto dto) {
        return new Venue(dto.getId(), dto.getName(), getEvents(dto.getId()));
    }

    protected Event dtoToEvent(EventDto dto) {
        String[] trackNames = dtoToString(dto);
        return new Event(getArtist(dto.getArtist().getMbid()),
                         dto.getEventDate(),
                         dto.getTour(),
                         trackNames,
                         getTracks(dto.getArtist().getName(), trackNames),
                         dto.getId());
    }

    private CompletableFuture<Track>[] getTracks(String artist, String[] trackNames) {
        return Arrays
                .stream(trackNames)
                .map(t -> getTrack(artist, t))
                .toArray(CompletableFuture[]::new);
    }

    private static String[] dtoToString(EventDto dto) {
        SetDto[] set = dto.getSets().set;
        if(set.length == 0)
            return new String[0];
        SongDto[] so = set[0].getSong();
        String[] trackNames = new String[so.length];
        for (int i = 0; i < trackNames.length; i++)
            trackNames[i] = so[i].getName();
        return trackNames;
    }
    protected static String[] imageDtoToStringArray(ImageDto[] image){
        String[] ret = new String[image.length];
        for (int i = 0; i < image.length; i++)
            ret[i] = image[i].text;
        return ret;
    }
}