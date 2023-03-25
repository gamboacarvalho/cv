package movasync;/*
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


import movasync.dto.*;
import movasync.model.Credit;
import movasync.model.Movie;
import movasync.model.Person;
import movasync.model.SearchItem;
import util.Queries;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

/**
 * @author Miguel Gamboa
 *         created on 02-03-2017
 */
public class MovService {

    private final MovWebApi movWebApi;
    private final Map<Integer, CompletableFuture<Movie>> movies = new ConcurrentHashMap<>();
    private final Map<Integer, CompletableFuture<List<Credit>>> cast = new ConcurrentHashMap<>();
    private final Map<Integer, CompletableFuture<Person>> actors = new ConcurrentHashMap<>();

    public MovService(MovWebApi movWebApi) {
        this.movWebApi = movWebApi;
    }

    public CompletableFuture<Stream<SearchItem>> search(String name) {
        return movWebApi
                .search(name,1)
                .thenApply(si -> {
                    CompletableFuture<SearchDto> searchDtoCF = CompletableFuture.completedFuture(si);
                    List<CompletableFuture<SearchDto>> l = new LinkedList<>();
                    l.add(searchDtoCF);
                    int i = 2;
                    while (i <= si.getTotal_pages()){
                        CompletableFuture<SearchDto> search = movWebApi.search(name, i++);
                        l.add(search);
                    }

                    return l.stream()
                            .map(CompletableFuture::join)
                            .map(SearchDto::getResults)
                            .map(it -> Stream.of(it).map(this::parseSearchItemDto))
                            .flatMap(it -> it);
                });
    }

    private SearchItem parseSearchItemDto(SearchItemDto dto) {
        return new SearchItem(
                dto.getId(),
                dto.getTitle(),
                dto.getReleaseDate(),
                dto.getVoteAverage(),
                () -> getMovie(dto.getId()));
    }

    public CompletableFuture<Movie> getMovie(int movId) {
        return movies.computeIfAbsent(movId, id -> {
            return movWebApi
                    .getMovie(id)
                    .thenApply(mov -> newMov(mov,id));
        });
    }

    private Movie newMov(MovieDto mov, Integer id) {
        return new Movie(
                mov.getId(),
                mov.getOriginalTitle(),
                mov.getTagline(),
                mov.getOverview(),
                mov.getVoteAverage(),
                mov.getReleaseDate(),
                mov.getPosterPath(),
                () -> getMovieCast(id));
    }

    public CompletableFuture<Stream<Credit>> getMovieCast(int movId) {
        return cast.computeIfAbsent(movId, id ->
                movWebApi
                        .getMovieCredits(movId)
                        .thenApply(movieCredits -> {
                            return map(movieCredits,movId);
                        })
                        .thenApply(it -> it.collect(toList()))
        ).thenApply(Collection::stream);
    }

    private Credit parseCastItemDto(CastItemDto dto, int movieId) {
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

    private Credit parseCrewItemDto(CrewItemDto dto, int movieId) {
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


    public CompletableFuture<Person> getActor(int actorId, String name) {
        return actors.computeIfAbsent(actorId, id -> {
            return movWebApi
                    .getPerson(id)
                    .thenApply( p -> newPerson(p,actorId));
        });
    }

    private Person newPerson(PersonDto p, int actorId) {
        return new Person(
                p.getId(),
                p.getName(),
                p.getPlace_of_birth(),
                p.getBiography(),
                () -> getActorCreditsCast(actorId),
                p.getProfile_path()
        );
    }


    /**
     * For each element of SearchItemDto[] returned by the movWebApi.getPersonCreditsCast(actorId) method apply the
     * parseSearchItemDto function that receive a SearchItemDto and return a SearchItem.
     * @param actorId
     * @return Iterable<SearchItem>
     */
    public CompletableFuture<Stream<SearchItem>> getActorCreditsCast(int actorId) {
        return movWebApi
                .getPersonCreditsCast(actorId)
                .thenApply(searchItemDtos -> Stream.of(searchItemDtos).map(this::parseSearchItemDto));
    }

    public Stream<Credit> map(CreditsDto movieCredits,int movId){
        return Queries.mapDistinct(
                stream(movieCredits.getCast()), stream(movieCredits.getCrew()),
                (it1, it2) -> it1.getId() == it2.getId(),
                (castItemDto -> parseCastItemDto(castItemDto,movId)),
                (crewItemDto -> parseCrewItemDto(crewItemDto,movId)),
                (castItemDto, crewItemDto) -> parseCastNCrewItemDto(castItemDto,crewItemDto,movId)
        );
    }

}
