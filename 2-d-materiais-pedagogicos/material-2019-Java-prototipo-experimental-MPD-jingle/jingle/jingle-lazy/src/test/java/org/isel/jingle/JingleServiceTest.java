/*
 * GNU General Public License v3.0
 *
 * Copyright (c) 2019, Miguel Gamboa (gamboa.pt)
 *
 *   All rights granted under this License are granted for the term of
 * copyright on the Program, and are irrevocable provided the stated
 * conditions are met.  This License explicitly affirms your unlimited
 * permission to run the unmodified Program.  The output from running a
 * covered work is covered by this License only if the output, given its
 * content, constitutes a covered work.  This License acknowledges your
 * rights of fair use or other equivalent, as provided by copyright law.
 *
 *   You may make, run and propagate covered works that you do not
 * convey, without conditions so long as your license otherwise remains
 * in force.  You may convey covered works to others for the sole purpose
 * of having them make modifications exclusively for you, or provide you
 * with facilities for running those works, provided that you comply with
 * the terms of this License in conveying all material for which you do
 * not control copyright.  Those thus making or running the covered works
 * for you must do so exclusively on your behalf, under your direction
 * and control, on terms that prohibit them from making any copies of
 * your copyrighted material outside their relationship with you.
 *
 *   Conveying under any other circumstances is permitted solely under
 * the conditions stated below.  Sublicensing is not allowed; section 10
 * makes it unnecessary.
 *
 */

package org.isel.jingle;

import org.isel.jingle.model.Album;
import org.isel.jingle.model.Artist;
import org.isel.jingle.model.TopTrack;
import org.isel.jingle.model.Track;
import org.isel.jingle.util.req.BaseRequest;
import org.isel.jingle.util.req.HttpRequest;
import org.isel.jingle.util.streams.StreamUtils;
import org.junit.Test;

import java.io.InputStream;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static junit.framework.Assert.*;

public class JingleServiceTest {
    static class HttpGet implements Function<String, InputStream> {
        int count = 0;

        @Override
        public InputStream apply(String path) {
            System.out.println("Requesting..." + ++count + " from " + path);
            return HttpRequest.openStream(path);
        }
    }

    @Test
    public void searchHiperAndCountAllResults() {
        HttpGet httpGet = new HttpGet();
        JingleService service = new JingleService(new LastFmWebApi(new BaseRequest(httpGet)));

        Supplier<Stream<Artist>> artists = () -> service.searchArtist("hiper");

        assertEquals(0, httpGet.count);
        assertTrue(artists.get().count() > 0);
        assertEquals(25, httpGet.count);

        Artist last = StreamUtils.of(artists.get()).last();

        assertEquals("Coma - Hipertrofia.(2008)", last.getName());
        assertEquals(50, httpGet.count);
    }

    @Test
    public void getFirstAlbumOfMuse() {
        HttpGet httpGet = new HttpGet();
        JingleService service = new JingleService(new LastFmWebApi(new BaseRequest(httpGet)));

        Stream<Artist> queryArtistMuse = service.searchArtist("muse");
        assertEquals(0, httpGet.count);

        Artist muse = queryArtistMuse.findFirst().orElse(null);

        assertNotNull(muse);
        assertEquals(1, httpGet.count);

        Stream<Album> albumsOfMuse = muse.getAlbums();

        assertEquals(1, httpGet.count);

        Album first = albumsOfMuse.findFirst().orElse(null);

        assertEquals(2, httpGet.count);
        assertNotNull(first);
        assertEquals("Black Holes and Revelations", first.getName());
    }

    @Test
    public void get111AlbumsOfMuse() {
        HttpGet httpGet = new HttpGet();
        JingleService service = new JingleService(new LastFmWebApi(new BaseRequest(httpGet)));

        Stream<Artist> queryArtistMuse = service.searchArtist("muse");
        Artist muse = queryArtistMuse.findFirst().orElse(null);

        assertNotNull(muse);

        Stream<Album> limit = muse.getAlbums().limit(111);

        assertEquals(111, limit.count());
        assertEquals(4, httpGet.count); // 1 for artist + 2 pages of albums
        //TODO - Get pages from mock file to avoid stupid failures for API
        //On page 2 of "muse" albums (shown bellow) the api says that 50 elements are on the page, but the obtained data shows otherwise (as seen on 2nd link)
        //(http://ws.audioscrobbler.com/2.0/?method=artist.gettopalbums&format=json&mbid=fd857293-5ab8-40de-b29e-55a69d4e4d0f&page=2&api_key=56bb17cf0d9c392dbb7e24ca955c8cb7)
        //
        //JSON Representation for page 2: https://jsoneditoronline.org/?url=http%3A%2F%2Fws.audioscrobbler.com%2F2.0%2F%3Fmethod%3Dartist.gettopalbums%26format%3Djson%26mbid%3Dfd857293-5ab8-40de-b29e-55a69d4e4d0f%26page%3D2%26api_key%3D56bb17cf0d9c392dbb7e24ca955c8cb7
        //
        // Therefor meaning that only 2 requests are being done.
    }

