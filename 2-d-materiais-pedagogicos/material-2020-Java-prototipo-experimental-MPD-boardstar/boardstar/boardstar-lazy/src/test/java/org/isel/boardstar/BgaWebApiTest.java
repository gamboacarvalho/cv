package org.isel.boardstar;

import org.isel.boardstar.model.Category;
import org.isel.boardstar.model.Game;
import org.junit.Assert;
import org.junit.Test;
import pt.isel.mpd.util.requests.AbstractRequest;
import pt.isel.mpd.util.requests.HttpRequest;
import pt.isel.mpd.util.requests.MockRequest;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static pt.isel.mpd.util.LazyQueries.*;

public class BgaWebApiTest {

    static class RequestMediator extends AbstractRequest {
        private AbstractRequest req;
        int count;
        public RequestMediator(AbstractRequest req ) {
            this.req = req;
        }
        public InputStream getStream(String path) {
            ++count;
            return req.getStream(path);
        }

        public int getCount() {
            return count;
        }
    }

    @Test
    public void testGetCategories() {
        RequestMediator req = new RequestMediator(new MockRequest());
        BoardstarService service = new BoardstarService(new BgaWebApi(req));
        Iterable<Category> categories = service.getCategories();
        Assert.assertEquals(0, req.getCount());
        int nCategories = count(categories);
        Assert.assertEquals(1, req.getCount());
        Assert.assertEquals(121, nCategories);
    }

    @Test
    public void testSearchByCategory() {
        RequestMediator req = new RequestMediator(new MockRequest());
        BoardstarService service = new BoardstarService(new BgaWebApi(req));
        Iterable<Game> games = service.searchByCategory("85OKv8p5Ow");
        Assert.assertEquals(0, req.getCount());
        int nGames = count(games);
        Assert.assertEquals(3, req.getCount());
        Assert.assertEquals(12, nGames);
    }

    @Test
    public void testSearchByArtist() {
        RequestMediator req = new RequestMediator(new MockRequest());
        BoardstarService service = new BoardstarService(new BgaWebApi(req));
        Iterable<Game> games = service.searchByArtist("Dimitri Bielak");
        Assert.assertEquals(0, req.getCount());
        int nGames = count(games);
        Assert.assertEquals(3, req.getCount());
        Assert.assertEquals(19, nGames);
    }

    @Test
    public void testGetArtists() {
        RequestMediator req = new RequestMediator(new MockRequest());
        BoardstarService service = new BoardstarService(new BgaWebApi(req));
        Iterable<Game> games = service.searchByArtist("Dimitri Bielak");
        Assert.assertEquals(0, req.getCount());

        int artistsCount = count(first(games).get().getArtists());

        Assert.assertEquals(1, req.getCount());
        Assert.assertEquals(2, artistsCount);
    }

    @Test
    public void testSearchByArtist_NoGames() {
        RequestMediator req = new RequestMediator(new MockRequest());
        BoardstarService service = new BoardstarService(new BgaWebApi(req));
        Iterable<Game> games = service.searchByArtist("NULL");
        Assert.assertEquals(0, req.getCount());
        int nGames = count(games);
        Assert.assertEquals(1, req.getCount());
        Assert.assertEquals(0, nGames);
    }

    @Test
    public void testSearchByCategory_Cache() {
        RequestMediator req = new RequestMediator(new MockRequest());
        BoardstarService service = new BoardstarService(new BgaWebApi(req));
        Iterable<Game> games = cache(service.searchByCategory("85OKv8p5Ow"));
        Assert.assertEquals(0, req.getCount());
        int nGames  = count(games);
        int nGames2 = count(games);
        Assert.assertEquals(3, req.getCount());
        Assert.assertEquals(12, nGames);
        Assert.assertEquals(12, nGames2);
    }

    @Test
    public void testGetCategories_Cache() {
        RequestMediator req = new RequestMediator(new MockRequest());
        BoardstarService service = new BoardstarService(new BgaWebApi(req));
        Iterable<Game> games = cache(service.searchByCategory("85OKv8p5Ow"));
        Assert.assertEquals(0, req.getCount());

        Object[] gamesStartPage = toArray(limit(games, 10));   // request count = 1 # total request count = 1
        Assert.assertEquals(1, req.getCount());
        Object[] gamesSamePage  = toArray(limit(games, 5));    // request count = 0 # total request count = 1
        Assert.assertEquals(1, req.getCount());
        Object[] gamesNewPage   = toArray(limit(games, 20));   // request count = 2 # total request count = 3
        Assert.assertEquals(3, req.getCount());

        /*
         * Since the 'games' iterable only returns 12 elements in pages of 10,
         * asking for a limit of 20 (which is bigger than the total number of
         * elements) will trigger a new 'request' until we find an empty page.
         */
    }
}
