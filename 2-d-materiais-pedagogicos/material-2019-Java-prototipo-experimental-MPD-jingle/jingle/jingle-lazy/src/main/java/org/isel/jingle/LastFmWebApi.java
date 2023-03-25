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

import com.google.gson.Gson;
import org.isel.jingle.util.req.BaseRequest;
import org.isel.jingle.util.req.HttpRequest;
import org.isel.jingle.util.req.Request;


public class LastFmWebApi extends LastFmApi {
    private static final String LASTFM_HOST = "http://ws.audioscrobbler.com/2.0/";
    private static final String LASTFM_API_KEY = "56bb17cf0d9c392dbb7e24ca955c8cb7";

    private static final String LASTFM_GET_ARTISTS =
            LASTFM_HOST
                    + "?method=artist.search&format=json&artist=%s&page=%d&api_key="
                    + LASTFM_API_KEY;

    private static final String LASTFM_GET_ALBUMS =
            LASTFM_HOST
                    + "?method=artist.gettopalbums&format=json&mbid=%s&page=%d&api_key="
                    + LASTFM_API_KEY;

    private static final String LASTFM_GET_ALBUM_INFO =
            LASTFM_HOST
                    + "?method=album.getinfo&format=json&mbid=%s&api_key="
                    + LASTFM_API_KEY;

    private static final String LASTFM_GET_TOP_TRACKS =
            LASTFM_HOST
                    + "?method=geo.gettoptracks&country=%s&page=%d&format=json&api_key="
                    + LASTFM_API_KEY;


    public LastFmWebApi(Request request) {
        this(request, new Gson());
    }

    public LastFmWebApi(Request request, Gson gson) {
        super(request, gson);
    }

    public LastFmWebApi() {
        super(new BaseRequest(HttpRequest::openStream));
    }


    @Override
    protected String getLastFmArtistsPath(String name, int page) {
        return String.format(LASTFM_GET_ARTISTS, name, page);

    }

    @Override
    protected String getLastFmAlbumsPath(String artistMbid, int page) {
        return String.format(LASTFM_GET_ALBUMS, artistMbid, page);
    }

    @Override
    protected String getLastFmAlbumInfoPath(String albumMbid) {
        return String.format(LASTFM_GET_ALBUM_INFO, albumMbid);
    }

    @Override
    protected String getLastFmTopTracksPath(String country, int page) {
        return String.format(LASTFM_GET_TOP_TRACKS, country, page);
    }
}
