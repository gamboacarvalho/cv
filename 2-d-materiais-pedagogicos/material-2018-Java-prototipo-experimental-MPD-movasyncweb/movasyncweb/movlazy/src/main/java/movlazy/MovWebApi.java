/*
 * Copyright (c) 2018 Miguel Gamboa
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package movlazy;

import com.google.gson.Gson;
import movlazy.dto.*;
import util.IRequest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author Miguel Gamboa
 *         created on 16-02-2017
 */

public class MovWebApi {
    /**
     * Constants
     *
     * To format messages URLs use {@link java.text.MessageFormat#format(String, Object...)} method.
     */
    private static final String MOVIE_DB_HOST = "https://api.themoviedb.org/3/";
    private static final String MOVIE_DB_SEARCH = "search/movie?api_key={0}&query={1}&page={2}";
    private static final String MOVIE_DB_MOVIE = "movie/{1}?api_key={0}";
    private static final String MOVIE_DB_MOVIE_CREDITS = "movie/{1}/credits?api_key={0}";
    private static final String MOVIE_DB_PERSON = "person/{1}?api_key={0}";
    private static final String MOVIE_DB_PERSON_CREDITS = "person/{1}/movie_credits?api_key={0}";
    private static final String API_KEY = "3206de5c5e8ebe8b47eca707e469e394";

    private final IRequest req;
    private final Gson gson = new Gson();

    /*
     * Constructors
     */
    public MovWebApi(IRequest req) {
        this.req = req;
    }

    /**
     * E.g. https://api.themoviedb.org/3/search/movie?api_key=3206de5c5e8ebe8b47eca707e469e394&query=war+games
     */
    public SearchItemDto[] search(String title, int page) {
        String path = MOVIE_DB_HOST +
                MessageFormat.format(MOVIE_DB_SEARCH, API_KEY,
                        title.replaceAll(" ","+"),
                        Integer.toString(page)
                );

        Stream<String> lines = inputStreamToStream( () -> req.getBody(path) );

        String json = lines.reduce( "", (prev, curr) -> prev + curr);

        SearchDto searchDto = gson.fromJson(json, SearchDto.class);

        return searchDto.getResults();
    }

    /**
     * E.g. https://api.themoviedb.org/3/movie/860?api_key=3206de5c5e8ebe8b47eca707e469e394
     */
    public MovieDto getMovie(int id) {

        String path = MOVIE_DB_HOST +
                MessageFormat.format(MOVIE_DB_MOVIE, API_KEY, Integer.toString(id));


        Stream<String> lines = inputStreamToStream( () -> req.getBody(path) );

        String json = lines.reduce( "", (prev, curr) -> prev + curr);

        return gson.fromJson( json , MovieDto.class);

    }

    /**
     * E.g. https://api.themoviedb.org/3/movie/860/credits?api_key=3206de5c5e8ebe8b47eca707e469e394
     */
    public CreditsDto getMovieCredits(int movieId) {
        String path = MOVIE_DB_HOST +
                MessageFormat.format(MOVIE_DB_MOVIE_CREDITS, API_KEY, Integer.toString(movieId));

        Stream<String> lines = inputStreamToStream( () -> req.getBody(path) );

        String json = lines.reduce( "", (prev, curr) -> prev + curr);

        return gson.fromJson(json, CreditsDto.class);
    }

    /**
     * E.g. https://api.themoviedb.org/3/person/4756?api_key=3206de5c5e8ebe8b47eca707e469e394
     */
    public PersonDto getPerson(int personId) {

        String path = MOVIE_DB_HOST +
                MessageFormat.format(MOVIE_DB_PERSON, API_KEY, Integer.toString(personId));

        Stream<String> lines = inputStreamToStream( () -> req.getBody(path) );

        String json = lines.reduce("", (prev, curr) -> prev + curr);

        return gson.fromJson( json , PersonDto.class);
    }

    /**
     * E.g. https://api.themoviedb.org/3/person/4756/movie_credits?api_key=3206de5c5e8ebe8b47eca707e469e394
     */
    public SearchItemDto[] getPersonCreditsCast(int personId) {

        String path = MOVIE_DB_HOST +
                MessageFormat.format(MOVIE_DB_PERSON_CREDITS, API_KEY, Integer.toString(personId));

        Stream<String> lines = inputStreamToStream( () -> req.getBody(path) );

        String json = lines.reduce("", (prev, curr) -> prev + curr);

        SearchPersonCreditsDto searchPersonCreditsDto = gson.fromJson(json, SearchPersonCreditsDto.class);

        return searchPersonCreditsDto.getCast();
    }


    /***********         AUXILIAR METHODS      *************/
    private Stream<String> inputStreamToStream(Supplier<InputStream> supplier) {
        return new BufferedReader(new InputStreamReader(supplier.get())).lines();
    }
}