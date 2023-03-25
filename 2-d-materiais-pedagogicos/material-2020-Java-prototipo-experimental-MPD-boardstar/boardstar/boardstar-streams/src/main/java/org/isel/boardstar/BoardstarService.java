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

import org.isel.boardstar.dto.CategoryDto;
import org.isel.boardstar.dto.GameDto;
import org.isel.boardstar.model.Artist;
import org.isel.boardstar.model.Category;
import org.isel.boardstar.model.Game;
import pt.isel.mpd.util.RequestObject;


import java.util.Collection;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static pt.isel.mpd.util.Utils.extractStringValue;
import static pt.isel.mpd.util.streams.StreamUtils.cache;


public class BoardstarService {

    private final BgaWebApi api;

    public BoardstarService(BgaWebApi api) {
        this.api = api;
    }

    public Supplier<Stream<Category>> getCategories() {
        return () ->
                api.getCategories()
                .stream()
                .map(this::toCategory);
    }

    public Supplier<Stream<Category>> getCategoriesBy(Predicate<Category> predicate) {
        return () ->
                api.getCategories()
                        .stream()
                        .map(this::toCategory)
                        .filter(predicate);
    }

    public Stream<Game> searchByCategory(String categoryId) {
        return Stream
                .iterate(0, page -> ++page)
                .map(page -> api.searchByCategories(page, categoryId))
                .takeWhile(list -> !list.isEmpty())
                .flatMap(Collection::stream)
                .map(this::toGame);
    }

    /**
     * RequestObject is a container for all the data needed to make the API requests such as the current page number
     * and the identifier value which can be obtained by passing a getter to the RequestObject.
     */
    public Stream<Game> searchByCategoryName(String name) {
        RequestObject<Stream<Category>,String> requestObject = new RequestObject<>(
                cache(getCategoriesBy((category) -> category.getName().equals(name))),           // Supplier
                (stream) -> extractStringValue(stream.findFirst(), Category::getId, "")  // Value Getter
        );
        return Stream
                .iterate(requestObject, RequestObject::incrPage)
                .map(reqObj ->  api.searchByCategories(reqObj.getPage(),reqObj.getValue()))
                .takeWhile(list -> !list.isEmpty())
                .flatMap(Collection::stream)
                .map(this::toGame);
    }

    public Stream<Game> searchByArtist(String name) {
        return Stream
                .iterate(0, page -> ++page)
                .map(page -> api.searchByArtist(page, name))
                .takeWhile(list -> !list.isEmpty())
                .flatMap(Collection::stream)
                .map(this::toGame);
    }

    private Category toCategory(CategoryDto dto) {
        return new Category(
                dto.getId(),
                dto.getName(),
                () -> this.searchByCategory(dto.getId())
        );
    }

    private Game toGame(GameDto dto) {
        return new Game(
                dto.getId(),
                dto.getName(),
                dto.getYearPublished(),
                dto.getDescription(),
                () -> dto.getCategories().stream().map(this::toCategory),
                () -> dto.getArtists().stream().map(this::toArtist)
        );
    }

    private Artist toArtist(String name) {
        return new Artist(
                name,
                () -> this.searchByArtist(name)
        );
    }
}
