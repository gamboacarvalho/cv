package org.isel.boardstar;

import org.isel.boardstar.model.Category;
import org.isel.boardstar.model.Game;
import org.junit.Assert;
import org.junit.Test;
import pt.isel.mpd.util.Utils;
import pt.isel.mpd.util.requests.HttpRequest;
import pt.isel.mpd.util.requests.MockRequest;
import pt.isel.mpd.util.requests.RequestMediator;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static pt.isel.mpd.util.streams.StreamUtils.*;

public class StreamQueriesTest {

    @Test
    public void testGetCategories_Cache() {
        RequestMediator req = new RequestMediator(new MockRequest());
        BoardstarService service = new BoardstarService(new BgaWebApi(req));
        Supplier<Stream<Category>> categories = cache(service.getCategories());
        Assert.assertEquals(0, req.getCount());
        long nCategories = categories.get().count();
        Assert.assertEquals(1, req.getCount());
        Assert.assertEquals(121, nCategories);
        long nCategories2 = categories.get().count();
        Assert.assertEquals(1, req.getCount());
        Assert.assertEquals(121, nCategories2);
    }

    @Test
    public void testSearchByCategory_Cache() {
        RequestMediator req = new RequestMediator(new MockRequest());
        BoardstarService service = new BoardstarService(new BgaWebApi(req));
        Supplier<Stream<Game>> games = cache(service.searchByCategory("85OKv8p5Ow"));
        Assert.assertEquals(0, req.getCount());
        long nGames  = games.get().count();
        long nGames2 = games.get().count();
        Assert.assertEquals(3, req.getCount()); //3 pages per request
        Assert.assertEquals(12, nGames);
        Assert.assertEquals(12, nGames2);
    }

    @Test
    public void testSearchByCategoryName_Cache() {
        RequestMediator req = new RequestMediator(new MockRequest());
        BoardstarService service = new BoardstarService(new BgaWebApi(req));
        Supplier<Stream<Game>> games = cache(service.searchByCategoryName("War"));
        Assert.assertEquals(0, req.getCount());
        long nGames = games.get().count();

        // 59 Games in Pages of 10 => 6 Requests + 1 Empty Page Request + 1 Request Categories
        Assert.assertEquals(8, req.getCount());
        Assert.assertEquals(59, nGames);
    }

    @Test
    public void testSearchByCategory_Intersection() {
        RequestMediator req = new RequestMediator(new MockRequest());
        BoardstarService service = new BoardstarService(new BgaWebApi(req));
        Stream<Game> g1 = service.searchByCategory("85OKv8p5Ow");
        Stream<Game> g2 = service.searchByCategory("85OKv8p5Ow").limit(5);
        Stream<Game> intersect = intersection(g1, g2);
        Assert.assertEquals(0, req.getCount());
        long nGames = intersect.count();
        Assert.assertEquals(5, nGames); // Max is 12
    }

    @Test
    public void testPrefix() {
        RequestMediator req = new RequestMediator(new HttpRequest());
        BoardstarService service = new BoardstarService(new BgaWebApi(req));
        Supplier<Stream<Category>> categories = cache(service.getCategories());
        Stream<String> headers = categories.get().limit(5).map(c -> c.getName() + ":");
        Stream<Stream<String>> content = categories.get().limit(5)
                .map(Category::getGames)
                .map(games -> games.limit(10).map(Game::getName));
        Supplier<Stream<String>> prefixed = cache(prefix(headers,content));
        Optional<String> resultOptional = prefixed.get().reduce((old, value) -> {
            if (value.matches("[\\w\\s]+:")) {
                return old + "\n" + value + " ";
            } else {
                return old + value + ",";
            }
        });
        System .out.println("\n" + resultOptional.get() + "\n");
    }
}
