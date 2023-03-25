package soccerapp.domain.entities;

public class Player {

    private final String name;
    private final String position;
    private final int jerseyNumber;
    private final String dateOfBirth;
    private final String nationality;
    private final String contractUntil;
    private final String marketValue;

    public Player(String name, String position, int jerseyNumber, String dateOfBirth, String nationality,
                  String contractUntil, String marketValue) {
        this.name = name;
        this.position = position;
        this.jerseyNumber = jerseyNumber;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.contractUntil = contractUntil;
        this.marketValue = marketValue;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public int getJerseyNumber() {
        return jerseyNumber;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getNationality() {
        return nationality;
    }

    public String getContractUntil() {
        return contractUntil;
    }

    public String getMarketValue() {
        return marketValue;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", position='" + position + '\'' +
                ", jerseyNumber=" + jerseyNumber +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", nationality='" + nationality + '\'' +
                ", contractUntil='" + contractUntil + '\'' +
                ", marketValue='" + marketValue + '\'' +
                '}';
    }
}
