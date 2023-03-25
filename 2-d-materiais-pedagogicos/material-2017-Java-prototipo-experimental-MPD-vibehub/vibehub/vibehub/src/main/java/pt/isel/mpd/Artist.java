package pt.isel.mpd;

import java.util.Arrays;

public class Artist {

    private final String name;
    private final String bio;
    private final String url;
    private final String[] imagesUri;
    private final String mbid;

    public String getName() { return name; }
    public String getBio() { return bio; }
    public String getUrl() { return url; }
    public String[] getImagesUri() { return imagesUri; }
    public String getMbid() {  return mbid; }

    public Artist(String name, String bio, String url, String[] imagesUri, String mbid) {
        this.name = name;
        this.bio = bio;
        this.url = url;
        this.imagesUri = imagesUri;
        this.mbid = mbid;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "name='" + name + '\'' +
                ", bio='" + bio + '\'' +
                ", url='" + url + '\'' +
                ", imagesUri=" + Arrays.toString(imagesUri) +
                ", mbid='" + mbid + '\'' +
                '}';
    }
}
