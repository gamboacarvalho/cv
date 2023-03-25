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
package pt.isel.mpd.jsonzai.test;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 *
 * @author Miguel Gamboa at CCISEL
 */
public class TestFindToken extends TestCase{
    
    public void test_look_for_nonescaped_char(){
        String src = "Quantos \\[  \\[  [  ";
        int idx = src.indexOf('\\');
        Assert.assertEquals(8, idx);
        idx = src.indexOf('\\', idx + 1);
        Assert.assertEquals(12, idx);
    }
    
}
