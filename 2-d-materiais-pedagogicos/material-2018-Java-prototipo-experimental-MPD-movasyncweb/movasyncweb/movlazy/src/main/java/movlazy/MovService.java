/*
 * Copyright (c) 2017, Miguel Gamboa
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

import movlazy.dto.*;
import movlazy.model.Person;
import movlazy.model.Credit;
import movlazy.model.Movie;
import movlazy.model.SearchItem;
import util.Cache;
import util.Queries;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

/**
 * @author Miguel Gamboa
 *         created on 02-03-2017
 */
public class MovService {

    private final MovWebApi movWebApi;
    private final Map<Integer, Movie> movies = new HashMap<>();
    private final Map<Integer, Supplier<Stream<Credit>>> cast = new HashMap<>();
    private final Map<Integer, Person> actors = new HashMap<>();

    public MovService(MovWebApi movWebApi) {
        this.movWebApi = movWebApi;
    }

    public Supplier<Stream<SearchItem>> search(String name) {
        return () -> Stream.iterate(1,prev->++prev)
                    .map(page-> movWebApi.search(name,page))
                    .takeWhile(movs -> movs.length != 0)
                    .flatMap(Stream::of)
                    .map(this::parseSearchItemDto);

    }

    private SearchItem parseSearchItemDto(SearchItemDto dto) {
        return new SearchItem(
                dto.getId(),
                dto.getTitle(),
                dto.getReleaseDate(),
                dto.getVoteAverage(),
                () -> getMovie(dto.getId()));
    }

    public Movie getMovie(int movId) {
        return movies.computeIfAbsent(movId, id -> {
            MovieDto mov = movWebApi.getMovie(id);
            return new Movie(
                    mov.getId(),
                    mov.getOriginalTitle(),
                    mov.getTagline(),
                    mov.getOverview(),
                    mov.getVoteAverage(),
                    mov.getReleaseDate(),
                    this.getMovieCast(id));
        });
    }

    public Supplier<Stream<Credit>> getMovieCast(int movId) {
        return cast.computeIfAbsent(movId, id ->
                Cache.of(
                    () -> {
                        CreditsDto movieCredits = movWebApi.getMovieCredits(movId);
                        return Queries.mapDistinct( stream(movieCredits.getCast()), stream(movieCredits.getCrew()),
                                (it1, it2) -> it1.getId() == it2.getId(),
                                (castItemDto -> parseCastItemDto(castItemDto,movId)),
                                (crewItemDto -> parseCrewItemDto(crewItemDto,movId)),
                                (castItemDto, crewItemDto) -> parseCastNCrewItemDto(castItemDto,crewItemDto,movId)
                        );
                    })
        );
    }

    private Credit parseCastItemDto(CastItemDto dto,int movieId) {
        return new Credit(
                dto.getId(),
                movieId,
                dto.getCharacter(),
                null,
                null,
                dto.getName(),
                () -> getActor(dto.getId(),dto.getName())
        );
    }

    private Credit parseCrewItemDto(CrewItemDto dto,int movieId) {
        return new Credit(
                dto.getId(),
                movieId,
                null,
                dto.getDepartment(),
                dto.getJob(),
                dto.getName(),
                () -> getActor(dto.getId(),dto.getName())
        );
    }

    private Credit parseCastNCrewItemDto(CastItemDto c,CrewItemDto cr,int movieId) {
        return new Credit(
                c.getId(),
                movieId,
                c.getCharacter(),
                cr.getDepartment(),
                cr.getJob(),
                c.getName(),
                () -> getActor(c.getId(),c.getName())
        );
    }


    public Person getActor(int actorId, String name) {
        return actors.computeIfAbsent(actorId, id -> {
            PersonDto personDto = movWebApi.getPerson(id);
            return new Person(
                    personDto.getId(),
                    personDto.getName(),
                    personDto.getPlace_of_birth(),
                    personDto.getBiography(),
                    this.getActorCreditsCast(actorId)
            );
        });
    }

    /**
     * For each element of SearchItemDto[] returned by the movWebApi.getPersonCreditsCast(actorId) method apply the
     * parseSearchItemDto function that receive a SearchItemDto and return a SearchItem.
     * @param actorId
     * @return Iterable<SearchItem>
     */
    public Supplier<Stream<SearchItem>> getActorCreditsCast(int actorId) {
        return () -> Stream.of(movWebApi.getPersonCreditsCast(actorId)).map(this::parseSearchItemDto);
                /*map(
                this::parseSearchItemDto,
                of( () -> movWebApi.getPersonCreditsCast(actorId))

        );
        */
    }

}
