package org.isel.boardstar;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import org.isel.boardstar.model.Category;
import org.isel.boardstar.model.Game;
import pt.isel.mpd.util.DataBox;

import java.util.HashMap;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class Cache {

    private final static Logger LOGGER = Logger.getLogger(Cache.class.getName());
    private final static long EXPIRATION_TIME = 20; //HOURS

    private DataBox<Observable<Category>> categories;
    private HashMap<String,DataBox<Observable<Game>>> gamesByCategory = new HashMap<>();
    private HashMap<String,DataBox<Observable<Game>>> gamesByArtist = new HashMap<>();
    private HashMap<String, DataBox<Maybe<Game>>> games  = new HashMap<>();

    public Cache() { }

    public Observable<Category> getCategories(Supplier<Observable<Category>> supplier) {
        if(categories == null || categories.expired()) {
            LOGGER.info("Categories aren't Cached. Fetching from API.");
            categories = new DataBox<>(supplier.get().cache(), EXPIRATION_TIME);
        }
        return categories.getContent();
    }

    public Observable<Game> getCategoryGames(String categoryId, long size, Supplier<Observable<Game>> supplier) {
        return gamesByCategory.compute(categoryId, (key, value) -> {
            if(value == null || value.expired()) {
                LOGGER.info("Category("+categoryId+") Games aren't Cached. Fetching from API.");
                return new DataBox<>(supplier.get().cache(), EXPIRATION_TIME);
            }
            return value;
        }).getContent();
        // Size may vary!!
    }

    public Observable<Game> getArtistGames(String artistId, long size, Supplier<Observable<Game>> supplier) {
        return gamesByArtist.compute(artistId, (key, value) -> {
            if(value == null || value.expired()) {
                LOGGER.info("Artist("+artistId+") Games aren't Cached. Fetching from API.");
                return new DataBox<>(supplier.get().cache(), EXPIRATION_TIME);
            }
            return value;
        }).getContent();
        // Size may vary!!
    }

    public Maybe<Game> getGame(String gameId, Supplier<Maybe<Game>> supplier) {
        return games.compute(gameId, (key, value) -> {
            if(value == null || value.expired()) {
                LOGGER.info("Game("+gameId+") isn't Cached. Fetching from API.");
                return new DataBox<>(supplier.get().cache(), EXPIRATION_TIME);
            }
            return value;
        }).getContent();
    }
}