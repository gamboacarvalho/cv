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

import movasync.MovService;
import movasync.model.Credit;
import movasync.model.Person;
import movasync.model.SearchItem;
import org.junit.jupiter.api.Test;
import util.ApiRequest;
import util.TestDto;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class MovServiceTestForGalo {

    @Test
    public void testSearchMovieInSinglePage() {
        MovServiceTest.testSearchMovieInSinglePage( "War Games", "War Games: The Dead Code");
    }

    @Test
    public void testSearchMovieManyPages() {
        Supplier<Stream<SearchItem>> movs = MovServiceTest.testSearchMovieManyPages("galo", "Galo Sengen");
        //TestDto dto = ApiRequest.search("galo");
        assertEquals( 51, movs.get().count());// Number of returned movies
    }

    @Test
    public void testMovieDbApiGetActor() {
        MovServiceTest.testMovieDbApiGetActor("Cruel Intentions",1234);
    }

    @Test
    public void testSearchMovieThenActorsThenMoviesAgain() {
        MovService movapi = MovServiceTest.testSearchMovieThenActorsThenMoviesAgain();
        TestDto dto = ApiRequest.search("galo");

        List<SearchItem> list = movapi.search("galo").join().collect(Collectors.toList());
        Supplier<Stream<SearchItem>> galo = list::stream;

        assertEquals(dto.getTotal_results(), list.size());// number of returned movies

        SearchItem axterix = galo.get()
                .filter(m -> m.getTitle().equals("Asterix the Gaul"))
                .findFirst()
                .get();
        assertEquals(11047, axterix.getId());
        assertEquals("Asterix the Gaul", axterix.getTitle());

        assertEquals("Astérix le Gaulois", axterix.getDetails().join().getOriginalTitle());
        assertEquals("", axterix.getDetails().join().getTagline());

        List<Credit> collect = axterix.getDetails().join().getCast().join().collect(Collectors.toList());
        Supplier<Stream<Credit>> axterixcast = () -> collect.stream();
        assertEquals("Roger Carel",
                axterixcast.get().findFirst().get().getName());
        Stream<Credit> iter = axterixcast.get().skip(2);
        assertEquals("Jacques Jouanneau",
                iter.findFirst().get().getName());

        Credit roger = axterixcast.get().findFirst().get();
        Person zzz = roger.getActor().join();
        assertEquals("Paris, France",
                zzz.getPlaceOfBirth());

        assertEquals("The Down-in-the-Hole Gang",
                roger.getActor().join().getMovies().join().findFirst().get().getTitle());

        assertEquals("New York City, New York, USA",
                movapi.getMovie(860).join().getCast().join().findFirst().get().getActor().join().getPlaceOfBirth());

        assertEquals("Predator",
                movapi.getMovie(861).join().getCast().join().findFirst().get().getActor().join().getMovies().join().findFirst().get().getTitle());
    }
}
