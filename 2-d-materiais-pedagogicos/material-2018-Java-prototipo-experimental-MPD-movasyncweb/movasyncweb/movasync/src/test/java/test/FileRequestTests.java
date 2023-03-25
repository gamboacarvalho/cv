package test;

import movasync.MovWebApi;
import movasync.dto.CastItemDto;
import movasync.dto.MovieDto;
import movasync.dto.PersonDto;
import movasync.dto.SearchItemDto;
import org.junit.jupiter.api.Test;
import util.FileRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class FileRequestTests {

    @Test
    public void getMovieTest() {
        MovWebApi movieWebApi = new MovWebApi(new FileRequest());
        MovieDto movie = movieWebApi.getMovie(860).join();
        assertEquals(860,movie.getId());
        assertEquals("WarGames",movie.getOriginalTitle());
    }

    @Test
    public void getPersonTest() {
        MovWebApi movieWebApi = new MovWebApi(new FileRequest());
        PersonDto person = movieWebApi.getPerson(4756).join();
        assertEquals("Matthew Broderick",person.getName());
        assertEquals("New York City, New York, USA",person.getPlace_of_birth());
    }

    @Test
    public void getPersonCreditsCastTest() {
        MovWebApi movieWebApi = new MovWebApi(new FileRequest());
        SearchItemDto[] searchItemDto = movieWebApi.getPersonCreditsCast(4756).join();
        assertEquals("Inspector Gadget",searchItemDto[0].getTitle());
        assertEquals("1999-07-23",searchItemDto[0].getReleaseDate());
        assertEquals("To Dust",searchItemDto[searchItemDto.length-1].getTitle());
        assertEquals("2018-04-22",searchItemDto[searchItemDto.length-1].getReleaseDate());
    }

    @Test
    public void getMovieCastTest() {
        MovWebApi movieWebApi = new MovWebApi(new FileRequest());
        CastItemDto[] movieCast = movieWebApi.getMovieCredits(860).join().getCast();
        assertEquals("David Lightman",movieCast[0].getCharacter());
        assertEquals("Matthew Broderick",movieCast[0].getName());
        assertEquals("NORAD Officer",movieCast[movieCast.length-1].getCharacter());
        assertEquals("William H. Macy",movieCast[movieCast.length-1].getName());
    }

    @Test
    public void searchTest() {
        MovWebApi movieWebApi = new MovWebApi(new FileRequest());
        SearchItemDto[] search = movieWebApi.search("war games", 1).join().getResults();
        assertEquals("War Games: The Dead Code",search[0].getTitle());
        assertEquals("2008-07-29",search[0].getReleaseDate());
        assertEquals("WarGames",search[search.length-1].getTitle());
        assertEquals("1983-06-03",search[search.length-1].getReleaseDate());
    }
}
