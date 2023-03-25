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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Miguel Gamboa at CCISEL
 */
public class SqlExecutorPolicySingleCall implements SqlExecutorPolicy{

    
    private final String urlConnection;

    public SqlExecutorPolicySingleCall(String urlConnection) {
        this.urlConnection = urlConnection;
    }
    
    @Override
    public SqlExecutor executor() {
        return new SqlExecutorSingleCall();
    }

    @Override
    public void close() throws Exception {
        // ...
        // Nothing to do.
        // Each SqlExecutor itis responsible for close its own connection.
        // ...
    }
    
    class SqlExecutorSingleCall implements SqlExecutor{

        Connection c;
        PreparedStatement cmd;
        ResultSet rs;
        
        @Override
        public ResultSet executeQuery(String query, Object... params) throws SQLException{
            if(c != null)
                throw new IllegalStateException("This SingleCall executor already performed a query. Please instantiate a new executor.");
            c = DriverManager.getConnection(urlConnection);
            cmd = c.prepareStatement(query);
            int idx = 1;
            for (Object p : params) {
                cmd.setObject(idx++, p);
            }
            rs = cmd.executeQuery();
            return rs;
        }

        @Override
        public void close() throws Exception {
            if(c != null){
                rs.close();
                rs = null;
                cmd.close();
                cmd = null;
                c.close();
                c = null;
            }
        }
    }

}

