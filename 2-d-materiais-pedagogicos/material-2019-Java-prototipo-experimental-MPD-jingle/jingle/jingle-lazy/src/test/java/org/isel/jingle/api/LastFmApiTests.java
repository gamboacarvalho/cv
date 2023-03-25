package org.isel.jingle.api;

import org.isel.jingle.LastFmApi;
import org.isel.jingle.dto.albums.AlbumDto;
import org.isel.jingle.dto.artists.ArtistDto;
import org.isel.jingle.dto.tracks.TrackDto;
import org.isel.jingle.dto.tracks.topTracks.TopTrackDto;
import org.junit.Test;

import static junit.framework.Assert.*;

public abstract class LastFmApiTests {
    private final static String NO_ALBUM_MBID_SEARCH = "a36a84af-a72b-436a-9d1a-807362b5a1f5";

    protected abstract LastFmApi getLastFmApi();

    @Test
    public void searchForArtistsNamedDavid() {
        LastFmApi api = getLastFmApi();
        ArtistDto[] artists = api.searchArtist("david", 1);
        String name = artists[0].getName();
        assertEquals("David Bowie", name);
    }


    @Test
    public void getTopAlbumsFromMuse() {
        LastFmApi api = getLastFmApi();
        ArtistDto[] artists = api.searchArtist("muse", 1);
        String mbid = artists[0].getMbid();
        AlbumDto[] albums = api.getAlbums(mbid, 1);
        assertEquals("Black Holes and Revelations", albums[0].getName());
    }

    @Test
    public void getStarlightFromBlackHolesAlbumOfMuse() {
        LastFmApi api = getLastFmApi();
        ArtistDto[] artists = api.searchArtist("muse", 1);
        String mbid = artists[0].getMbid();
        AlbumDto album = api.getAlbums(mbid, 1)[0];
        TrackDto track = api.getAlbumInfo(album.getAlbumMbid())[1];
        assertEquals("Starlight", track.getName());
    }

    @Test
    public void getAlbumsWithNoTracksShouldReturnNull() {
        LastFmApi api = getLastFmApi();
        AlbumDto albumDto = api.getAlbums(NO_ALBUM_MBID_SEARCH, 2)[0];

        assertNull(albumDto.getAlbumMbid());
        assertNull(albumDto.getTracks());
        assertNotNull(albumDto.getName());
    }

    @Test
    public void getTopTracksForPortugal() {
        LastFmApi api = getLastFmApi();
        TopTrackDto[] actual = api.getTopTracks("Portugal", 1);

        assertNotNull(actual);
        assertEquals("The Less I Know the Better", actual[0].getName());
        assertEquals(0, actual[0].getRank());
        assertEquals(50, actual.length);
    }
}
