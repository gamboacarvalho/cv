/*
 * GNU General Public License v3.0
 *
 * Copyright (c) 2020, Miguel Gamboa (gamboa.pt)
 *
 *   All rights granted under this License are granted for the term of
 * copyright on the Program, and are irrevocable provided the stated
 * conditions are met.  This License explicitly affirms your unlimited
 * permission to run the unmodified Program.  The output from running a
 * covered work is covered by this License only if the output, given its
 * content, constitutes a covered work.  This License acknowledges your
 * rights of fair use or other equivalent, as provided by copyright law.
 *
 *   You may make, run and propagate covered works that you do not
 * convey, without conditions so long as your license otherwise remains
 * in force.  You may convey covered works to others for the sole purpose
 * of having them make modifications exclusively for you, or provide you
 * with facilities for running those works, provided that you comply with
 * the terms of this License in conveying all material for which you do
 * not control copyright.  Those thus making or running the covered works
 * for you must do so exclusively on your behalf, under your direction
 * and control, on terms that prohibit them from making any copies of
 * your copyrighted material outside their relationship with you.
 *
 *   Conveying under any other circumstances is permitted solely under
 * the conditions stated below.  Sublicensing is not allowed; section 10
 * makes it unnecessary.
 */

package org.isel.boardstar;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Predicate;
import io.reactivex.rxjava3.functions.Supplier;
import org.isel.boardstar.dto.CategoryDto;
import org.isel.boardstar.dto.GameDto;
import org.isel.boardstar.model.Artist;
import org.isel.boardstar.model.Category;
import org.isel.boardstar.model.Game;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static org.isel.boardstar.BgaWebApi.PAGE_SIZE;

public class BoardstarService {

    private final static Logger LOGGER = Logger.getLogger(BoardstarService.class.getName());

    private final BgaWebApi api;
    private final Cache cache;

    public BoardstarService(BgaWebApi api) {
        this.api = api;
        this.cache = new Cache();
    }

    public Observable<Category> getCategories() {
        return cache.getCategories(() ->
                fromColdCF(api::getCategories)
                        .flatMap(Observable::fromIterable)
                        .map(this::toCategory));
    }

    public Observable<Category> getCategoriesBy(Predicate<Category> predicate) {
        return getCategories().filter(predicate);
    }

    public Observable<Game> searchByCategory(String categoryId, long size) {
        return cache.getCategoryGames(categoryId, size, () ->
                Observable
                        .intervalRange(0, size/PAGE_SIZE + 1, 0, 100, TimeUnit.MILLISECONDS)
                        .map(page -> api.searchByCategories(page, categoryId))
                        .flatMap(BoardstarService::fromCF)
                        .flatMap(Observable::fromIterable)
                        .take(size)
                        .map(this::toGame));
    }

    public Observable<Game> searchByCategoryName(String name, long size) {
        return getCategoriesBy((category) -> category.getName().equals(name))
                .singleElement()
                .flatMapObservable(category -> searchByCategory(category.getId(), size));
    }

    public Maybe<Game> searchByGame(String gameName) {
        return cache.getGame(gameName, () ->
                fromColdCF(() -> api.searchByGame(gameName))
                        .flatMap(Observable::fromIterable)
                        .map(this::toGame)
                        .filter(game -> game.getName().equalsIgnoreCase(gameName))
                        .firstElement());
    }

    public Observable<Game> searchByArtist(String name, long size) {
        return cache.getArtistGames(name, size, () ->
                Observable
                        .intervalRange(0, size/PAGE_SIZE + 1, 0, 100, TimeUnit.MILLISECONDS)
                        .map(page -> api.searchByArtist(page, name))
                        .flatMap(BoardstarService::fromCF)
                        .flatMap(Observable::fromIterable)
                        .take(size)
                        .map(this::toGame));
    }

    private Category toCategory(CategoryDto dto) {
        return new Category(
                dto.getId(),
                dto.getName(),
                (size) -> this.searchByCategory(dto.getId(), size)
        );
    }

    private Game toGame(GameDto dto) {
        return new Game(
                dto.getId(),
                dto.getName(),
                dto.getYearPublished(),
                dto.getDescription(),
                Observable.fromIterable(dto.getCategories()).map(this::toCategory),
                Observable.fromIterable(dto.getArtists()).map(this::toArtist)
        );
    }

    private Artist toArtist(String name) {
        return new Artist(
                name,
                (size) -> this.searchByArtist(name, size)
        );
    }


    private static <T> Observable<T> fromCF(CompletableFuture<T> cf) {
        return Observable.create(subscriber -> {
            cf
                    .thenAccept(item -> {
                        subscriber.onNext(item);
                        subscriber.onComplete();
                    })
                    .exceptionally(err -> {
                        subscriber.onError(err);
                        return null;
                    });
        });
    }

    private static <T> Observable<T> fromColdCF(Supplier<CompletableFuture<T>> supplier) {
        return Observable.create(subscriber -> {
            supplier.get()
                    .thenAccept(item -> {
                        subscriber.onNext(item);
                        subscriber.onComplete();
                    })
                    .exceptionally(err -> {
                        subscriber.onError(err);
                        return null;
                    });
        });
    }
}

