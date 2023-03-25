package soccerapp.domain.service;

import soccerapp.domain.entities.League;
import soccerapp.domain.entities.Player;
import soccerapp.domain.entities.Standing;
import soccerapp.domain.entities.Team;
import soccerapp.webapi.SoccerWebApi;
import soccerapp.webapi.model.*;
import util.Utils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class SoccerService implements AutoCloseable {

    private final SoccerWebApi api = new SoccerWebApi();

    @Override
    public void close() throws Exception {
        if(!api.isClosed()) api.close();
    }

    public boolean isClosed() {
        return api.isClosed();
    }

    public CompletableFuture<Stream<League>> getLeagues(){
        return api.getLeagues()
                .thenApplyAsync(Arrays::stream)
                .thenApplyAsync(stream -> stream.map(this::leagueDtoToDomain));
    }

    public CompletableFuture<Team> getTeam(int id){
        return api.getTeam(id)
                .thenApplyAsync(this::teamDtoToDomain);
    }

    private League leagueDtoToDomain(DtoLeague dtoLeague) {
        Supplier<CompletableFuture<List<Standing>>> standingSupplier = ()->
                getStandings(dtoLeague.getId())
                    .thenApplyAsync(standingStream -> standingStream.collect(toList()));
        return new League(standingSupplier, dtoLeague.getId(), dtoLeague.getCaption(),
                dtoLeague.getLeague(), dtoLeague.getYear(), dtoLeague.getCurrentMatchday(),
                dtoLeague.getNumberOfMatchdays() , dtoLeague.getNumberOfTeams(),
                dtoLeague.getNumberOfGames(), dtoLeague.getLastUpdated());
    }

    public CompletableFuture<Stream<Standing>> getStandings(int id) {
        CompletableFuture<DtoLeagueTable> leagueTable = api.getLeagueTable(id);
        return leagueTable
                .thenApplyAsync(dtoLeagueTable -> standingsDtoArrayToDomain(dtoLeagueTable.getStanding()));
    }

    private Stream<Standing> standingsDtoArrayToDomain(DtoLeagueTableStanding[] arr){
        List<Standing> standings = new LinkedList<>();
        for (DtoLeagueTableStanding dto : arr) {
            standings.add(new Standing(getTeams(api.getTeam(dto.get_links().getTeam().getHref())),
                    dto.getPosition(), dto.getTeamName(), dto.getPlayedGames(), dto.getPoints(), dto.getGoals(),
                    dto.getWins(), dto.getDraws(), dto.getLosses(), dto.get_links().getTeam().getHref()));
        }
        return standings.stream();
    }

    private CompletableFuture<Team> getTeams(CompletableFuture<DtoTeam> team) {
        return team.thenApplyAsync(this::teamDtoToDomain);
    }

    public CompletableFuture<List<Player>> getPlayers(int id) {
        return api.getPlayers(id)
                    .thenApplyAsync(dto -> playerDtoArrayToDomain(dto.getPlayers()));
    }

    private Team teamDtoToDomain(DtoTeam dto){
        int teamId = Utils.getTeamIdFromUri(dto.get_links().getSelf().getHref());
        return new Team(teamId,dto.getName(),dto.getCode(),dto.getShortName(),dto.getSquadMarketValue(),
                        dto.getCrestUrl(), getPlayers(teamId));
    }

    private List<Player> playerDtoArrayToDomain(DtoPlayer[] players) {
        List<Player> playersList = new LinkedList<>();
        for (DtoPlayer p : players) {
            playersList.add(new Player(p.getName(),p.getPosition(),p.getJerseyNumber(),p.getDateOfBirth(),
                            p.getNationality(),p.getContractUntil(),p.getMarketValue()));
        }
        return playersList;
    }
}
