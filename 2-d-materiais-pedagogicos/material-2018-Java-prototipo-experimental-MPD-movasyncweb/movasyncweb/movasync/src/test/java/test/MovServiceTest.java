package test;

import movasync.MovService;
import movasync.MovWebApi;
import movasync.dto.SearchItemDto;
import movasync.model.SearchItem;
import util.ApiRequest;
import util.HttpRequest;
import util.IRequest;
import util.TestDto;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MovServiceTest {

    public static void testSearchMovieInSinglePage(String name, String title) {
        //final RateLimiter rateLimiter = RateLimiter.create(3);
        MovService movapi = new MovService(new MovWebApi(new HttpRequest()
                .compose(System.out::println))
                //.compose(__ -> rateLimiter.acquire()))

        );
        List<SearchItem> searchItems = movapi.search(name).join().collect(Collectors.toList());
        Supplier<Stream<SearchItem>> movs = searchItems::stream;
        SearchItem m = movs.get().findFirst().get();
        assertEquals(title, m.getTitle());
        TestDto dto = ApiRequest.search(name);
        assertEquals( dto.getTotal_results() , movs.get().count());// number of returned movies
    }

    public static Supplier<Stream<SearchItem>> testSearchMovieManyPages(String name, String title) {
        //final RateLimiter rateLimiter = RateLimiter.create(3);
        IRequest req = new HttpRequest()
                .compose(System.out::println);
                //.compose(__ -> rateLimiter.acquire());;

        MovService movapi = new MovService(new MovWebApi(req));
        List<SearchItem> searchItems = movapi.search(name).join().collect(Collectors.toList());

        Supplier<Stream<SearchItem>> movs = searchItems::stream;

        SearchItem galo = movs.get()
                                .filter(m -> m.getTitle().equals(title))
                                .findFirst()
                                .get();

        assertEquals(title,galo.getTitle());
        return movs;
    }

    public static void testMovieDbApiGetActor(String title, int personId) {
        //final RateLimiter rateLimiter = RateLimiter.create(3);
        IRequest req = new HttpRequest()
                 .compose(System.out::println);
                //.compose(__ -> rateLimiter.acquire());;


        MovWebApi movWebApi = new MovWebApi(req);
        SearchItemDto[] actorMovs = movWebApi.getPersonCreditsCast(personId).join();
        assertNotNull(actorMovs);
        assertEquals(title, actorMovs[1].getTitle());
    }


    public static MovService testSearchMovieThenActorsThenMoviesAgain() {
        //final RateLimiter rateLimiter = RateLimiter.create(3);
        IRequest req = new HttpRequest()
                .compose(System.out::println);
                //.compose(__ -> rateLimiter.acquire());

        return new MovService(new MovWebApi(req));
    }
}