    @Test
    public void getSecondSongFromBlackHolesAlbumOfMuse() {
        HttpGet httpGet = new HttpGet();
        JingleService service = new JingleService(new LastFmWebApi(new BaseRequest(httpGet)));

        Album blackHoles = service.searchArtist("muse").findFirst().get().getAlbums().findFirst().get();

        assertEquals(2, httpGet.count); // 1 for artist + 1 page of albums
        assertEquals("Black Holes and Revelations", blackHoles.getName());

        Track song = blackHoles.getTracks().skip(1).findFirst().orElse(null);

        assertNotNull(song);
        assertEquals(3, httpGet.count); // + 1 to getTopTracksContainer
        assertEquals("Starlight", song.getName());
    }

    @Test
    public void get42thTrackOfMuse() {
        HttpGet httpGet = new HttpGet();
        JingleService service = new JingleService(new LastFmWebApi(new BaseRequest(httpGet)));

        Stream<Track> tracks = service.searchArtist("muse").findFirst().get().getTracks();

        assertEquals(1, httpGet.count); // 1 for artist + 0 for tracks because it fetches lazily

        Track track = tracks.skip(42).findFirst().orElse(null);

        assertNotNull(track);
        assertEquals("MK Ultra", track.getName());
        assertEquals(6, httpGet.count);
    }

    @Test
    public void getLastTrackOfMuseOf500() {
        HttpGet httpGet = new HttpGet();
        JingleService service = new JingleService(new LastFmWebApi(new BaseRequest(httpGet)));

        Stream<Track> tracks = service.searchArtist("muse").findFirst().get().getTracks().limit(500);

        assertEquals(500, tracks.count());
        assertTrue(httpGet.count > 70); // Each page has 50 albums => 50 requests to get their tracks. Some albums have no tracks.
    }


    @Test
    public void searchHiperAndCountAllResultsWhenCached() {
        HttpGet httpGet = new HttpGet();
        JingleService service = new JingleService(new LastFmWebApi(new BaseRequest(httpGet)));

        Stream<Artist> artists = service.searchArtist("hiper");

        Supplier<Stream<Artist>> cachedArtists = StreamUtils.of(artists).cache();

        assertEquals(0, httpGet.count);
        assertTrue(cachedArtists.get().count() > 0);
        assertEquals(25, httpGet.count);

        Artist last = StreamUtils.of(cachedArtists.get()).last();

        assertEquals("Coma - Hipertrofia.(2008)", last.getName());
        assertEquals(25, httpGet.count);
    }

    @Test
    public void searchAlbumWithNoTracksReturnsEmptyList() {
        HttpGet httpGet = new HttpGet();
        JingleService service = new JingleService(new LastFmWebApi(new BaseRequest(httpGet)));

        Stream<Artist> theMusesRapt = service.searchArtist("The-Muses-Rapt");
        Optional<Album> noTracksAlbum = theMusesRapt.flatMap(Artist::getAlbums).filter(album -> album.getName().equalsIgnoreCase("Shamanisma")).findFirst();

        assertEquals(0, noTracksAlbum.get().getTracks().count());
    }

    @Test
    public void searchNonExistentArtistsReturnsEmptyQuery() {
        HttpGet httpGet = new HttpGet();
        JingleService service = new JingleService(new LastFmWebApi(new BaseRequest(httpGet)));

        Supplier<Stream<Artist>> noArtistResult = () -> service.searchArtist("akdlkaslsadkldaskads");

        assertEquals(0, noArtistResult.get().count());
        assertEquals(0, noArtistResult.get().flatMap(Artist::getAlbums).count());
        assertEquals(0, noArtistResult.get().flatMap(Artist::getTracks).count());
    }

    @Test
    public void searchTopTrackForPortugal() {
        HttpGet httpGet = new HttpGet();
        JingleService service = new JingleService(new LastFmWebApi(new BaseRequest(httpGet)));

        Supplier<Stream<TopTrack>> topTracks = () -> service.getTopTracks("Portugal");

        assertEquals(0, httpGet.count);

        TopTrack topTrack = topTracks.get().findFirst().get();

        assertEquals(1, httpGet.count);
        assertEquals("Portugal", topTrack.getCountry());
        assertEquals(0, topTrack.getRank());
    }

    @Test
    public void topTrackSearchResultShouldBeOrderedByRank() {
        HttpGet httpGet = new HttpGet();
        JingleService service = new JingleService(new LastFmWebApi(new BaseRequest(httpGet)));

        Supplier<Stream<TopTrack>> topTracks = () -> service.getTopTracks("Portugal");

        final int count[] = {0};
        topTracks.get()
                .limit(50)
                .forEach(topTrack -> assertEquals(count[0]++, topTrack.getRank()));
    }

    @Test
    public void testTracksRank() {
        HttpGet httpGet = new HttpGet();
        JingleService service = new JingleService(new LastFmWebApi(new BaseRequest(httpGet)));

        String artistMbid = "ada7a83c-e3e1-40f1-93f9-3e73dbc9298a";
        String country = "Portugal";

        Supplier<Stream<Track>> result = StreamUtils
                .of(service.getTracksRank(artistMbid, country))
                .cache();

        result
                .get()
                .limit(10)
                .forEach(track -> assertTrue(track.getCountryRanks().containsKey(country)));
    }

}
