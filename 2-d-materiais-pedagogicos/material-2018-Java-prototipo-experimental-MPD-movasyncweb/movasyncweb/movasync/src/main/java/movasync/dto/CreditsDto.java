package movasync.dto;

import java.util.Arrays;

/**
 *  Class that contains a CastItemDto array used when we want to retrieve the movie cast
 */

public class CreditsDto {
    private final CastItemDto[] cast;
    private final CrewItemDto[] crew;
    private final int id;

    public CreditsDto(CastItemDto[] cast, CrewItemDto[] crew, int id) {
        this.cast = cast;
        this.crew = crew;
        this.id = id;
    }

    public CastItemDto[] getCast() {
        return cast;
    }

    public CrewItemDto[] getCrew() {
        return crew;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "CreditsDto{" +
                "cast=" + (cast == null ? null : Arrays.asList(cast)) +
                ", crew=" + (crew == null ? null : Arrays.asList(crew)) +
                ", id=" + id +
                '}';
    }
}
