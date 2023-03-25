package org.isel.jingle.api;

import com.google.gson.Gson;
import org.isel.jingle.asyncReq.AsyncRequest;
import org.isel.jingle.asyncReq.BaseRequestAsync;

import java.util.Map;

public class LastFmFileApi extends LastFmApi {
    private static final String ARTIST_FILE_PATH = "%s-page-%d";
    private static final String ALBUM_FOR_ARTIST_FILE_PATH = "topAlbumsFor-%s-page-%d";
    private static final String ALBUM_INFO_FILE_PATH = "albumInfo-of-%s";
    private static final String TOP_TRACK_FOR_COUNTRY_PATH = "topTracks-of-%s-page-%d";
    private static final Map<String, String> topAlbumsOfArtistToFileName = registerSupportedTopAlbumsForArtistsMbid();
    private static final Map<String, String> albumMbidToFileName = registerSupportedAlbumsMbid();

    //On call to 'getTopAlbums of artist mbid'
    private static Map<String, String> registerSupportedTopAlbumsForArtistsMbid() {
        return Map.of(
                "fd857293-5ab8-40de-b29e-55a69d4e4d0f", "muse",
                "a36a84af-a72b-436a-9d1a-807362b5a1f5", "empty-albumMBID"
        );
    }

    //On call to 'getAlbum of album mbid'
    private static Map<String, String> registerSupportedAlbumsMbid() {
        return Map.of(
                "aefcf53b-5980-463b-b03d-a6c8da6a9432", "BlackHolesAndRevelations"
        );
    }

    public LastFmFileApi(AsyncRequest request) {
        super(request);
    }

    public LastFmFileApi(AsyncRequest request, Gson gson) {
        super(request, gson);
    }

    public LastFmFileApi() {
        super(new BaseRequestAsync());
    }

    @Override
    protected String getLastFmArtistsPath(String name, int page) {
        return String.format(ARTIST_FILE_PATH, name, page);
    }

    @Override
    protected String getLastFmAlbumsPath(String artistMbid, int page) {
        return String.format(ALBUM_FOR_ARTIST_FILE_PATH, topAlbumsOfArtistToFileName.get(artistMbid), page);
    }

    @Override
    protected String getLastFmAlbumInfoPath(String albumMbid) {
        return String.format(ALBUM_INFO_FILE_PATH, albumMbidToFileName.get(albumMbid));
    }

    @Override
    protected String getLastFmTopTracksPath(String country, int page) {
        return String.format(TOP_TRACK_FOR_COUNTRY_PATH, country, page);
    }
}
