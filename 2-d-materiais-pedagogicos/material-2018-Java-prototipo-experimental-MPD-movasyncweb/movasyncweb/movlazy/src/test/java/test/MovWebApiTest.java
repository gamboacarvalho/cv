package test;

import movlazy.MovWebApi;
import movlazy.dto.*;
import org.junit.jupiter.api.Test;
import util.HttpRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MovWebApiTest {

    @Test
    public void getMovieTest() {
        MovWebApi movieWebApi = new MovWebApi(new HttpRequest());
        MovieDto movie = movieWebApi.getMovie(860);
        assertEquals(860,movie.getId());
        assertEquals("WarGames",movie.getOriginalTitle());
    }

    @Test
    public void getPersonTest() {
        MovWebApi movieWebApi = new MovWebApi(new HttpRequest());
        PersonDto person = movieWebApi.getPerson(4756);
        assertEquals("Matthew Broderick",person.getName());
        assertEquals("New York City, New York, USA",person.getPlace_of_birth());
    }

    @Test
    public void getPersonCreditsCastTest() {
        MovWebApi movieWebApi = new MovWebApi(new HttpRequest());
        SearchItemDto[] searchItemDto = movieWebApi.getPersonCreditsCast(4756);
        assertEquals("Inspector Gadget",searchItemDto[0].getTitle());
        assertEquals("1999-07-23",searchItemDto[0].getReleaseDate());
        assertEquals("To Dust",searchItemDto[searchItemDto.length-1].getTitle());
        assertEquals("2018-04-22",searchItemDto[searchItemDto.length-1].getReleaseDate());
    }

    @Test
    public void getMovieCastTest() {
        MovWebApi movieWebApi = new MovWebApi(new HttpRequest());
        CastItemDto[] movieCast = movieWebApi.getMovieCredits(860).getCast();
        assertEquals("David Lightman",movieCast[0].getCharacter());
        assertEquals("Matthew Broderick",movieCast[0].getName());
        assertEquals("NORAD Officer",movieCast[movieCast.length-1].getCharacter());
        assertEquals("William H. Macy",movieCast[movieCast.length-1].getName());
    }

    @Test
    public void searchTest() {
        MovWebApi movieWebApi = new MovWebApi(new HttpRequest());
        SearchItemDto[] search = movieWebApi.search("war games", 1);
        assertEquals("War Games: The Dead Code",search[0].getTitle());
        assertEquals("2008-07-29",search[0].getReleaseDate());
        assertEquals("WarGames",search[search.length-1].getTitle());
        assertEquals("1983-06-03",search[search.length-1].getReleaseDate());
    }
}
