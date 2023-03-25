package org.isel.boardstar;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import org.isel.boardstar.model.Category;
import org.isel.boardstar.model.Game;
import org.junit.Assert;
import org.junit.Test;
import pt.isel.mpd.util.requests.AsyncHttpRequest;
import pt.isel.mpd.util.requests.AsyncMockRequest;
import pt.isel.mpd.util.requests.AsyncRequestMediator;


public class BgaWebApiTest {

    final int GAMES_LIST_SIZE = 500;

    @Test
    public void testGetCategories() {
        AsyncRequestMediator req = new AsyncRequestMediator(new AsyncMockRequest());
        BoardstarService service = new BoardstarService(new BgaWebApi(req));
        Observable<Category> categories = service.getCategories();
        long count = categories.count().blockingGet();
        Assert.assertEquals(121, count);
    }

    @Test
    public void testSearchByCategory() {
        AsyncRequestMediator req = new AsyncRequestMediator(new AsyncHttpRequest());
        BoardstarService service = new BoardstarService(new BgaWebApi(req));
        Observable<Game> games = service.searchByCategory("85OKv8p5Ow", GAMES_LIST_SIZE);
        Assert.assertEquals(0, req.getCount());
        long count = games.count().blockingGet();
        Assert.assertEquals(12, count);
    }

    @Test
    public void testSearchByCategoryLimited() {
        AsyncRequestMediator req = new AsyncRequestMediator(new AsyncMockRequest());
        BoardstarService service = new BoardstarService(new BgaWebApi(req));
        Observable<Game> games = service.searchByCategory("85OKv8p5Ow", 11);
        long nGames = games.count().blockingGet();
        Assert.assertEquals(11, nGames);
        Assert.assertEquals(2, req.getCount());
    }

    @Test
    public void testSearchByCategoryName() {
        AsyncRequestMediator req = new AsyncRequestMediator(new AsyncMockRequest());
        BoardstarService service = new BoardstarService(new BgaWebApi(req));
        Observable<Game> games = service.searchByCategoryName("War", GAMES_LIST_SIZE);
        long nGames = games.count().blockingGet();
        Assert.assertEquals(59, nGames);
    }

    @Test
    public void testSearchByArtist() {
        AsyncRequestMediator req = new AsyncRequestMediator(new AsyncMockRequest());
        BoardstarService service = new BoardstarService(new BgaWebApi(req));
        Observable<Game> games = service.searchByArtist("Dimitri Bielak", GAMES_LIST_SIZE);
        long nGames = games.count().blockingGet();
        Assert.assertEquals(19, nGames);
    }

    @Test
    public void testGetArtists() {
        AsyncRequestMediator req = new AsyncRequestMediator(new AsyncMockRequest());
        BoardstarService service = new BoardstarService(new BgaWebApi(req));
        Observable<Game> games = service.searchByArtist("Dimitri Bielak", GAMES_LIST_SIZE);
        Assert.assertTrue(
                games.all(
                        game -> game.getArtists().any(
                                artist -> artist.getName().equals("Dimitri Bielak")
                        ).blockingGet()
                ).blockingGet()
        );
    }

    @Test
    public void testSearchByArtist_NoGames() {
        AsyncRequestMediator req = new AsyncRequestMediator(new AsyncMockRequest());
        BoardstarService service = new BoardstarService(new BgaWebApi(req));
        Observable<Game> games = service.searchByArtist("NULL", GAMES_LIST_SIZE);
        long nGames = games.count().blockingGet();
        Assert.assertEquals(0, nGames);
    }

}
