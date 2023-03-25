import pt.isel.mpd.Artist;
import pt.isel.mpd.Event;
import pt.isel.mpd.Track;
import pt.isel.mpd.Venue;
import pt.isel.mpd.services.VibeService;
import pt.isel.mpd.services.VibeServiceCache;
import pt.isel.mpd.util.HttpRequest;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class App {

    public static void main(String[] args) {
        VibeService service = new VibeServiceCache(new HttpRequest());

        Stream<Event> events = service.getEvents("33d4a8c9").get().limit(5);
        Stream<Venue> venues = service.searchVenues("mafra").get().limit(5);
        CompletableFuture<Artist> artist = service.getArtist("9c9f1380-2516-4fc9-a3e6-f9f61941d090");
        CompletableFuture<Track> track = service.getTrack("Muse","Uprising");

        events.forEach(System.out::println);

        System.out.println("\n\n\n NEXT ROUND \n\n\n");

        venues.forEach(System.out::println);

        System.out.println("\n\n\n NEXT ROUND \n\n\n");

        System.out.println(artist.join());

        System.out.println("\n\n\n NEXT ROUND \n\n\n");

        System.out.println(track.join());
    }
}

