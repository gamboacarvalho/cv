package org.isel.jingle;

import com.google.gson.Gson;
import org.isel.jingle.dto.albums.AlbumDto;
import org.isel.jingle.dto.albums.AlbumSearchResultDto;
import org.isel.jingle.dto.albums.SingleAlbumContainerDto;
import org.isel.jingle.dto.artists.ArtistDto;
import org.isel.jingle.dto.artists.ArtistSearchInitialResultDto;
import org.isel.jingle.dto.tracks.TrackDto;
import org.isel.jingle.dto.tracks.topTracks.TopTrackDto;
import org.isel.jingle.dto.tracks.topTracks.TopTrackSearchResultDto;
import org.isel.jingle.util.req.Request;

public abstract class LastFmApi {
    private final Request request;
    private final Gson gson;

    //TODO Add exception thrown
    LastFmApi(Request request) {
        this(request, new Gson());
    }

    LastFmApi(Request request, Gson gson) {
        this.request = request;
        this.gson = gson;
    }

    public final ArtistDto[] searchArtist(String name, int page) {
        String path = getLastFmArtistsPath(name, page);
        String body = request.getBody(path);

        final ArtistSearchInitialResultDto initialSearchDto = gson.fromJson(body, ArtistSearchInitialResultDto.class);
        return mapToArtistDto(initialSearchDto);
    }


    public final AlbumDto[] getAlbums(String artistMbid, int page) {
        String path = getLastFmAlbumsPath(artistMbid, page);
        String body = request.getBody(path);

        final AlbumSearchResultDto searchResultDto = gson.fromJson(body, AlbumSearchResultDto.class);
        return mapToAlbumDto(searchResultDto);
    }


    public final TrackDto[] getAlbumInfo(String albumMbid) {
        String path = getLastFmAlbumInfoPath(albumMbid);
        String body = request.getBody(path);

        final SingleAlbumContainerDto singleAlbumContainerDto = gson.fromJson(body, SingleAlbumContainerDto.class);
        return mapToTrackDto(singleAlbumContainerDto);
    }

    public TopTrackDto[] getTopTracks(String country, int page) {
        String path = getLastFmTopTracksPath(country, page);
        String body = request.getBody(path);

        final TopTrackSearchResultDto topTrackSearchResultDto = gson.fromJson(body, TopTrackSearchResultDto.class);
        return mapToTopTrackDto(topTrackSearchResultDto);
    }


    private TrackDto[] mapToTrackDto(SingleAlbumContainerDto singleAlbumContainerDto) {
        return singleAlbumContainerDto.getAlbum().getTracks().getTrack();
    }

    private ArtistDto[] mapToArtistDto(ArtistSearchInitialResultDto initialSearchDto) {
        return initialSearchDto.getResults().getArtistContainer().getArtists();
    }

    private AlbumDto[] mapToAlbumDto(AlbumSearchResultDto searchResultDto) {
        return searchResultDto.getAlbumsContainer().getAlbums();
    }

    private TopTrackDto[] mapToTopTrackDto(TopTrackSearchResultDto topTrackSearchResultDto) {
        return topTrackSearchResultDto.getTopTracksContainer().getTracks();
    }

    protected abstract String getLastFmArtistsPath(String name, int page);

    protected abstract String getLastFmAlbumsPath(String artistMbid, int page);

    protected abstract String getLastFmAlbumInfoPath(String albumMbid);

    protected abstract String getLastFmTopTracksPath(String country, int page);
}
