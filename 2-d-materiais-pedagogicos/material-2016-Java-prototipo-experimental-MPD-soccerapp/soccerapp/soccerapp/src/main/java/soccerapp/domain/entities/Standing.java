package soccerapp.domain.entities;

import util.Utils;

import java.util.concurrent.CompletableFuture;

public class Standing {

    private final CompletableFuture<Team> team;
    private final int position;
    private final String teamName;
    private final int playedGames;
    private final int points;
    private final int goals;
    private final int wins;
    private final int draws;
    private final int losses;
    private final String teamHref;


    public Standing(CompletableFuture<Team> team, int position, String teamName, int playedGames, int points,
                    int goals, int wins, int draws, int losses, String teamHref) {
        this.team = team;
        this.position = position;
        this.teamName = teamName;
        this.playedGames = playedGames;
        this.points = points;
        this.goals = goals;
        this.wins = wins;
        this.draws = draws;
        this.losses = losses;
        //this.teamHref = teamHref;
        this.teamHref = "/soccerapp/teams/"+ Utils.getTeamIdFromUri(teamHref);
    }

    public CompletableFuture<Team> getTeam() {
        return team;
    }

    public int getPosition() {
        return position;
    }

    public String getTeamName() {
        return teamName;
    }

    public int getPlayedGames() {
        return playedGames;
    }

    public int getPoints() {
        return points;
    }

    public int getGoals() {
        return goals;
    }

    public int getWins() {
        return wins;
    }

    public int getDraws() {
        return draws;
    }

    public int getLosses() {
        return losses;
    }

    public String getTeamHref(){ return teamHref; }


    @Override
    public String toString() {
        return "Standing{" +
                "team=" + team +
                ", position=" + position +
                ", teamName='" + teamName + '\'' +
                ", playedGames=" + playedGames +
                ", points=" + points +
                ", goals=" + goals +
                ", wins=" + wins +
                ", draws=" + draws +
                ", losses=" + losses +
                '}';
    }

    public int getTeamId() {
        return Integer.valueOf(teamHref.substring(teamHref.lastIndexOf('/') + 1));

    }
}
