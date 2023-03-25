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
package pt.isel.mpd.githubgw.model.async.test;

import org.junit.Test;
import pt.isel.mpd.githubgw.model.IGhOrg;
import pt.isel.mpd.githubgw.model.IGhRepo;
import pt.isel.mpd.githubgw.model.IGhUser;
import pt.isel.mpd.githubgw.model.async.GhServiceAsync;
import pt.isel.mpd.githubgw.webapi.GhApi;
import pt.isel.mpd.util.HttpGwAsync;

import java.util.List;
import java.util.concurrent.Future;

import static java.util.stream.Collectors.toList;
import static junit.framework.Assert.assertEquals;

/**
 * Created by Miguel Gamboa on 08-06-2015.
 */
public class GhAsyncTest {

    @Test
    public void test_gh_async_service_get_repos_from_organization() throws Exception {
        try(HttpGwAsync httpGw = new HttpGwAsync ()) {
            try (GhServiceAsync gh = new GhServiceAsync(new GhApi(httpGw))) {
                Future<IGhOrg> org = gh.getOrg("zendframework");
                assertEquals(1, httpGw.getNrOfRequests()); // 1.a One request through GhApi.getUserInfo
                assertEquals(0, httpGw.getNrOfResponses());

                org.get(); // Wait for Response
                assertEquals(1, httpGw.getNrOfResponses()); // 1.b One response received for GhApi.getUserInfo
                assertEquals(2, httpGw.getNrOfRequests());  // 2.a One request through GhApi.getOrgRepos

                org.get().getRepos(); // -> Wait for Stream<IGhRepo>
                assertEquals(2, httpGw.getNrOfResponses()); // 2.b One response received for GhApi.getOrgRepos

                /*
                 * 3.a One request through GhApi.getRepoContributors
                 * per GhRepo object instantiated.
                 */
                List<IGhRepo> repos = org.get().getRepos().limit(30).collect(toList());
                assertEquals(2 + repos.size(), httpGw.getNrOfRequests());
                assertEquals(2, httpGw.getNrOfResponses());

                /*
                 * 3.b Wait for responses from GhApi.getRepoContributors
                 */
                repos.stream().map(repo -> repo.getContributors()).collect(toList()); // List<Stream<IGhUser>>
                assertEquals(2 + repos.size(), httpGw.getNrOfResponses());

                /*
                 * 3.c NO requests through GhApi.getRepoContributors -- already cached
                 */
                org.get().getRepos().limit(30).map(repo -> repo.getContributors()).collect(toList()); // List<Stream<IGhUser>>
                assertEquals(2 + repos.size(), httpGw.getNrOfRequests());
                assertEquals(2 + repos.size(), httpGw.getNrOfResponses());

                /*
                 * 3.d Get one more item to jump to next page.
                 * One request through GhApi.getOrgRepos(id, page)
                 */
                repos = org.get().getRepos().limit(31).collect(toList());
                assertEquals(3 + repos.size(), httpGw.getNrOfRequests());
                assertEquals(2 + repos.size(), httpGw.getNrOfResponses());
            }
        }
    }

    @Test
    public void test_gh_async_service_count_repos() throws Exception {
        try (GhServiceAsync gh = new GhServiceAsync()) {
            assertEquals(107, gh.getOrg("zendframework").get().getRepos().count());
        }
    }

    @Test
    public void test_gh_async_service_cyclic_ref_over_organizations() throws Exception {
        try(HttpGwAsync httpGw = new HttpGwAsync ()) {
            try (GhServiceAsync gh = new GhServiceAsync(new GhApi(httpGw))) {

                IGhUser user = gh.getOrg("zendframework")
                        .get()
                        .getRepos()
                        .findFirst()
                        .get()
                        .getContributors()
                        .findFirst()
                        .get();
                assertEquals("weierophinney", user.getLogin());
                int nrOfReqs = httpGw.getNrOfRequests();
                assertEquals(4, user.getOrgs().count());
                /*
                 * 3 more requests.
                 * The zendframework organization was already loaded and
                 * fetched from the identity map.
                 */
                assertEquals(3 + nrOfReqs, httpGw.getNrOfRequests());
            }
        }
    }
}
