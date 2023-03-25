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
import pt.isel.mpd.githubgw.webapi.dto.GhRepoDto;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Stream;

/**
 * Created by Miguel Gamboa on 08-06-2015.
 */
public class GhRepo implements IGhRepo {
    final int id;
    final String name;
    final String fullName;
    final String description;
    final int size;
    final int stargazersCount;
    final int watchersCount;
    final String language;
    final IGhOrg owner;
    final Future<Stream<IGhUser>> contributors;

    public GhRepo(int id, String name, String fullName, String description, int size, int stargazersCount, int watchersCount, String language, IGhOrg owner, Future<Stream<IGhUser>> contributors) {
        this.id = id;
        this.name = name;
        this.fullName = fullName;
        this.description = description;
        this.size = size;
        this.stargazersCount = stargazersCount;
        this.watchersCount = watchersCount;
        this.language = language;
        this.owner = owner;
        this.contributors = contributors;
    }

    public GhRepo(GhRepoDto dto, IGhOrg owner, Future<Stream<IGhUser>> contributors) {
        this(
                dto.id,
                dto.name,
                dto.full_name,
                dto.description,
                dto.size,
                dto.stargazers_count,
                dto.watchers_count,
                dto.language,
                owner,
                contributors);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getFullName() {
        return fullName;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public int getStargazersCount() {
        return stargazersCount;
    }

    @Override
    public int getWatchersCount() {
        return watchersCount;
    }

    @Override
    public String getLanguage() {
        return language;
    }

    @Override
    public IGhOrg getOwner() {
        return owner;
    }

    @Override
    public Stream<IGhUser> getContributors() {
        try {
            return contributors.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
