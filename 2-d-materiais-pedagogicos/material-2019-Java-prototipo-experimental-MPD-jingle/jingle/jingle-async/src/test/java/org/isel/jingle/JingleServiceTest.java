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

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import org.isel.jingle.api.LastFmWebApi;
import org.isel.jingle.asyncReq.AsyncRequest;
import org.isel.jingle.mock.MockAsyncRequest;
import org.isel.jingle.model.Album;
import org.isel.jingle.model.Artist;
import org.isel.jingle.model.TopTrack;
import org.isel.jingle.model.Track;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.*;

public class JingleServiceTest {
    static class HttpGet implements AsyncRequest {
        int count = 0;
        //BaseRequestAsync requestAsync = new BaseRequestAsync();
        MockAsyncRequest requestAsync = new MockAsyncRequest();

        @Override
        public CompletableFuture<String> getBody(String path) {
            System.out.println("Requesting..." + ++count + " from " + path);
            return requestAsync.getBody(path);
        }
    }

    private HttpGet httpGet;
    private JingleService service;

    @Before
    public void initializeRequestService() {
        httpGet = new HttpGet();
        service = new JingleService(new LastFmWebApi(httpGet));
    }


    @Test
    public void searchHiperAndCountAllResults() {
        Observable<Artist> artists = service.searchArtist("hiper");
        List<Artist> result = new ArrayList<>();

        assertEquals(0, httpGet.count);
        assertEquals(0, result.size());

        artists.blockingSubscribe(result::add, Throwable::printStackTrace);

        assertTrue(result.size() > 0);
        assertEquals(25, httpGet.count);

        Artist last = artists.blockingLast(null);

        assertNotNull(last);
        assertEquals("Coma - Hipertrofia.(2008)", last.getName());

        assertEquals(50, httpGet.count);
    }


    @Test
    public void multipleSubscribersOnObservableShouldWorkProperlyAndRequestTwice() {
        //Unless the value is cached, multiple subscribers will request multiple times
        Observable<Artist> artists = service.searchArtist("hiper");

        Disposable subscribe = artists.subscribe(Assert::assertNotNull, ex -> fail());
        artists.blockingSubscribe(Assert::assertNotNull, ex -> fail());

        assertEquals(50, httpGet.count);
        subscribe.dispose();
    }

    @Test
    public void getFirstAlbumOfMuse() {
        Observable<Artist> queryArtistMuse = service.searchArtist("muse");

        assertEquals(0, httpGet.count);

        Artist muse = queryArtistMuse.blockingFirst(null);

        assertNotNull(muse);
        assertEquals(1, httpGet.count);

        Observable<Album> albumsOfMuse = muse.getAlbums();

        //No extra requests should be done
        assertEquals(1, httpGet.count);

        Album first = albumsOfMuse.blockingFirst();

        assertEquals(2, httpGet.count);
        assertNotNull(first);
        assertEquals("Black Holes and Revelations", first.getName());
    }

    @Test
    public void get111AlbumsOfMuse() {
        Observable<Artist> queryArtistMuse = service.searchArtist("muse");
        Artist muse = queryArtistMuse.blockingFirst(null);

        assertNotNull(muse);

        Observable<Album> limit = muse.getAlbums().take(111);

        assertEquals(Long.valueOf(111), limit.count().blockingGet());
        assertEquals(4, httpGet.count); // 1 for artist + 2 pages of albums
    }

    @Test
    public void getSecondSongFromBlackHolesAlbumOfMuse() {
        Album blackHoles = service.searchArtist("muse").blockingFirst().getAlbums().blockingFirst();

        assertEquals(2, httpGet.count); // 1 for artist + 1 page of albums
        assertEquals("Black Holes and Revelations", blackHoles.getName());

        Track song = blackHoles.getTracks().skip(1).blockingFirst();

        assertNotNull(song);
        assertEquals(3, httpGet.count); // + 1 to getTopTracksContainer
        assertEquals("Starlight", song.getName());
    }

    @Test
    public void get42thTrackOfMuse() {
        Observable<Track> tracks = service.searchArtist("muse").blockingFirst(null).getTracks();

        assertEquals(1, httpGet.count); // 1 for artist + 0 for tracks because it fetches lazily

        Track track = tracks.skip(42).blockingFirst(null);

        assertNotNull(track);
        assertEquals("Hoodoo", track.getName());
        assertEquals(6, httpGet.count);
    }


