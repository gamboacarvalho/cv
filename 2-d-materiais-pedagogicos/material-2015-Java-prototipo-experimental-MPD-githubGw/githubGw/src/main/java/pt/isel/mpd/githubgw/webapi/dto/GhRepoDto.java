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

package pt.isel.mpd.githubgw.webapi.dto;

import pt.isel.mpd.githubgw.model.IGhOrg;
import pt.isel.mpd.githubgw.model.IGhUser;

import java.util.stream.Stream;

/**
 * Created by Miguel Gamboa on 05-06-2015.
 */
public class GhRepoDto {

    public final int id;
    public final String name;
    public final String full_name;
    public final String description;
    public final int size;
    public final int stargazers_count;
    public final int watchers_count;
    public final String language;

    public GhRepoDto(
            int id,
            String name,
            String full_name,
            String description,
            int size,
            int stargazers_count,
            int watchers_count,
            String language)
    {
        this.id = id;
        this.name = name;
        this.full_name = full_name;
        this.description = description;
        this.size = size;
        this.stargazers_count = stargazers_count;
        this.watchers_count = watchers_count;
        this.language = language;
    }

    @Override
    public String toString() {
        return "GhRepoDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", full_name='" + full_name + '\'' +
                ", description='" + description + '\'' +
                ", size=" + size +
                ", stargazers_count=" + stargazers_count +
                ", watchers_count=" + watchers_count +
                ", language='" + language + '\'' +
                '}';
    }
}
