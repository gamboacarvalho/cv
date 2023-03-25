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

package pt.isel.mpd.githubgw.model.async;

import pt.isel.mpd.githubgw.model.IGhOrg;
import pt.isel.mpd.githubgw.model.IGhRepo;
import pt.isel.mpd.githubgw.model.IGhUser;
import pt.isel.mpd.githubgw.webapi.GhApi;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * Created by Miguel Gamboa on 08-06-2015.
 */
public class GhServiceAsync implements AutoCloseable{

    private final Map<Integer, IGhOrg> identityOrgs = new HashMap<>();
    private final GhApi gh;

    public GhServiceAsync() {
        this(new GhApi());
    }

    public GhServiceAsync(GhApi ghApi) {
        this.gh = ghApi;
    }

    public CompletableFuture<IGhOrg> getOrg(String login)
    {
        CompletableFuture<IGhOrg> res = gh
                .getUserInfo(login)
                .thenApply(
                        dto -> new GhOrg(dto.id, dto.login, dto.name, dto.location, getRepos(dto.id)));
        res.thenAccept(org -> identityOrgs.put(org.getId(), org));
        return res;
    }

    public CompletableFuture<Stream<IGhRepo>> getRepos(int id)
    {
        return gh
                .getOrgRepos(id)
                .thenApply(
                        l -> l.stream().map(
                                dto -> new GhRepo(
                                        dto,
                                        identityOrgs.get(id),
                                        getContributors(identityOrgs.get(id).getLogin(), dto.name))));
    }

    public CompletableFuture<Stream<IGhUser>> getContributors(String login, String repo) {
        return gh
                .getRepoContributors(login, repo)
                .thenApply(
                        l -> l.stream().map(
                                dto -> null
                        )
                );
    }


    @Override
    public void close() throws Exception {
        if(!gh.isClosed())
            gh.close();
    }

    public boolean isClosed() {
        return gh.isClosed();
    }

}
