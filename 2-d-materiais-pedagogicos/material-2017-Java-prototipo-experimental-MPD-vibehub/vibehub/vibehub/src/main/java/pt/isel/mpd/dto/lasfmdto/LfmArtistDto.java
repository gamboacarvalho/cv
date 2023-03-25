package pt.isel.mpd.dto.lasfmdto;

import com.google.gson.JsonObject;
import pt.isel.mpd.dto.DtoUtils;

public class LfmArtistDto {

    private final String name;
    private final String mbid;
    private final String url;
    private final BioDto bio;
    private final ImageDto[] image;

    public LfmArtistDto(String name, String mbid, String url, BioDto bio, ImageDto[] image) {
        this.name = name;
        this.mbid = mbid;
        this.url = url;
        this.bio = bio;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getMbid() {
        return mbid;
    }

    public String getUrl() {
        return url;
    }

    public ImageDto[] getImage() {
        return image;
    }

    public BioDto getBio() {
        return bio;
    }

    public static LfmArtistDto fromJson(JsonObject obj) {
        if(obj == null)
            return new LfmArtistDto("", "", "",
                                    new BioDto(""),
                                    new ImageDto[0]);
        String name = DtoUtils.stringFromJson(obj, "name");
        String mbid = DtoUtils.stringFromJson(obj, "mbid");
        String url = DtoUtils.stringFromJson(obj, "url");
        BioDto bio = DtoUtils.objectFromJson(obj, "bio", BioDto::fromJson);
        ImageDto[] images = DtoUtils.arrayFromJson(obj, "image", ImageDto::fromJson, ImageDto.class);
        return new LfmArtistDto(name, mbid, url, bio, images);
    }
}
