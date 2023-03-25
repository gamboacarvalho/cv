package movasync.dto;

/**
 * Dto class that contains a SearchItemDto array and information about the number of results and pages
 * of a searched movie.
 */

public class SearchDto {
    private final SearchItemDto[] results;
    private final int page;
    private final int total_results;
    private final int total_pages;

    public SearchDto(SearchItemDto[] results, int page, int total_results, int total_pages) {
        this.results = results;
        this.page = page;
        this.total_results = total_results;
        this.total_pages = total_pages;
    }

    public SearchItemDto[] getResults() {
        return results;
    }

    public int getPage() {
        return page;
    }

    public int getTotal_results() {
        return total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }
}
