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

import java.util.Iterator;
import java.util.List;

import static pt.isel.mpd.util.LazyQueries.*;

public class BoardstarService {
    private final BgaWebApi api;

    public BoardstarService(BgaWebApi api) {
        this.api = api;
    }

    public Iterable<Category> getCategories() {
        return () -> map(
                api.getCategories(),
                this::toCategory
        ).iterator();
    }

    public Iterable<Game> searchByCategory(String categoryId) {
        return map (
                flatMap(
                        takeWhile(
                                map(
                                        iterate(
                                                0,
                                                (page) -> ++page
                                        ),
                                        page -> api.searchByCategories(page, categoryId)
                                ),
                                list -> !list.isEmpty()
                        ),
                        list -> list
                ),
                this::toGame
        );
    }

    public Iterable<Game> searchByArtist(String name) {
        return map (
                flatMap(
                        takeWhile(
                                map(
                                        iterate(
                                                0,
                                                (page) -> ++page
                                        ),
                                        page -> api.searchByArtist(page, name)
                                ),
                                list -> !list.isEmpty()
                        ),
                        list -> list
                ),
                this::toGame
        );
    }

    private Category toCategory(CategoryDto dto) {
        return new Category(
                dto.getId(),
                dto.getName(),
                this.searchByCategory(dto.getId())
        );
    }

    private Game toGame(GameDto dto) {
        return new Game(
                dto.getId(),
                dto.getName(),
                dto.getYearPublished(),
                dto.getDescription(),
                map(dto.getCategories(), this::toCategory),
                map(dto.getArtists(), this::toArtist)
        );
    }

    private Artist toArtist(String name) {
        return new Artist(
                name,
                this.searchByArtist(name)
        );
    }
}
