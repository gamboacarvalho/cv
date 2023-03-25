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

package pt.isel.mpd14.dataadapter.test;

import static java.util.stream.StreamSupport.stream;
import org.junit.Assert;
import org.junit.Test;
import pt.isel.mpd14.dataadapter.SqlExecutorPolicySingleCall;
import pt.isel.mpd14.dataadapter.SqlIterable;
import pt.isel.mpd14.dataadapter.northwind.Product;
import pt.isel.mpd14.dataadapter.northwind.ProductsMapper;

/**
 *
 * @author Miguel Gamboa at CCISEL
 */
public class ProductsMapperTest {
    static final String url = 
			"jdbc:sqlserver://localhost:1433;" +
			"databaseName=Northwind;" + 
			"user=myAppUser;password=fcp";
    
    @Test
    public void test_get_all(){
        ProductsMapper mapper = new ProductsMapper(new SqlExecutorPolicySingleCall(url));
        long count = stream(mapper.getAll().spliterator(), false).count();
        Assert.assertEquals(77, count);
    }
    
    @Test
    public void test_where(){
        ProductsMapper mapper = new ProductsMapper(new SqlExecutorPolicySingleCall(url));
        Iterable<Product> res = mapper
                .getAll()
                .where("UnitPrice > 15.5")
                .where("UnitsinStock > 5");
        long count = stream(res.spliterator(), false).count();
        Assert.assertEquals(45, count);
    }
    
    @Test
    public void test_multiple_bind(){
        ProductsMapper mapper = new ProductsMapper(new SqlExecutorPolicySingleCall(url));
        SqlIterable<Product> res = mapper
                .getAll()
                .where("UnitPrice > ?")
                .where("UnitsInStock > ?");

        long count = stream(res.bind(20, 10).spliterator(), false).count();
        Assert.assertEquals(30, count);
        
        count = stream(res.bind(30.8, 5).spliterator(), false).count();
        Assert.assertEquals(21, count);

    }
    
}
