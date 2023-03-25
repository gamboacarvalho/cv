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
import pt.isel.mpd.githubgw.model.IGhUser;

import java.util.stream.Stream;

/**
 * Created by Miguel Gamboa on 08-06-2015.
 */
public class GhUser implements IGhUser{

    @Override
    public int getId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getLogin() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getCompany() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Stream<IGhOrg> getOrgs() {
        throw new UnsupportedOperationException();
    }
}
