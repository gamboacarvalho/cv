package soccerapp.webapi.model;

import java.util.Arrays;

/**
 * @author Miguel Gamboa
 *         created on 02-06-2016
 */
public class DtoPlayersList {
    private final DtoPlayer [] players;

    public DtoPlayersList(DtoPlayer [] players) {
        this.players = players;
    }

    public DtoPlayer[] getPlayers() {
        return players;
    }

    @Override
    public String toString() {
        return "DtoPlayersList{" +
                "players=" + Arrays.toString(players) +
                '}';
    }
}
