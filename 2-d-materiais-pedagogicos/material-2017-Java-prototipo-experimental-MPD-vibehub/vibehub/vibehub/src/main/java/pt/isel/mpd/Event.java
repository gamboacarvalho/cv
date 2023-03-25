package pt.isel.mpd;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static pt.isel.mpd.util.StringUtils.join;

public class Event {

    private final CompletableFuture<Artist> artist;
    private final String eventDate;
    private final String tour;
    private final String[] tracksName;
    private final CompletableFuture<Track>[] tracks;
    private final String setListId;

    public Artist getArtist() { return artist.join(); }
    public String getArtistName() { return artist.join().getName(); }
    public String getEventDate() { return eventDate; }
    public String getTour() { return tour; }
    public String[] getTracksName() { return tracksName; }
    public Stream<Track> getTracks() { return Arrays.stream(tracks).map(CompletableFuture::join); }

    public String setListId() { return setListId; }

    public Event(CompletableFuture<Artist> artist, String eventDate, String tour,
                 String[] tracksName, CompletableFuture<Track>[] tracks, String setListId) {
        this.artist = artist;
        this.eventDate = eventDate;
        this.tour = tour;
        this.tracksName = tracksName;
        this.tracks = tracks;
        this.setListId = setListId;
    }

    @Override
    public String toString() {
        return "Event{" +
                "artist=" + getArtist().toString() +
                ", eventDate='" + eventDate + '\'' +
                ", tour='" + tour + '\'' +
                ", tracksName=" + Arrays.toString(tracksName) +
                ", tracks=" + join(getTracks()) +
                ", setListId='" + setListId + '\'' +
                '}';
    }
}
