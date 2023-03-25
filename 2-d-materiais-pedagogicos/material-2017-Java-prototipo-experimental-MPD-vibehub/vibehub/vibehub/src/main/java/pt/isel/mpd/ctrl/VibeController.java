/*
 * Copyright (c) 2017, Miguel Gamboa
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package pt.isel.mpd.ctrl;

import pt.isel.mpd.Artist;
import pt.isel.mpd.Event;
import pt.isel.mpd.Track;
import pt.isel.mpd.Venue;
import pt.isel.mpd.services.VibeService;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.lang.ClassLoader.getSystemResource;
import static java.nio.file.Files.lines;
import static java.util.stream.Collectors.joining;

public class VibeController {

    private final String root;

    private final VibeService api;
    private final String searchView = load("views/search.html");
    private final String searchVenuesView = load("views/searchVenues.html");
    private final String searchVenuesRow = load("views/searchVenuesRow.html");
    private final String searchEventsView = load("views/searchEvents.html");
    private final String searchEventsRow = load("views/searchEventsRow.html");
    private final String searchEventView = load("views/searchEvent.html");
    private final String searchArtistView = load("views/searchArtist.html");
    private final String searchTrackView = load("views/searchTrack.html");

    private final ConcurrentHashMap<String, CompletableFuture<String>>
            eventsViewsCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, CompletableFuture<String>>
            eventViewsCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, CompletableFuture<String>>
            artistViewsCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, CompletableFuture<String>>
            trackViewsCache = new ConcurrentHashMap<>();

    public VibeController(VibeService api) {
        this.api = api;
        try {
            this.root = getSystemResource(".").toURI().getPath();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public String getSearch(HttpServletRequest req) {
        return searchView;
    }

    public String searchVenues(HttpServletRequest req) {
        String city = req.getParameter("city");
        String rows = api
                .searchVenues(city).get()
                //.peek(v -> get(eventsViewsCache, this::requestEvents, v.getId()))
                .map(v -> String.format(searchVenuesRow,
                        linkForVenue(v)))
                .collect(joining());
        return String.format(searchVenuesView, rows);
    }

    private static String linkForVenue(Venue v) {
        return String.format("<a href=\"/events?venue=%s\">%s</a>", v.getId(), v.getName());
    }

    public String getEvents(HttpServletRequest req) {
        return get(eventsViewsCache, this::requestEvents, req.getParameter("venue")).join();
    }

    public CompletableFuture<String> requestEvents(String... params) {
        CompletableFuture<Stream<Event>> events = CompletableFuture.supplyAsync(api.getEvents(params[0]));
        events.thenAcceptAsync(past ->
                past.forEach(e ->
                        get(eventViewsCache, this::requestEvent, e.setListId())))
                .exceptionally(ex -> {
                    System.out.println(ex);
                    return null;
                });
        return events.thenApply(past -> past
                        .map(e -> String.format(searchEventsRow,
                                linkForEvent(e),
                                e.getEventDate(),
                                e.getTour()))
                        .collect(joining()))
                .thenApply(rows -> String.format(searchEventsView, rows));
    }

    private static String linkForEvent(Event e) {
        return String.format("<a href=\"/event?id=%s\">%s</a>", e.setListId(), e.setListId());
    }

    public String getEvent(HttpServletRequest req) {
        return get(eventViewsCache, this::requestEvent, req.getParameter("id")).join();
    }

    public CompletableFuture<String> requestEvent(String... params) {
        return api
                .getEvent(params[0])
                .thenApply(e -> String.format(searchEventView,
                                e.setListId(),
                                e.getEventDate(),
                                e.getTour(),
                                linkForArtist(e.getArtist()),
                                e.getTracks().map(VibeController::linkForTrack).collect(joining(","))));
    }

    private static String linkForArtist(Artist a) {
        return String.format("<a href=\"/artist?mbid=%s\">%s</a>", a.getMbid(), a.getName());
    }

    private static String linkForTrack(Track t) {
        return String.format("<a href=\"/track?artist=%s&track=%s\">%s</a>", t.getArtistName(), t.getName(), t.getName());
    }

    public String getArtist(HttpServletRequest req) {
        return get(artistViewsCache, this::requestArtist, req.getParameter("mbid")).join();
    }

    public CompletableFuture<String> requestArtist(String... params) {
        return api
                .getArtist(params[0])
                .thenApply(a -> String.format(searchArtistView,
                        a.getName(),
                        a.getMbid(),
                        linkForUrl(a.getUrl()),
                        linkForUri(a.getImagesUri()),
                        a.getBio()));
    }

    public String getTrack(HttpServletRequest req) {
        return get(trackViewsCache, this::requestTrack, req.getParameter("artist"), req.getParameter("track")).join();
    }

    public CompletableFuture<String> requestTrack(String... params) {
        return api
                .getTrack(params[0], params[1])
                .thenApply(t -> String.format(searchTrackView,
                        t.getName(),
                        t.getArtistName(),
                        t.getAlbumName(),
                        linkForUrl(t.getTrackUrl()),
                        linkForUri(t.getImagesUrl()),
                        linkForUrl(t.getAlbumUrl()),
                        t.getDuration() + " ms"));
    }

    private static String linkForUri(String[] urls) {
        return Arrays.stream(urls)
                .map(VibeController::linkForUrl)
                .collect(joining());
    }

    private static String linkForUrl(String url) {
        return String.format("<p><a href=\"%s\">%s</a></p>", url, url);
    }

    private CompletableFuture<String> get(ConcurrentHashMap<String,CompletableFuture<String>> cache,
                                          Function<String[],CompletableFuture<String>> request,
                                          String... params) {
        String id = Arrays.stream(params).collect(joining());
        CompletableFuture<String> future = cache.get(id);
        if (future == null) {
            String view = loadFile(id); // Search on file system
            if(view == null) { // Not in file system
                future = request.apply(params) // Request to service
                        .thenApply(v -> writeFile(id, v)); // Save on disk
            }
            else { // Already in File System
                future = new CompletableFuture<>();
                future.complete(view);
            }
            cache.putIfAbsent(id, future); // Save on cache
        }
        return future;
    }
    private String writeFile(String filename, String view) {
        String path = root + filename + ".html";
        try (FileWriter fw = new FileWriter(path)) {
            fw.write(view);
            fw.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return view;
    }
    private String loadFile(String filename) {
        File file = new File(root + filename + ".html");
        return !file.exists() ? null : load(file.toURI());
    }
    private static String load(String path) {
        return load(ClassLoader.getSystemResource(path));
    }
    private static String load(URL url) {
        try {
            return load(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    private static String load(URI uri) {
        try {
            Path path = Paths.get(uri);
            return lines(path).collect(joining());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
