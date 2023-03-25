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

package pt.isel.mpd14.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import pt.isel.mpd14.dataadapter.Builder;
import pt.isel.mpd14.dataadapter.DataMapper;
import pt.isel.mpd14.dataadapter.SqlExecutorPolicySingleCall;
import pt.isel.mpd14.dataadapter.northwind.Product;

/**
 * Hello world!
 *
 */
public class Program 
{
    public static void main( String[] args ) throws SQLException
    {
        final String connectionUrl = 
			"jdbc:sqlserver://localhost:1433;" +
			"databaseName=Northwind;" + 
			"user=myAppUser;password=fcp";
        Connection c = DriverManager.getConnection(connectionUrl);
        PreparedStatement cmd = c.prepareStatement("SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM Products where ProductId = 1");
        ResultSet rs = cmd.executeQuery();
        int count = 0;
        while(rs.next()) count++;
        System.out.println("Fetched " + count + " rows");
        
        
        Builder b = new Builder(new SqlExecutorPolicySingleCall(connectionUrl));
        DataMapper<Product> prodMapper = b.build(Product.class);
        Iterable<Product> prods = prodMapper.getAll();
    }
}
