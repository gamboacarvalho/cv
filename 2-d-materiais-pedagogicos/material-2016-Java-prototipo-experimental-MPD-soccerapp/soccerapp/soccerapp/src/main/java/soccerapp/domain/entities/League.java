package soccerapp.domain.entities;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class League {

    private final Supplier<CompletableFuture<List<Standing>>> leagueTable;
    private final int id;
    private final String caption;
    private final String league;
    private final String year;
    private final int currentMatchDay;
    private final int numberOfMatchday;
    private final int numberOfTeams;
    private final int numberOfGames;
    private final Date lastUpdate;

    public League(Supplier<CompletableFuture<List<Standing>>> leagueTable, int id, String caption, String league, String year,
                  int currentMatchDay, int numberOfMatchday, int numberOfTeams, int numberOfGames, Date lastUpdate) {
        this.leagueTable = leagueTable;
        this.id = id;
        this.caption = caption;
        this.league = league;
        this.year = year;
        this.currentMatchDay = currentMatchDay;
        this.numberOfMatchday = numberOfMatchday;
        this.numberOfTeams = numberOfTeams;
        this.numberOfGames = numberOfGames;
        this.lastUpdate = lastUpdate;
    }

    public Supplier<CompletableFuture<List<Standing>>> getLeagueTable() {
        return leagueTable;
    }

    public int getId() {
        return id;
    }

    public String getCaption() {
        return caption;
    }

    public String getLeague() {
        return league;
    }

    public String getYear() {
        return year;
    }

    public int getCurrentMatchDay() {
        return currentMatchDay;
    }

    public int getNumberOfMatchday() {
        return numberOfMatchday;
    }

    public int getNumberOfTeams() {
        return numberOfTeams;
    }

    public int getNumberOfGames() {
        return numberOfGames;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    @Override
    public String toString() {
        return "League{" +
                "leagueTable=" + leagueTable +
                ", id=" + id +
                ", caption='" + caption + '\'' +
                ", league='" + league + '\'' +
                ", year='" + year + '\'' +
                ", currentMatchDay=" + currentMatchDay +
                ", numberOfMatchday=" + numberOfMatchday +
                ", numberOfTeams=" + numberOfTeams +
                ", numberOfGames=" + numberOfGames +
                ", lastUpdate=" + lastUpdate +
                '}';
    }
}
