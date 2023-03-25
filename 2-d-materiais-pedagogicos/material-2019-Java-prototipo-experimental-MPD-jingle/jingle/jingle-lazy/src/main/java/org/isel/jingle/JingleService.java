/*
 * GNU General Public License v3.0
 *
 * Copyright (c) 2019, Miguel Gamboa (gamboa.pt)
 *
 *   All rights granted under this License are granted for the term of
 * copyright on the Program, and are irrevocable provided the stated
 * conditions are met.  This License explicitly affirms your unlimited
 * permission to run the unmodified Program.  The output from running a
 * covered work is covered by this License only if the output, given its
 * content, constitutes a covered work.  This License acknowledges your
 * rights of fair use or other equivalent, as provided by copyright law.
 *
 *   You may make, run and propagate covered works that you do not
 * convey, without conditions so long as your license otherwise remains
 * in force.  You may convey covered works to others for the sole purpose
 * of having them make modifications exclusively for you, or provide you
 * with facilities for running those works, provided that you comply with
 * the terms of this License in conveying all material for which you do
 * not control copyright.  Those thus making or running the covered works
 * for you must do so exclusively on your behalf, under your direction
 * and control, on terms that prohibit them from making any copies of
 * your copyrighted material outside their relationship with you.
 *
 *   Conveying under any other circumstances is permitted solely under
 * the conditions stated below.  Sublicensing is not allowed; section 10
 * makes it unnecessary.
 *
 */

package org.isel.jingle;

import org.isel.jingle.dto.albums.AlbumDto;
import org.isel.jingle.dto.artists.ArtistDto;
import org.isel.jingle.dto.tracks.TrackDto;
import org.isel.jingle.dto.tracks.topTracks.TopTrackDto;
import org.isel.jingle.model.Album;
import org.isel.jingle.model.Artist;
import org.isel.jingle.model.TopTrack;
import org.isel.jingle.model.Track;
import org.isel.jingle.util.req.BaseRequest;
import org.isel.jingle.util.req.HttpRequest;
import org.isel.jingle.util.streams.StreamUtils;

import java.util.stream.Stream;

public class JingleService {

    private static final Integer STARTING_PAGE = 1;
    private static final Integer MAX_TRACK_RANKS_FOR_COUNTRY = 100;

    private final LastFmWebApi api;

    public JingleService(LastFmWebApi api) {
        this.api = api;
    }

    public JingleService() {
        this(new LastFmWebApi(new BaseRequest(HttpRequest::openStream)));
    }

    public Stream<Artist> searchArtist(String name) {
        return Stream
                .iterate(STARTING_PAGE, page -> page + 1)
                .map(page -> api.searchArtist(name, page))
                .takeWhile(artistDtos -> artistDtos.length != 0)
                .flatMap(Stream::of)
                .map(this::mapToArtist);
    }

    public Stream<TopTrack> getTopTracks(String country) {
        return Stream
                .iterate(STARTING_PAGE, page -> page + 1)
                .map(page -> api.getTopTracks(country, page))
                .takeWhile(topTrackDtos -> topTrackDtos.length != 0)
                .flatMap(Stream::of)
                .map(topTrackDto -> mapToTopTrack(topTrackDto, country));
    }

    public Stream<Track> getTracksRank(String artistMbid, String country) {
        Stream<Track> artistTracks = getArtistTracks(artistMbid);
        Stream<TopTrack> topTracks = getTopTracks(country).limit(MAX_TRACK_RANKS_FOR_COUNTRY);

        TopTrack defaultTopTrack = new TopTrack("", "", country, 0, 0);

        return StreamUtils
                .of(artistTracks)
                .merge(
                        topTracks,
                        this::compareTrackNames,
                        this::addCountryRankToTrack,
                        defaultTopTrack
                );
    }

    private Stream<Album> getAlbums(String artistMbid) {
        return Stream
                .iterate(STARTING_PAGE, page -> page + 1)
                .map(page -> api.getAlbums(artistMbid, page))
                .takeWhile(albumDtos -> albumDtos.length != 0)
                .flatMap(Stream::of)
                .map(this::mapToAlbum);
    }

    /**
     * Obtains album tracks for a given albumMbid.
     * It is possible for an album to have no mbid, rendering it impossible to obtain it's tracks.
     * <p>
     * For an example, see:
     * http://ws.audioscrobbler.com/2.0/?method=artist.gettopalbums&format=json&mbid=a36a84af-a72b-436a-9d1a-807362b5a1f5&page=2&api_key=56bb17cf0d9c392dbb7e24ca955c8cb7
     *
     * @return Empty Iterable, if 'albumMbid' is null. Else an Iterable of tracks
     */
    private Stream<Track> getAlbumTracks(String albumMbid) {
        if (albumMbid == null) {
            return Stream.empty();
        }
        return Stream
                .of(api.getAlbumInfo(albumMbid))
                .map(this::mapToTrack);
    }


    private Stream<Track> getArtistTracks(String artistMbid) {
        return getAlbums(artistMbid)
                .flatMap(Album::getTracks);
    }


    //-----------------------------------------------------------
    //Set of mappers
    private Track mapToTrack(TrackDto trackDto) {
        return new Track(
                trackDto.getName(),
                trackDto.getUrl(),
                trackDto.getDuration()
        );
    }

    private Album mapToAlbum(AlbumDto albumDto) {
        String albumMbid = albumDto.getAlbumMbid();

        return new Album(
                albumDto.getName(),
                albumDto.getPlaycount(),
                albumMbid,
                albumDto.getAlbumUrl(),
                albumDto.getImage()[0].getUrl(),
                () -> getAlbumTracks(albumMbid)
        );
    }

    private Artist mapToArtist(ArtistDto artistDto) {
        String artistMbid = artistDto.getMbid();

        return new Artist(
                artistDto.getName(),
                artistDto.getListeners(),
                artistDto.getMbid(),
                artistDto.getUrl(),
                artistDto.getImage()[0].getUrl(),
                () -> getAlbums(artistMbid),
                () -> getArtistTracks(artistMbid)
        );
    }

    private TopTrack mapToTopTrack(TopTrackDto topTrackDto, String country) {
        return new TopTrack(
                topTrackDto.getName(),
                topTrackDto.getUrl(),
                country,
                topTrackDto.getDuration(),
                topTrackDto.getRank()
        );
    }


    //-----------------------------------------------------------
    //Aux. method reference for method 'getTracksRank'
    private boolean compareTrackNames(Track track, TopTrack topTrack) {
        return track.getName().equalsIgnoreCase(topTrack.getName());
    }

    private Track addCountryRankToTrack(Track track, TopTrack topTrack) {
        track.addCountryRank(topTrack.getCountry(), topTrack.getRank());
        return track;
    }
}
