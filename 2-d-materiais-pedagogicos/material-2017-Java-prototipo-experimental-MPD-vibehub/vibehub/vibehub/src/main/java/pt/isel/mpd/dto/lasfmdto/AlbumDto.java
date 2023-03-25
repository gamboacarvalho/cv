package pt.isel.mpd.dto.lasfmdto;

        import com.google.gson.JsonObject;
        import pt.isel.mpd.dto.DtoUtils;

public class AlbumDto {

    private final String title;
    private final String url;
    private final ImageDto[] image;

    public AlbumDto(String title, String url, ImageDto[] image) {
        this.title = title;
        this.url = url;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }
    public String getUrl() {
        return url;
    }
    public ImageDto[] getImage() {
        return image;
    }

    public static AlbumDto fromJson(JsonObject obj) {
        if(obj == null)
            return new AlbumDto("", "", new ImageDto[0]);
        String title = DtoUtils.stringFromJson(obj, "title");
        String url = DtoUtils.stringFromJson(obj, "url");
        ImageDto[] images = DtoUtils.arrayFromJson(obj, "image", ImageDto::fromJson, ImageDto.class);
        return new AlbumDto(title, url, images);
    }

}
