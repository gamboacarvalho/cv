package movasync.dto;
/*
 * Copyright (c) 2018 Miguel Gamboa
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


/**
 * @author Miguel Gamboa
 *         created on 04-08-2017
 */
public class CastItemDto {
    private final int id;
    private final String character;
    private final String name;

    public CastItemDto(int id, int movieId, String character, String department, String job, String name) {
        this.id = id;
        this.character = character;
        this.name = name;
    }

    public int getId() {
        return id;
    }


    public String getCharacter() {
        return character;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "CastItemDto{" +
                "id='" + id + '\'' +
                ", character='" + character + '\'' +
                ", name=" + name +
                '}';
    }
}
