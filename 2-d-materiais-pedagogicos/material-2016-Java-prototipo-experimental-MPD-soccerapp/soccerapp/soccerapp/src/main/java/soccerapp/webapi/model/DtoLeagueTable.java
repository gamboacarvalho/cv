package soccerapp.webapi.model;

import java.util.Arrays;

/**
 * @author Miguel Gamboa
 *         created on 23-05-2016
 */
public class DtoLeagueTable {
    private final DtoLeagueTableStanding[] standing;

    public DtoLeagueTable(DtoLeagueTableStanding[] standing) {
        this.standing = standing;
    }

    public DtoLeagueTableStanding[] getStanding() {
        return standing;
    }

    @Override
    public String toString() {
        return "DtoLeagueTable{" +
                "standing=" + Arrays.toString(standing) +
                '}';
    }
}
