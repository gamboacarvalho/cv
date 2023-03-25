package test;

import com.google.common.util.concurrent.RateLimiter;
import movlazy.MovService;
import movlazy.MovWebApi;
import movlazy.dto.SearchItemDto;
import movlazy.model.SearchItem;
import util.ApiRequest;
import util.HttpRequest;
import util.IRequest;
import util.TestDto;

import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MovServiceTest {

    public static void testSearchMovieInSinglePage(String name, String title) {
        MovService movapi = new MovService(new MovWebApi(new HttpRequest()));
        Supplier<Stream<SearchItem>> movs = movapi.search(name);
        SearchItem m = movs.get().findFirst().get();
        assertEquals(title, m.getTitle());
        TestDto dto = ApiRequest.search(name);
        assertEquals( dto.getTotal_results() , movs.get().count());// number of returned movies
    }


    public static Supplier<Stream<SearchItem>> testSearchMovieManyPages(int[] count, String name, String title) {
        IRequest req = new HttpRequest()
                .compose(System.out::println)
                .compose(__ -> count[0]++);

        MovService movapi = new MovService(new MovWebApi(req));
        Supplier<Stream<SearchItem>> movs = movapi.search(name);

        assertEquals(0, count[0]);

        SearchItem candleshoe = movs.get()
                                .filter(m -> m.getTitle().equals(title))
                                .findFirst()
                                .get();

        return movs;
    }

    public static void testMovieDbApiGetActor(int[] count, String title, int personId) {
        IRequest req = new HttpRequest()
                // .compose(System.out::println)
                .compose(__ -> count[0]++);

        MovWebApi movWebApi = new MovWebApi(req);
        SearchItemDto[] actorMovs = movWebApi.getPersonCreditsCast(personId);
        assertNotNull(actorMovs);
        assertEquals(title, actorMovs[1].getTitle());
        assertEquals(1, count[0]); // 1 request
    }

    public static MovService testSearchMovieThenActorsThenMoviesAgain(int[] count) {
        final RateLimiter rateLimiter = RateLimiter.create(1.0);
        IRequest req = new HttpRequest()
                .compose(__ -> count[0]++)
                .compose(System.out::println)
                .compose(__ -> rateLimiter.acquire());

        return new MovService(new MovWebApi(req));
    }

    public static void testSearchMovieWithManyPages(String name) {
        int[] count = {0};
        final RateLimiter rateLimiter = RateLimiter.create(1.0);
        IRequest req = new HttpRequest()
                .compose(__ -> count[0]++)
                .compose(System.out::println)
                .compose(__ -> rateLimiter.acquire());
                //.compose( MovServiceTest::printToFile );

        MovService movapi = new MovService(new MovWebApi(req));
        TestDto dto = ApiRequest.search(name);


        Supplier<Stream<SearchItem>> vs = movapi.search(name);
        assertEquals(dto.getTotal_results(), vs.get().count()); // number of returned movies
        assertEquals(dto.getTotal_pages() + 1, count[0]); // 2 requests to consume all pages
    }

    /*private static InputStream printToFile(String uri, InputStream result) {
        String fileName = UriToFilenameParser.parse( uri );
        String filePath = new File("src/test/resources")
                                    .getAbsolutePath()
                                    .concat("\\")
                                    .concat(fileName);
        URL resourcesPath = ClassLoader.getSystemResource(filePath);
        if(resourcesPath != null) return result;
        InputStream inputStream = null;
        try (PrintWriter writer = new PrintWriter(filePath, "UTF-8")){
            Iterable<String> lines =  () -> new InputStreamLineIterator( () -> result) ;
            String content = reduce(lines, "", (prev, curr) -> prev + curr);
            writer.write( content );
            inputStream = new ByteArrayInputStream(content.getBytes("UTF-8"));
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return inputStream;
    }
    */
}
