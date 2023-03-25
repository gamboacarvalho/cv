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

import java.sql.ResultSet;
import java.util.Iterator;
import static pt.isel.mpd14.util.SneakyUtils.call;

/**
 *
 * @author Miguel Gamboa at CCISEL
 * @param <T>
 */
public class SqlIterableImpl<T> implements SqlIterable<T>{

    private SqlExecutorPolicy execFac;
    private final String query;
    private final SqlConverter<T> converter;

    public SqlIterableImpl(SqlExecutorPolicy execFac, String query, SqlConverter<T> converter) {
        this.execFac = execFac;
        this.query = query;
        this.converter = converter;
    }
    
    @Override
    public SqlIterable<T> where(String clause){
        String newQuery = query.toUpperCase().contains("WHERE")?
                query + " AND " + clause : 
                query + " WHERE " + clause;
        return new SqlIterableImpl<>(execFac, newQuery, converter);
    }
    
    @Override
    public Iterable<T> bind(final Object...params){
        return () -> new SqlIterator(params);
    }
    
    @Override
    public Iterator<T> iterator() {
        return new SqlIterator();
    }    
    
    @Override
    public void close() throws Exception{
        if(execFac != null){
            execFac.close();
            execFac = null;
        }
    }
    
    class SqlIterator implements Iterator<T>{

        Nullable<T> next;
        SqlExecutor exec;
        ResultSet rs; 

        public SqlIterator(final Object...params) {
            exec = execFac.executor();
            rs = call(() -> exec.executeQuery(query, params));
            next = new Nullable<>();
            converter.Convert(rs, next);
        }
        
        @Override
        public boolean hasNext() {
            return next.hasValue();
        }

        @Override
        public T next() {
            if(!next.hasValue())
                throw new IllegalStateException("Next element not available!");
            T curr = next.value();
            next = new Nullable<>();
            converter.Convert(rs, next);
            if(!next.hasValue())
                call(() -> exec.close());
            return curr;
        }
    }
}

