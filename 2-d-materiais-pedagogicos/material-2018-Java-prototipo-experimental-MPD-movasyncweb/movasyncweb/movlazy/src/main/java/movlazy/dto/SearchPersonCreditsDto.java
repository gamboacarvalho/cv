package movlazy.dto;

/**
 * Dto class that contains a SearchItemDto array used when we want to retrieve the person credits cast
 */
public class SearchPersonCreditsDto {
    private final SearchItemDto[] cast;

    public SearchPersonCreditsDto(SearchItemDto[] cast) {
        this.cast = cast;
    }

    public SearchItemDto[] getCast() {
        return cast;
    }
}
