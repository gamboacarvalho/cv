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

package test;

import movlazy.MovService;
import movlazy.model.Credit;
import movlazy.model.SearchItem;
import org.junit.jupiter.api.Test;
import util.ApiRequest;
import util.TestDto;

import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class MovServiceTestForBlackPanther {

    @Test
    public void testSearchMovieInSinglePage() {
        MovServiceTest.testSearchMovieInSinglePage( "Black Panther", "Black Panther");
    }

    @Test
    public void testSearchMovieManyPages() {
        int[] count = {0};
        Supplier<Stream<SearchItem>> movs = MovServiceTest.testSearchMovieManyPages(count,
                "Black Panther", "Public Enemy: Reflections of The Black Panthers");
        TestDto dto = ApiRequest.search("Black Panther");

        assertEquals(2, count[0]); // Found on 2nd page
        assertEquals( dto.getTotal_results(), movs.get().count());// Number of returned movies
        assertEquals(5, count[0]); // 4 requests more to consume all pages
    }

    @Test
    public void testMovieDbApiGetActor() {
        int[] count = {0};
        MovServiceTest.testMovieDbApiGetActor(count,"Ladyhawke",4756);
    }

    @Test
    public void testSearchMovieThenActorsThenMoviesAgain() {
        int[] count = {0};
        MovService movapi = MovServiceTest.testSearchMovieThenActorsThenMoviesAgain(count);
        TestDto dto = ApiRequest.search("Black Panther");

        Supplier<Stream<SearchItem>> vs = movapi.search("Black Panther");

        assertEquals(dto.getTotal_results(), vs.get().count());// number of returned movies
        assertEquals(3, count[0]);         // 3 requests to consume all pages
        /**
         * Iterable<SearchItem> is Lazy and without cache.
         */
        SearchItem blackPanther = vs.get()
                                .filter(m -> m.getTitle().equals("Black Panther"))
                                .findFirst()
                                .get();
        assertEquals(4, count[0]); // 1 more request for 1st page
        assertEquals(284054, blackPanther .getId());
        assertEquals("Black Panther", blackPanther.getTitle());
        assertEquals(4, count[0]); // Keep the same number of requests
        /**
         * getDetails() relation SearchItem ---> Movie is Lazy and supported on Supplier<Movie> with Cache
         */
        assertEquals("Black Panther", blackPanther.getDetails().getOriginalTitle());
        assertEquals(5, count[0]); // 1 more request to get the Movie
        assertEquals("Long live the king", blackPanther.getDetails().getTagline());
        assertEquals(5, count[0]); // NO more request. It is already in cache
        /**
         * getCast() relation Movie --->* Credit is Lazy and
         * supported on Supplier<List<Credit>> with Cache
         */
        Supplier<Stream<Credit>> blackPantherCast = blackPanther.getDetails().getCast();
        assertEquals(5, count[0]); // 1 more request to get the Movie Cast
        assertEquals("Chadwick Boseman",
                blackPantherCast.get().findFirst().get().getName());
        assertEquals(6, count[0]); // NO more request. It is already in cache
        Stream<Credit> iter = blackPantherCast.get().skip(2);
        assertEquals("Lupita Nyong'o",
                iter.findFirst().get().getName());
        assertEquals(6, count[0]); // NO more request. It is already in cache
        /**
         * Credit ---> Person is Lazy and with Cache for Person but No cache for actor credits
         */
        Credit broderick = blackPanther.getDetails().getCast().get().findFirst().get();
        assertEquals(6, count[0]); // NO more request. It is already in cache
        assertEquals("Anderson - South Carolina - USA",
                broderick.getActor().getPlaceOfBirth());
        assertEquals(7, count[0]); // 1 more request for Person Person
        assertEquals("Anderson - South Carolina - USA",
                broderick.getActor().getPlaceOfBirth());
        assertEquals(7, count[0]); // NO more request. It is already in cache
        assertEquals("The Kill Hole",
                broderick.getActor().getMovies().get().findFirst().get().getTitle());
        assertEquals(8, count[0]); // 1 more request for Person Credits
        assertEquals("The Kill Hole",
                broderick.getActor().getMovies().get().findFirst().get().getTitle());
        assertEquals(9, count[0]); // 1 more request. Person Cast is not in cache

        /**
         * Check Cache from the beginning
         */
        assertEquals("Anderson - South Carolina - USA",
                movapi.getMovie(284054).getCast().get().findFirst().get().getActor().getPlaceOfBirth());
        assertEquals(9, count[0]); // No more requests for the same getMovie.
        /*
         * Now get a new Film
         */
        assertEquals("La Cesta",
                movapi.getMovie(284055).getCast().get().findFirst().get().getActor().getMovies().get().findFirst().get().getTitle());
        assertEquals(13, count[0]); // 1 request for Movie + 1 for CastItems + 1 Person + 1 Person Credits
    }


    @Test
    public void testSearchMovieWithManyPages() {
        MovServiceTest.testSearchMovieWithManyPages("Black Panther");
    }
}
