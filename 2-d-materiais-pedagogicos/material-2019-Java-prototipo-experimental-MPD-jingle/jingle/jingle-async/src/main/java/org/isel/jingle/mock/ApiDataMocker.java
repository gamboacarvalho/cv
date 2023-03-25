package org.isel.jingle.mock;

import io.reactivex.Observable;
import org.isel.jingle.api.LastFmWebApi;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * Responsible for fetching data from LastFM API and saving it to a file,
 * in order for the files to be used as Mock for tests
 */
public class ApiDataMocker {
    private static final String ARTIST_HIPER = "Hiper";
    private static final String ARTIST_MUSE = "Muse";
    private static final String ARTIST_MUSES_RAPT = "Muses-Rapt";
    private static final String ARTIST_INVALID = "akdlkaslsadkldaskads";

    private static final String MUSE_ARTIST_ID = "fd857293-5ab8-40de-b29e-55a69d4e4d0f";
    private static final String MUSES_RAPT_ARTIST_ID = "a36a84af-a72b-436a-9d1a-807362b5a1f5";

    private static final String MUSE_BLACK_HOLES_ALBUM_TRACKS_ID = "aefcf53b-5980-463b-b03d-a6c8da6a9432";
    private static final String MUSE_THE_RESISTANCE_ALBUM_TRACKS_ID = "6a5d9eac-0fa6-3170-9cff-a1cb832fd8cd";
    private static final String MUSE_ABSOLUTION_ALBUM_TRACKS_ID = "1dad9b13-1f02-33f6-815e-7fb2e6af17ea";
    private static final String MUSE_ORIGIN_OF_SYM_ALBUM_TRACKS_ID = "1cc29145-b0e4-47bf-8bda-a1edef67dd1d";
    private static final String MUSE_SHOWBIZ_ALBUM_TRACKS_ID = "c8d7945e-e193-4677-b39e-248ce81936e7";
    private static final String MUSE_SECOND_LAW_ALBUM_TRACKS_ID = "e3c0e7c7-df7c-4b51-9894-e45d1480e7b5";

    private static final String PORTUGAL = "Portugal";

    private static final int MAX_PAGES = 25;
    private static final int STARTING_PAGE = 1;

    private static final LastFmWebApi webApi = new LastFmWebApi();


    public static void main(String[] args) {
        //save artists "hiper"; "muse"; "muses-rapt" and an invalid artist that does not exist
        save(page -> saveArtist(ARTIST_HIPER, page), STARTING_PAGE, MAX_PAGES);
        save(page -> saveArtist(ARTIST_MUSE, page), STARTING_PAGE, MAX_PAGES);
        save(page -> saveArtist(ARTIST_MUSES_RAPT, page), STARTING_PAGE, 5);
        save(page -> saveArtist(ARTIST_INVALID, page), STARTING_PAGE, STARTING_PAGE);

        //save response for "muse" and "muses-rapt" top albums
        save(page -> saveAlbum(MUSE_ARTIST_ID, page), STARTING_PAGE, MAX_PAGES);
        save(page -> saveAlbum(MUSES_RAPT_ARTIST_ID, page), STARTING_PAGE, STARTING_PAGE);

        //save response for "muse" album tracks
        save(page -> saveAlbumTrack(MUSE_BLACK_HOLES_ALBUM_TRACKS_ID, page), STARTING_PAGE, STARTING_PAGE);
        save(page -> saveAlbumTrack(MUSE_THE_RESISTANCE_ALBUM_TRACKS_ID, page), STARTING_PAGE, STARTING_PAGE);
        save(page -> saveAlbumTrack(MUSE_ABSOLUTION_ALBUM_TRACKS_ID, page), STARTING_PAGE, STARTING_PAGE);
        save(page -> saveAlbumTrack(MUSE_ORIGIN_OF_SYM_ALBUM_TRACKS_ID, page), STARTING_PAGE, STARTING_PAGE);
        save(page -> saveAlbumTrack(MUSE_SHOWBIZ_ALBUM_TRACKS_ID, page), STARTING_PAGE, STARTING_PAGE);
        save(page -> saveAlbumTrack(MUSE_SECOND_LAW_ALBUM_TRACKS_ID, page), STARTING_PAGE, STARTING_PAGE);


        //save response for top tracks for "Portugal"
        save(page -> saveCountryTopTrack(PORTUGAL, page), STARTING_PAGE, STARTING_PAGE + 1);


    }

    private static <T> void save(Function<Integer, CompletableFuture<T>> mapper, int startingPage, int maxPages) {
        Observable
                .range(startingPage, maxPages)
                .map(mapper::apply)
                .blockingSubscribe(CompletableFuture::get);
    }


    private static CompletableFuture<Void> saveArtist(String artist, Integer page) {
        String lastFmArtistsPath = webApi.getLastFmArtistsPath(artist, page);
        String uri = MockUriFormatter.formatUri(lastFmArtistsPath);

        CompletableFuture<String> bodyForArtist = webApi.getBodyForArtist(artist, page);

        return bodyForArtist.thenAccept(str -> FileSaver.saveFileToSystem(uri, str));
    }

    private static CompletableFuture<Void> saveAlbum(String museArtistId, Integer page) {
        String lastFmAlbumsPath = webApi.getLastFmAlbumsPath(museArtistId, page);
        String uri = MockUriFormatter.formatUri(lastFmAlbumsPath);

        CompletableFuture<String> bodyForAlbums = webApi.getBodyForAlbums(museArtistId, page);

        return bodyForAlbums.thenAccept(str -> FileSaver.saveFileToSystem(uri, str));
    }

    private static CompletableFuture<Void> saveAlbumTrack(String albumMbid, Integer page) {
        String lastFmAlbumInfoPath = webApi.getLastFmAlbumInfoPath(albumMbid);
        String uri = MockUriFormatter.formatUri(lastFmAlbumInfoPath);

        CompletableFuture<String> bodyForAlbumInfo = webApi.getBodyForAlbumInfo(albumMbid);

        return bodyForAlbumInfo.thenAccept(str -> FileSaver.saveFileToSystem(uri, str));
    }


    private static CompletableFuture<Void> saveCountryTopTrack(String country, Integer page) {
        String lastFmTopTracksPath = webApi.getLastFmTopTracksPath(country, page);
        String uri = MockUriFormatter.formatUri(lastFmTopTracksPath);

        CompletableFuture<String> bodyForLastFmTopTracks = webApi.getBodyForLastFmTopTracks(country, page);

        return bodyForLastFmTopTracks.thenAccept(str -> FileSaver.saveFileToSystem(uri, str));
    }

}