    @Test
    public void getLastTrackOfMuseOf200() {
        Artist muse = service.searchArtist("muse").blockingFirst(null);

        assertNotNull(muse);

        Observable<Track> tracks = muse.getTracks().take(200).cache();

        Track first = tracks.blockingFirst(null);
        Track last = tracks.blockingLast(null);

        assertNotNull(first);
        assertNotNull(last);

        assertNotEquals(first, last);

        assertEquals(Long.valueOf(200), tracks.count().blockingGet());
        assertTrue(httpGet.count > 20);
        // Each page has 50 albums => 50 requests to get their tracks. Some albums have no tracks.
    }

    @Test
    public void creatingObservableThenCachingItShouldNotSendRequestsUntilSubscribed() {
        Observable<Artist> muse = service.searchArtist("muse");

        assertEquals(0, httpGet.count);

        //Cache the result - this should *not* start the api requests
        Observable<Artist> cached = muse.take(60).cache();
        assertEquals(0, httpGet.count);

        //Only when a subscriber is attached to the observable is when the requests are sent
        cached.blockingSubscribe();
        assertEquals(2, httpGet.count);
    }

    @Test
    public void cachingSearchResultThenFilteringWillRequestBeyondFilterUntilFinished() {
        Observable<Artist> hiper = service.searchArtist("hiper");

        //Expected would be 1 http request, but instead the Observable will request
        //until the 'takeWhile' condition in 'JingleService' class is true
        hiper.cache().take(30).blockingSubscribe();

        assertEquals(25, httpGet.count);

        //To avoid this "pit-fall", one should filter first *then* cache the given observable, like so:
        httpGet.count = 0; //reset counter
        Observable<Artist> secondHiper = service.searchArtist("hiper");

        secondHiper.take(30).cache().blockingSubscribe();

        assertEquals(1, httpGet.count);
    }


    @Test
    public void cachingSearchResultShouldNotSendExtraRequests() {
        Observable<Artist> artists = service.searchArtist("hiper");
        Observable<Artist> cachedArtists = artists.cache();

        //Only when a subscriber is attached to the observable is when the requests are sent
        assertTrue(cachedArtists.count().blockingGet() > 0);
        assertEquals(25, httpGet.count);

        //Fetch last, which should not start more requests, as they are cached
        Artist last = cachedArtists.blockingLast(null);

        assertNotNull(last);
        assertEquals("Coma - Hipertrofia.(2008)", last.getName());
        assertEquals(25, httpGet.count);
    }

    @Test
    public void searchAlbumWithNoTracksReturnsEmptyList() {
        String noMbidAlbum = "Shamanisma";
        Observable<Artist> theMusesRapt = service.searchArtist("Muses-Rapt");

        Artist artist = theMusesRapt.blockingFirst(null);

        assertNotNull(artist);

        Album result = artist
                .getAlbums()
                .filter(album -> album.getName().equalsIgnoreCase(noMbidAlbum))
                .blockingFirst(null);

        assertNotNull(result);
        assertNull(result.getMbid());
    }

    @Test
    public void searchNonExistentArtistsReturnsEmptyQuery() {
        Observable<Artist> noArtistResult = service.searchArtist("akdlkaslsadkldaskads").cache();

        assertEquals(Long.valueOf(0), noArtistResult.count().blockingGet());
        assertEquals(Long.valueOf(0), noArtistResult.flatMap(Artist::getAlbums).count().blockingGet());
        assertEquals(Long.valueOf(0), noArtistResult.flatMap(Artist::getTracks).count().blockingGet());
    }

    @Test
    public void searchTopTrackForPortugal() {
        Observable<TopTrack> topTracks = service.getTopTracks("Portugal");

        assertEquals(0, httpGet.count);

        TopTrack topTrack = topTracks.blockingFirst(null);

        assertNotNull(topTrack);
        assertEquals(1, httpGet.count);
        assertEquals("Portugal", topTrack.getCountry());
        assertEquals(0, topTrack.getRank());
    }

    @Test
    public void topTrackSearchResultShouldBeOrderedByRank() {
        Observable<TopTrack> topTracks = service.getTopTracks("Portugal");

        final int count[] = {0};
        topTracks
                .take(50)
                .blockingForEach(topTrack -> assertEquals(count[0]++, topTrack.getRank()));
    }

    @Test
    public void testTracksRank() {
        String artistMbid = "fd857293-5ab8-40de-b29e-55a69d4e4d0f";
        String country = "Portugal";

        Observable<Track> result = service.getTracksRank(artistMbid, country);

        result
                .take(10)
                .blockingForEach(track -> assertTrue(track.getCountryRanks().containsKey(country)));
    }

}
