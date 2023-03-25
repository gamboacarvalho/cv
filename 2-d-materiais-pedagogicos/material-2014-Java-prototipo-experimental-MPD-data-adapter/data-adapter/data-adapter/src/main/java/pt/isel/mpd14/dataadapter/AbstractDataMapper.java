/*
 * Copyright 2014 Miguel Gamboa at CCISEL.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pt.isel.mpd14.dataadapter;

/**
 *
 * @author Miguel Gamboa at CCISEL
 * @param <T>
 */
public abstract class AbstractDataMapper<T> implements DataMapper<T>{

    private final SqlExecutorPolicy execFac;

    public AbstractDataMapper(SqlExecutorPolicy execFac) {
        this.execFac = execFac;
    }
    
    @Override
    public SqlIterable<T> getAll() {
        return new SqlIterableImpl<>(execFac, strGetAll(), converter());
    }

    @Override
    public void update(T val) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(T val) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void insert(T val) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    protected abstract SqlConverter<T> converter();
    
    protected abstract String strGetAll();
}
