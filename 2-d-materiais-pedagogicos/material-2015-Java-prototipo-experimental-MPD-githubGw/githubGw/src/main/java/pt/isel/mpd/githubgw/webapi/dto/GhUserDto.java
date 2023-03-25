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

import pt.isel.mpd.githubgw.model.IGhRepo;

import java.util.stream.Stream;

/**
 * Created by Miguel Gamboa on 05-06-2015.
 */
public class GhUserDto {

    public final int id;
    public final String login;
    public final String name;
    public final String location;
    public final String company;
    public final String type;

    public GhUserDto(int id, String login, String name, String location, String company, String type) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.location = location;
        this.company = company;
        this.type = type;
    }

    @Override
    public String toString() {
        return "GhUserDto{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", company='" + company + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
