package soccerapp.domain.entities;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Team {

    private final String name;
    private final String code;
    private final String shortName;
    private final String squadMarketValue;
    private final String crestUrl;
    private final CompletableFuture<List<Player>> players;

    private final int id;

    public Team(int id, String name, String code, String shortName, String squadMarketValue, String crestUrl,
                CompletableFuture<List<Player>> players) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.shortName = shortName;
        this.squadMarketValue = squadMarketValue;
        this.crestUrl = crestUrl;
        this.players = players;
    }

    public int getId() { return id; }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getShortName() {
        return shortName;
    }

    public String getSquadMarketValue() {
        return squadMarketValue;
    }

    public String getCrestUrl() {
        return crestUrl;
    }

    public CompletableFuture<List<Player>> getPlayers() {
        return players;
    }

    @Override
    public String toString() {
        return "Team{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", shortName='" + shortName + '\'' +
                ", squadMarketValue='" + squadMarketValue + '\'' +
                ", crestUrl='" + crestUrl + '\'' +
                ", players=" + players +
                '}';
    }
}
