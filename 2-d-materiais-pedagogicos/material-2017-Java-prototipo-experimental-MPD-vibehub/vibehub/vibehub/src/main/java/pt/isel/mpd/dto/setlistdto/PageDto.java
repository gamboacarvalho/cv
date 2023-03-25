package pt.isel.mpd.dto.setlistdto;

public abstract class PageDto {

    private final int itemsPerPage;
    private final int total;

    public int getItemsPerPage() {
        return itemsPerPage;
    }
    public int getTotal() {
        return total;
    }

    public PageDto(int itemsPerPage, int total) {
        this.itemsPerPage = itemsPerPage;
        this.total = total;
    }

}
