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

package movasync.dto;

public class PersonDto {
    private final int id;
    private final String name;
    private final String profile_path;
    private final String place_of_birth;
    private final String biography;

    public PersonDto(int id, String name, String place_of_birth, String biography,String profile_path) {
        this.id = id;
        this.name = name;
        this.place_of_birth = place_of_birth;
        this.biography = biography;
        this.profile_path = profile_path;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public String getPlace_of_birth() {
        return place_of_birth;
    }

    public String getBiography() {
        return biography;
    }

    @Override
    public String toString() {
        return "PersonDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", profile_path='" + profile_path + '\'' +
                ", place_of_birth='" + place_of_birth + '\'' +
                ", biography='" + biography + '\'' +
                '}';
    }
}
