package soccerapp.webapi.model;

/**
 * @author Miguel Gamboa
 *         created on 23-05-2016
 */
public class DtoTeam {

    public static class DtoLinks {
        private final DtoLinksSelf self;
        private final DtoLinksPlayer players;
        public DtoLinks(DtoLinksSelf self, DtoLinksPlayer players) {
            this.self = self;
            this.players = players;
        }

        public DtoLinksSelf getSelf(){ return self; }

        public DtoLinksPlayer getPlayers() {
            return players;
        }

        @Override
        public String toString() {
            return "players = " + players;
        }
    }

    public static class DtoLinksSelf{
        private final String href;
        public DtoLinksSelf(String href){ this.href = href;}
        public String getHref(){ return href;}
    }

    public static class DtoLinksPlayer {
        private final String href;
        public DtoLinksPlayer(String href) {
            this.href = href;
        }

        public String getHref() {
            return href;
        }

        @Override
        public String toString() {
            return href;
        }
    }

    private final DtoLinks _links;
    private final String name;
    private final String code;
    private final String shortName;
    private final String squadMarketValue;
    private final String crestUrl;

    public DtoTeam(DtoLinks links, String name, String code, String shortName, String squadMarketValue, String crestUrl) {
        this._links = links;
        this.name = name;
        this.code = code;
        this.shortName = shortName;
        this.squadMarketValue = squadMarketValue;
        this.crestUrl = crestUrl;
    }

    public DtoLinks get_links() {
        return _links;
    }

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

    @Override
    public String toString() {
        return "DtoTeam{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", shortName='" + shortName + '\'' +
                ", squadMarketValue='" + squadMarketValue + '\'' +
                ", crestUrl='" + crestUrl + '\'' +
                '}';
    }
}
