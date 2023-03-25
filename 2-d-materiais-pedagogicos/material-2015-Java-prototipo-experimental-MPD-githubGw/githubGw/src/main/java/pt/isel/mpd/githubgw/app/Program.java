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

package pt.isel.mpd.githubgw.app;

import pt.isel.mpd.githubgw.model.IGhOrg;
import pt.isel.mpd.githubgw.model.IGhRepo;
import pt.isel.mpd.githubgw.model.async.GhOrg;
import pt.isel.mpd.githubgw.model.async.GhRepo;
import pt.isel.mpd.githubgw.model.async.GhServiceAsync;
import pt.isel.mpd.githubgw.webapi.GhApi;

import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.averagingInt;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;

/**
 * Created by Miguel Gamboa on 05-06-2015.
 */
public class Program {

    public static void main(String [] args) throws Exception {
        try (GhApi gw = new GhApi()) {
            gw.getUserOrgs("Ocramius").thenAccept(System.out::println).join();
            gw.getUserInfo("github").thenAccept(System.out::println).join();
            gw.getOrgRepos(9919).thenAccept(System.out::println).join();
            gw.getOrgRepos(9919, 1).thenAccept(System.out::println).join();
            gw.getRepoContributors("zendframework", "zf2").thenAccept(System.out::println).join();
            gw.getRepoContributors("zendframework", "zf2", 1).thenAccept(System.out::println).join();


            GhServiceAsync as = new GhServiceAsync(gw);

            System.out.println(
                    getAverageRepositorySizeByLanguageStr(Stream.of(as.getOrg("github").get())));
        }
    }

    static String getAverageRepositorySizeByLanguageStr(Stream<IGhOrg> organizations) {
        return organizations.flatMap(IGhOrg::getRepos).collect(
                groupingBy(IGhRepo::getLanguage, averagingInt(IGhRepo::getSize)))
                .entrySet().stream().map(es -> es.getKey() + ":" + es.getValue()).collect(joining());
    }

    static void test1() {
        Comparator<GhRepo> cmp1 = comparing(GhRepo::getName);
        Comparator<GhRepo> cmp2 = cmp1.reversed();
    }

    static void test2() {
        Comparator<GhRepo> cmp1 = comparing(GhRepo::getName).andThen(GhRepo::getSize).reversed();
        Comparator<GhRepo> cmp2 = comparing(GhRepo::getName).andThen(GhRepo::getSize).andThen(GhRepo::getId).reversed();
    }


    static <T, R extends Comparable<R>> CmpBuilder<T, R> comparing(Function<T, R> extractor) {
        return null;
    }

    static class CmpBuilder<T, R> implements Comparator<T>{
        public <R2 extends Comparable<R2>> CmpBuilder<T,R> andThen(Function<T, R2> sup) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int compare(T o1, T o2) {
            return 0;
        }

        public CmpBuilder<T,R> reversed() {
            throw new UnsupportedOperationException();
        }
    }
}
