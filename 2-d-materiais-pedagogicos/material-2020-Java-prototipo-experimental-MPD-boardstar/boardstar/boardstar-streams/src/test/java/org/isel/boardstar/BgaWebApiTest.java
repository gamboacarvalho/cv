package org.isel.boardstar;

import org.isel.boardstar.model.Category;
import org.isel.boardstar.model.Game;
import org.junit.Assert;
import org.junit.Test;
import pt.isel.mpd.util.requests.MockRequest;
import pt.isel.mpd.util.requests.RequestMediator;

import java.util.function.Supplier;
import java.util.stream.Stream;


public class BgaWebApiTest {

    @Test
    public void testGetCategories() {
        RequestMediator req = new RequestMediator(new MockRequest());
        BoardstarService service = new BoardstarService(new BgaWebApi(req));
        Supplier<Stream<Category>> categories = service.getCategories();
        Assert.assertEquals(0, req.getCount());
        long nCategories = categories.get().count();
        Assert.assertEquals(1, req.getCount());
        Assert.assertEquals(121, nCategories);
    }

    @Test
    public void testSearchByCategory() {
        RequestMediator req = new RequestMediator(new MockRequest());
        BoardstarService service = new BoardstarService(new BgaWebApi(req));
        Stream<Game> games = service.searchByCategory("85OKv8p5Ow");
        Assert.assertEquals(0, req.getCount());
        long nGames = games.count();
        Assert.assertEquals(3, req.getCount());
        Assert.assertEquals(12, nGames);
    }

    @Test
    public void testSearchByCategoryName() {
        RequestMediator req = new RequestMediator(new MockRequest());
        BoardstarService service = new BoardstarService(new BgaWebApi(req));
        Stream<Game> games = service.searchByCategoryName("War");
        Assert.assertEquals(0, req.getCount());
        long nGames = games.count();
        Assert.assertEquals(8, req.getCount());
        Assert.assertEquals(59, nGames);
    }

    @Test
    public void testSearchByArtist() {
        RequestMediator req = new RequestMediator(new MockRequest());
        BoardstarService service = new BoardstarService(new BgaWebApi(req));
        Stream<Game> games = service.searchByArtist("Dimitri Bielak");
        Assert.assertEquals(0, req.getCount());
        long nGames = games.count();
        Assert.assertEquals(3, req.getCount());
        Assert.assertEquals(19, nGames);
    }

    @Test
    public void testGetArtists() {
        RequestMediator req = new RequestMediator(new MockRequest());
        BoardstarService service = new BoardstarService(new BgaWebApi(req));
        Stream<Game> games = service.searchByArtist("Dimitri Bielak");
        Assert.assertEquals(0, req.getCount());

        long artistsCount = games.findFirst().get().getArtists().count();

        Assert.assertEquals(1, req.getCount());
        Assert.assertEquals(2, artistsCount);
    }

    @Test
    public void testSearchByArtist_NoGames() {
        RequestMediator req = new RequestMediator(new MockRequest());
        BoardstarService service = new BoardstarService(new BgaWebApi(req));
        Stream<Game> games = service.searchByArtist("NULL");
        Assert.assertEquals(0, req.getCount());
        long nGames = games.count();
        Assert.assertEquals(1, req.getCount());
        Assert.assertEquals(0, nGames);
    }
}
