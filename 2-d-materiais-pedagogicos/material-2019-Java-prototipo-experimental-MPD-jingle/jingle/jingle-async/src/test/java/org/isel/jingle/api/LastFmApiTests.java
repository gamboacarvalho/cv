package org.isel.jingle.api;

import org.isel.jingle.dto.albums.AlbumDto;
import org.isel.jingle.dto.artists.ArtistDto;
import org.isel.jingle.dto.tracks.TrackDto;
import org.isel.jingle.dto.tracks.topTracks.TopTrackDto;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;


public abstract class LastFmApiTests {
    private final static String NO_ALBUM_MBID_SEARCH = "a36a84af-a72b-436a-9d1a-807362b5a1f5";

    protected abstract LastFmApi getLastFmApi();

    @Test
    public void searchForArtistsNamedDavid() throws ExecutionException, InterruptedException {
        LastFmApi api = getLastFmApi();
        CompletableFuture<ArtistDto[]> result = api.searchArtist("muse", 1);

        ArtistDto[] artists = result.get();

        String name = artists[0].getName();
        assertEquals("Muse", name);
    }


    @Test
    public void getTopAlbumsFromMuse() throws ExecutionException, InterruptedException {
        LastFmApi api = getLastFmApi();
        CompletableFuture<ArtistDto[]> result = api.searchArtist("muse", 1);

        ArtistDto[] artists = result.get();
        String mbid = artists[0].getMbid();

        CompletableFuture<AlbumDto[]> albums = api.getAlbums(mbid, 1);
        assertEquals("Black Holes and Revelations", albums.get()[0].getName());
    }

    @Test
    public void getStarlightFromBlackHolesAlbumOfMuse() throws ExecutionException, InterruptedException {
        LastFmApi api = getLastFmApi();
        CompletableFuture<ArtistDto[]> result = api.searchArtist("muse", 1);

        String mbid = result.get()[0].getMbid();
        AlbumDto album = api.getAlbums(mbid, 1).get()[0];
        TrackDto track = api.getAlbumInfo(album.getAlbumMbid()).get()[1];

        assertEquals("Starlight", track.getName());
    }

    @Test
    public void getAlbumsWithNoTracksShouldReturnNull() throws ExecutionException, InterruptedException {
        LastFmApi api = getLastFmApi();
        AlbumDto albumDto = api.getAlbums(NO_ALBUM_MBID_SEARCH, 2).get()[0];

        assertNull(albumDto.getAlbumMbid());
        assertNull(albumDto.getTracks());
        assertNotNull(albumDto.getName());
    }

    @Test
    public void getTopTracksForPortugal() throws ExecutionException, InterruptedException {
        LastFmApi api = getLastFmApi();
        TopTrackDto[] actual = api.getTopTracks("Portugal", 1).get();

        assertNotNull(actual);
        assertEquals("The Less I Know the Better", actual[0].getName());
        assertEquals(0, actual[0].getRank());
        assertEquals(50, actual.length);
    }
}
