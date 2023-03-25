package util;

/**
 * Dto class that return the number of current results and current number of pages for a movie search
 */

public class TestDto {
    private int total_results;
    private int total_pages;

    public TestDto(int total_results, int total_pages) {
        this.total_results = total_results;
        this.total_pages = total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }
}
