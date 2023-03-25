/*
 * Copyright (C) 2015 Miguel Gamboa at CCISEL
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package pt.isel.mpd.githubgw.webapi;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ning.http.client.Response;
import pt.isel.mpd.githubgw.webapi.dto.GhUserDto;
import pt.isel.mpd.githubgw.webapi.dto.GhRepoDto;
import pt.isel.mpd.util.HttpGwAsync;
import pt.isel.mpd.util.Pair;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created by Miguel Gamboa on 05-06-2015.
 */
public class GhApi implements AutoCloseable{

    final static String GH_URI = "https://api.github.com";
    final static String GH_VERSION = "application/vnd.github.v3+json";
    final static String GH_TOKEN = "token d36f819ad61e05cd79e39a612ea487ed02e8b8e9";
    final static Pair<String, String>[] GH_HEADERS;

    static{
        GH_HEADERS = new Pair[2];
        GH_HEADERS[0] = new Pair<String, String>("Accept", GH_VERSION);
        GH_HEADERS[1] = new Pair<String, String>("Authorization", GH_TOKEN);
    }

    private final HttpGwAsync httpGw;
    private final Gson reader;

    public GhApi() {
        this(new HttpGwAsync(), new Gson());
    }

    public GhApi(HttpGwAsync httpGw) {
        this(httpGw, new Gson());
    }

    public GhApi(HttpGwAsync httpGw, Gson reader) {
        this.httpGw = httpGw;
        this.reader = reader;
    }

    public CompletableFuture<GhUserDto> getUserInfo(String login){
        String uri = GH_URI + "/users/" + login;
        return httpGw.getDataAsync(uri,GH_HEADERS).thenApply(r -> jsonTo(r, GhUserDto.class));
    }

    public CompletableFuture<List<GhRepoDto>> getOrgRepos(int id){
        return getOrgRepos(id, 1);
    }

    public CompletableFuture<List<GhRepoDto>> getOrgRepos(int id, int page){
        String uri = GH_URI + "/organizations/" + id + "/repos?page=" + page;
        Type listType = new TypeToken<List<GhRepoDto>>(){}.getType();
        return httpGw.getDataAsync(uri, GH_HEADERS).thenApply(r -> jsonTo(r, listType));
    }

    public CompletableFuture<List<GhUserDto>> getRepoContributors(String login, String repo){
        return getRepoContributors(login, repo, 1);
    }

    public CompletableFuture<List<GhUserDto>> getRepoContributors(String login, String repo, int page){
        String uri = GH_URI + "/repos/" + login + "/" + repo + "/contributors?page=" + page;
        Type listType = new TypeToken<List<GhUserDto>>(){}.getType();
        return httpGw.getDataAsync(uri, GH_HEADERS).thenApply(r -> jsonTo(r, listType));
    }

    public CompletableFuture<List<GhUserDto>> getUserOrgs(String login){
        String uri = GH_URI + "/users/" + login + "/orgs";
        Type listType = new TypeToken<List<GhUserDto>>(){}.getType();
        return httpGw.getDataAsync(uri, GH_HEADERS).thenApply(r -> jsonTo(r, listType));
    }

    @Override
    public void close() throws Exception {
        if(!httpGw.isClosed())
            httpGw.close();
    }

    public boolean isClosed() {
        return httpGw.isClosed();
    }

    private <T> T jsonTo(Response resp, Class<T> destKlass){
        try {
            return reader.fromJson(resp.getResponseBody(), destKlass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T jsonTo(Response resp, Type destType){
        try {
            return reader.fromJson(resp.getResponseBody(), destType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
