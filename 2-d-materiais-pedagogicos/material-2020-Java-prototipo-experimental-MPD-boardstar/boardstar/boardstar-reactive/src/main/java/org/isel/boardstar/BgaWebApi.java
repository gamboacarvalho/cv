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

import com.google.gson.Gson;
import org.isel.boardstar.dto.CategoryDto;
import org.isel.boardstar.dto.GameDto;
import org.isel.boardstar.dto.GetCategoriesDto;
import org.isel.boardstar.dto.SearchDto;
import pt.isel.mpd.util.requests.AsyncRequest;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;


public class BgaWebApi {
    private final static Logger LOGGER = Logger.getLogger(BgaWebApi.class.getName());
    public final static long PAGE_SIZE = 30;

    final static String HOST = "https://www.boardgameatlas.com/api/";
    final static String GET_CATEGORIES = "game/categories?";
    final static String SEARCH = "search?";
    final static String CATEGORIES = "categories=%s";
    final static String GAME_NAME = "name=%s";
    final static String ARTIST = "artist=%s";
    final static String LIMIT = "limit="+PAGE_SIZE;
    final static String SKIP = "skip=%s";
    final static String CLIENT_ID = "client_id=%s";

    final static String BGA_KEY;

    private final AsyncRequest req;
    private final Gson gson = new Gson();


    static {
        try(
            InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream("BGA_KEY.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in)))
        {
            BGA_KEY = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Error reading BGA_KEY.txt. Put your Board Game Atlas key within BGA_KEY.txt in resources folder.");
        }
    }

    public BgaWebApi(AsyncRequest req) {
        this.req = req;
    }

    public CompletableFuture<List<CategoryDto>> getCategories() {
        String path = HOST + GET_CATEGORIES +
                String.format(CLIENT_ID, BGA_KEY);

        return req
                .getBody(path)
                .thenApply(reader -> gson.fromJson(reader, GetCategoriesDto.class))
                .thenApply(dto -> dto.getCategories());
    }

    public CompletableFuture<List<GameDto>> searchByCategories(long page, String categoryId) {
        String path = HOST + SEARCH +
                String.format(CATEGORIES, categoryId) +
                '&' + LIMIT +
                '&' + String.format(SKIP, calculateSkip(page)) +
                '&' + String.format(CLIENT_ID, BGA_KEY);

        return req
                .getBody(path)
                .thenApply(reader -> gson.fromJson(reader, SearchDto.class))
                .thenApply(dto -> dto.getGames())
                .whenComplete((list, err) -> {
                    LOGGER.info("REQUEST PATH:" + path);
                    LOGGER.info("Received list with size " + list.size());
                });
    }

    public CompletableFuture<List<GameDto>> searchByGame(String gameName) {
        String path = HOST + SEARCH +
                String.format(GAME_NAME, URLEncoder.encode(gameName, StandardCharsets.UTF_8)) +
                '&' + String.format(CLIENT_ID, BGA_KEY);

        return req
                .getBody(path)
                .thenApply(reader -> gson.fromJson(reader, SearchDto.class))
                .thenApply(dto -> dto.getGames())
                .whenComplete((list, err) -> {
                    LOGGER.info("REQUEST PATH:" + path);
                    LOGGER.info("Received list with size " + list.size());
                });
    }

    public CompletableFuture<List<GameDto>> searchByArtist(long page, String name) {
        String path = HOST + SEARCH +
                String.format(ARTIST, URLEncoder.encode(name, StandardCharsets.UTF_8)) +
                '&' + LIMIT +
                '&' + String.format(SKIP, calculateSkip(page)) +
                '&' + String.format(CLIENT_ID, BGA_KEY);

        return req
                .getBody(path)
                .thenApply(reader -> gson.fromJson(reader, SearchDto.class))
                .thenApply(dto -> dto.getGames());
    }

    private long calculateSkip(long page) {
        return page*PAGE_SIZE;
    }
}
