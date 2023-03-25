package pt.isel.mpd;

import java.util.function.Supplier;
import java.util.stream.Stream;

import static pt.isel.mpd.util.StringUtils.join;

public class Venue {

    private final String id;
    private final String name;
    private final Supplier<Stream<Event>> events;

    public String getId() { return id; }
    public String getName() { return name; }
    public Stream<Event> getEvents() { return events.get(); }

    public Venue(String id, String name, Supplier<Stream<Event>> events) {
        this.id = id;
        this.name = name;
        this.events = events;
    }

    @Override
    public String toString() {
        return "Venue{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", events=" + join(getEvents()) +
                '}';
    }

}
