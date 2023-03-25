package pt.isel.mpd.services;

import pt.isel.mpd.dto.lasfmdto.LfmArtistDto;
import pt.isel.mpd.dto.lasfmdto.LfmTrackDto;
import pt.isel.mpd.util.IRequest;

import java.util.concurrent.CompletableFuture;

public class LastFmApi extends Api {

    private static final String KEY = "44c1896d48375c0f62972f9b9b734c77";
    private static final String HOST = "http://ws.audioscrobbler.com";
    private static final String ARTIST = "/2.0/?method=artist.getinfo&format=json&mbid=%s&api_key=";
    private static final String TRACK = "/2.0/?track=%s&artist=%s&method=track.getinfo&format=json&api_key=";
    private static final String ARTIST_FIELD = "artist";
    private static final String TRACK_FIELD = "track";

    public LastFmApi(IRequest req){
        super(req);
    }

    public CompletableFuture<LfmArtistDto> getArtistInfo(String artist){
        artist = artist.replace(' ', '+');
        return get(HOST, ARTIST, KEY, ARTIST_FIELD, LfmArtistDto::fromJson, artist);
    }

    public CompletableFuture<LfmTrackDto> getTrackInfo(String artist, String track){
        artist = artist.replace(' ', '+');
        track = track.replace(' ', '+');
        return get(HOST, TRACK, KEY, TRACK_FIELD, LfmTrackDto::fromJson, track, artist);
    }

}
