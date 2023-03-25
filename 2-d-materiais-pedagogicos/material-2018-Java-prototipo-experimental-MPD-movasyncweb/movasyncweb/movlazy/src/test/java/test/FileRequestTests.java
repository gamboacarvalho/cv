package test;

import movlazy.MovService;
import movlazy.MovWebApi;
import movlazy.model.SearchItem;
import org.junit.jupiter.api.Test;
import util.FileRequest;
import util.IRequest;

import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class FileRequestTests {
    @Test
    public void testSearchMovieInSinglePage() {
        MovService movapi = new MovService(new MovWebApi(new FileRequest()));
        Supplier<Stream<SearchItem>> movs = movapi.search("War Games");
        SearchItem m = movs.get().findFirst().get();
        assertEquals("War Games: The Dead Code", m.getTitle());
        assertEquals(6, movs.get().count());// number of returned movies
    }

    @Test
    public void testNoMoviesFound() {
        MovService movapi = new MovService(new MovWebApi(new FileRequest()));
        Supplier<Stream<SearchItem>> movs = movapi.search("empty");
        assertEquals(0, movs.get().count());// number of returned movies
    }

    @Test
    public void testSearchMovieManyPages() {
        int[] count = {0};
        IRequest req = new FileRequest()
                .compose(System.out::println)
                .compose(__ -> count[0]++);

        MovService movapi = new MovService(new MovWebApi(req));
        Supplier<Stream<SearchItem>> movs = movapi.search("candle");
        assertEquals(0, count[0]);
        SearchItem candleshoe = movs.get()
                .filter(m -> m.getTitle().equals("Candleshoe"))
                .findFirst()
                .get();
        assertEquals(2, count[0]); // Found on 2nd page
        assertEquals(40, movs.get().count());// Number of returned movies
        assertEquals(5, count[0]); // 3 requests more to consume all pages
    }

}
