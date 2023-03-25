package org.isel.jingle.model;

public class TopTrack {
    private final String name;
    private final String url;
    private final String country;
    private final int duration;
    private final int rank;

    public TopTrack(String name, String url, String country, int duration, int rank) {
        this.name = name;
        this.url = url;
        this.country = country;
        this.duration = duration;
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getCountry() {
        return country;
    }

    public int getDuration() {
        return duration;
    }

    public int getRank() {
        return rank;
    }
}
