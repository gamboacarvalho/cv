package org.isel.jingle.api;

import com.google.gson.Gson;
import org.isel.jingle.asyncReq.AsyncRequest;
import org.isel.jingle.dto.albums.AlbumDto;
import org.isel.jingle.dto.albums.AlbumSearchResultDto;
import org.isel.jingle.dto.albums.SingleAlbumContainerDto;
import org.isel.jingle.dto.artists.ArtistDto;
import org.isel.jingle.dto.artists.ArtistSearchInitialResultDto;
import org.isel.jingle.dto.tracks.TrackDto;
import org.isel.jingle.dto.tracks.topTracks.TopTrackDto;
import org.isel.jingle.dto.tracks.topTracks.TopTrackSearchResultDto;

import java.util.concurrent.CompletableFuture;

public abstract class LastFmApi {
    private final AsyncRequest request;
    private final Gson gson;

    //TODO Add exception thrown using CF.exceptionally()
    //If gives valid request (but error), then throw 'invalid request exception'
    //If invalid request (hhtp 405 error),then return null/empty gson object
    public LastFmApi(AsyncRequest request) {
        this(request, new Gson());
    }

    public LastFmApi(AsyncRequest request, Gson gson) {
        this.request = request;
        this.gson = gson;
    }

    public final CompletableFuture<ArtistDto[]> searchArtist(String name, int page) {
        String path = getLastFmArtistsPath(name, page);
        CompletableFuture<String> body = request.getBody(path);

        return body
                .thenApply(str -> gson.fromJson(str, ArtistSearchInitialResultDto.class))
                .thenApply(this::mapToArtistDto)
                .exceptionally(exception -> null);
    }

    public final CompletableFuture<AlbumDto[]> getAlbums(String artistMbid, int page) {
        String path = getLastFmAlbumsPath(artistMbid, page);
        CompletableFuture<String> body = request.getBody(path);

        return body
                .thenApply(str -> gson.fromJson(str, AlbumSearchResultDto.class))
                .thenApply(this::mapToAlbumDto)
                .exceptionally(exception -> null);
    }


    public final CompletableFuture<TrackDto[]> getAlbumInfo(String albumMbid) {
        String path = getLastFmAlbumInfoPath(albumMbid);
        CompletableFuture<String> body = request.getBody(path);

        return body
                .thenApply(str -> gson.fromJson(str, SingleAlbumContainerDto.class))
                .thenApply(this::mapToTrackDto)
                .exceptionally(exception -> null);

    }

    public CompletableFuture<TopTrackDto[]> getTopTracks(String country, int page) {
        String path = getLastFmTopTracksPath(country, page);
        CompletableFuture<String> body = request.getBody(path);

        return body
                .thenApply(str -> gson.fromJson(str, TopTrackSearchResultDto.class))
                .thenApply(this::mapToTopTrackDto)
                .exceptionally(exception -> null);
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


    //-----------------------------------------------------------------------------
    //Set of helper methods for mocking files

    public final CompletableFuture<String> getBodyForArtist(String name, int page) {
        String path = getLastFmArtistsPath(name, page);
        return request.getBody(path);
    }

    public final CompletableFuture<String> getBodyForAlbums(String artistMbid, int page) {
        String path = getLastFmAlbumsPath(artistMbid, page);
        return request.getBody(path);
    }

    public final CompletableFuture<String> getBodyForAlbumInfo(String albumMbid) {
        String path = getLastFmAlbumInfoPath(albumMbid);
        return request.getBody(path);
    }

    public final CompletableFuture<String> getBodyForLastFmTopTracks(String country, int page) {
        String path = getLastFmTopTracksPath(country, page);
        return request.getBody(path);
    }


}
