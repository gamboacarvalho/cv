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

package pt.isel.mpd14.dataadapter.northwind;

import pt.isel.mpd14.dataadapter.AbstractDataMapper;
import pt.isel.mpd14.dataadapter.SqlConverter;
import pt.isel.mpd14.dataadapter.SqlExecutorPolicy;
import static pt.isel.mpd14.util.SneakyUtils.call;

/**
 *
 * @author Miguel Gamboa at CCISEL
 */
public class ProductsMapper extends AbstractDataMapper<Product>{

    public ProductsMapper(SqlExecutorPolicy execFac) {
        super(execFac);
    }

    @Override
    protected SqlConverter<Product> converter() {
        return (rs, prod) -> {
            boolean res = call(() -> rs.next());
            if(res) prod.setValue(
                    new Product(
                            call(() -> rs.getInt(1)), 
                            call(() -> rs.getString(2)), 
                            call(() -> rs.getDouble(3)), 
                            call(() -> rs.getInt(4))));
            return res;
        };
    }

    @Override
    protected String strGetAll() {
        return "SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM Products";
    }
    
}
