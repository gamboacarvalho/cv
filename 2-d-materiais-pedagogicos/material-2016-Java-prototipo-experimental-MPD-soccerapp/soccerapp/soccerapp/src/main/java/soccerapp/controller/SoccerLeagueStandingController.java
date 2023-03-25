package soccerapp.controller;

import soccerapp.cache.Cache;
import soccerapp.domain.CacheFactory;
import soccerapp.domain.Endpoints;
import soccerapp.domain.entities.Standing;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static soccerapp.domain.Endpoints.TEAM_ENDPOINT;
import static soccerapp.domain.Endpoints.TEAM_PLAYERS_ENDPOINT;
import static util.Utils.getIntValueFromRequestUri;
import static util.Utils.getTeamIdFromUri;

public class SoccerLeagueStandingController extends SoccerController {

    private Cache cache = CacheFactory.get(SoccerLeagueStandingController.class);

    @Override
    public void close() throws Exception {
        super.close();
    }

    public String getLeagueStandings(HttpServletRequest req) throws IOException {
        log.info("SoccerController - getLeagueStandings");

        Supplier<CompletableFuture<String>> supplier =
                () -> service.getStandings(getIntValueFromRequestUri(req))
                        .thenApplyAsync(this::requestTeamsAndPlayers)
                        .handleAsync((standingList, throwable) ->
                                applyToView(standingList, leagueTableStandingView, throwable));
        return cache.getOrDefault(req.getRequestURI(), supplier);

    }

    private List<Standing> requestTeamsAndPlayers(Stream<Standing> standingStream) {
        List<Standing> standingList = standingStream.collect(toList());
        standingList.forEach(standing -> {
            cache.getOrDefault(createTeamUri(standing.getTeamHref()), () -> createTeamHtmlView(standing));
        });

        return standingList;
    }

    private String createTeamUri(String path){
        return TEAM_ENDPOINT.endpoint+ getTeamIdFromUri(path);
    }
    private String createTeamPlayersUri(int id){
        return TEAM_PLAYERS_ENDPOINT.endpoint+ id;
    }

    private CompletableFuture<String> createTeamHtmlView(Standing standing){
        return standing.getTeam()
                .thenApplyAsync(team -> {
                    cache.getOrDefault(createTeamPlayersUri(team.getId()), () ->
                            team
                                    .getPlayers()
                                    .handleAsync((playersList, throwable) -> applyToView(playersList,teamPlayersView,throwable)));
                    return team;
                })
                .handleAsync((team, throwable) -> applyToView(team, teamView,throwable));
    }
}
