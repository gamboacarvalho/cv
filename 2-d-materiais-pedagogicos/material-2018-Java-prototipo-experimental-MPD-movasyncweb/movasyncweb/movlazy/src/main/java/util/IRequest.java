/*
 * Copyright (c) 2017, Miguel Gamboa
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

package util;

import java.io.InputStream;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Miguel Gamboa
 *         created on 03-03-2017
 */
public interface IRequest {
    InputStream getBody(String path);

    public default IRequest compose(Consumer<String> cons) {
        return path -> {
            cons.accept(path);
            return getBody(path);
        };
    }

    public default IRequest compose(BiFunction<String, InputStream,InputStream> biFunc) {
        return (path) -> biFunc.apply(path,getBody(path));
    }
}
