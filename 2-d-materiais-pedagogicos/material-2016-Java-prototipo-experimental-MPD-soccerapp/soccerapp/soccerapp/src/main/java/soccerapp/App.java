package soccerapp;

import soccerapp.controller.*;
import util.HttpServer;

import static soccerapp.domain.Endpoints.*;

public class App {

    private static class SoccerServer{
        static void run(int port) throws Exception {

            try(SoccerController ctr = new SoccerController()){
                new HttpServer(port)
                        .addHandler(LEAGUES_ENDPOINT.endpoint, req -> new SoccerLeagueController().getLeagues(req))
                        .addHandler(LEAGUE_STANDINGS_ENDPOINT.endpoint + "*",
                                req -> new SoccerLeagueStandingController().getLeagueStandings(req))
                        .addHandler(TEAM_ENDPOINT.endpoint + "*", req -> new SoccerTeamController().getTeam(req))
                        .addHandler(TEAM_PLAYERS_ENDPOINT.endpoint + "*",
                                req -> new SoccerPlayerController().getPlayers(req))
                        .run();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        SoccerServer.run(8888);
    }
}
