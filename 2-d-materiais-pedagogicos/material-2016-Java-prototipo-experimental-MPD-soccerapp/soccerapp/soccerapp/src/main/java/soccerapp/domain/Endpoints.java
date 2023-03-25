package soccerapp.domain;

public enum Endpoints {
    LEAGUES_ENDPOINT("/soccerapp/leagues"),
    LEAGUE_STANDINGS_ENDPOINT("/soccerapp/leagues/"),
    TEAM_ENDPOINT("/soccerapp/teams/"),
    TEAM_PLAYERS_ENDPOINT("/soccerapp/players/");

    public String endpoint;

    Endpoints(String endpoint){
        this.endpoint = endpoint;
    }

}
