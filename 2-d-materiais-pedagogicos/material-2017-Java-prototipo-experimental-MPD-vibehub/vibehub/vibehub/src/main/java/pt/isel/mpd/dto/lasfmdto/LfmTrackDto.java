package pt.isel.mpd.dto.lasfmdto;

import com.google.gson.JsonObject;
import pt.isel.mpd.dto.DtoUtils;

public class LfmTrackDto {

    private final String name;
    private final String trackUrl;
    private final int duration;
    private final ArtistDto artist;
    private final AlbumDto album;

    public LfmTrackDto(String name, String trackUrl, int duration, ArtistDto artist, AlbumDto album) {
        this.name = name;
        this.trackUrl = trackUrl;
        this.duration = duration;
        this.artist = artist;
        this.album = album;
    }

    public String getName() {
        return name;
    }

    public String getTrackUrl() {
        return trackUrl;
    }

    public int getDuration() {
        return duration;
    }

    public ArtistDto getArtist() {
        return artist;
    }

    public AlbumDto getAlbum() {
        return album;
    }

    public static LfmTrackDto fromJson(JsonObject obj) {
        if(obj == null)
            return new LfmTrackDto("", "", 0,
                                    new ArtistDto("", "", ""),
                                    new AlbumDto("", "", new ImageDto[0]));
        String name = DtoUtils.stringFromJson(obj, "name");
        String url = DtoUtils.stringFromJson(obj, "url");
        int duration = Integer.parseInt(DtoUtils.stringFromJson(obj,"duration"));
        ArtistDto artist = DtoUtils.objectFromJson(obj,"artist", ArtistDto::fromJson);
        AlbumDto album = DtoUtils.objectFromJson(obj,"album", AlbumDto::fromJson);
        return new LfmTrackDto(name, url, duration, artist,album);
    }
}
