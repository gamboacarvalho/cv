package orm.northwind.mapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import orm.core.IConverter;
import orm.core.IDBContext;
import orm.core.ISqlBinder;
import orm.core.SqlIntegerBinder;
import orm.core.TemplateInsert;
import orm.core.TemplateQuery;
import orm.core.TemplateUpdate;
import orm.mapper.AbstractDataMapper;
import orm.northwind.Product;

public class ProductDataMapper extends AbstractDataMapper<Integer, Product>{
	private final IConverter<Product> conv = new IConverter<Product>() {
		@Override
    public Product convert(ResultSet rs) throws SQLException {
	    return new Product(
	    		rs.getInt(1), 
	    		rs.getString(2), 
	    		rs.getString(3), 
	    		rs.getString(4), 
	    		rs.getDouble(5), 
	    		rs.getShort(6), 
	    		rs.getShort(7), 
	    		rs.getShort(8), 
	    		rs.getBoolean(9));
    }
	};
	private final String sqlQuery = "SELECT [ProductID],[ProductName],[QuantityPerUnit],[CategoryName],[UnitPrice],[UnitsInStock],[UnitsOnOrder],[ReorderLevel],[Discontinued] FROM [Northwind].[dbo].[Products], [Northwind].[dbo].[Categories] WHERE Products.CategoryID = Categories.CategoryID";
	private final String sqlQueryById = sqlQuery + " AND ProductID = ?";
	private final String sqlInsert = "INSERT INTO [Northwind].[dbo].[Products]([ProductName],[CategoryID],[QuantityPerUnit],[UnitPrice],[UnitsInStock],[UnitsOnOrder],[ReorderLevel],[Discontinued]) VALUES (?, (SELECT [CategoryID] FROM [Northwind].[dbo].[Categories] WHERE CategoryName = ?), ?, ?, ?, ?, ?, ?)";
	private final TemplateQuery<Product> query;
	private final TemplateQuery<Product> queryById;
	private final TemplateInsert insert;
	
	public ProductDataMapper(IDBContext ctx) {
	  super(ctx);
	  this.query = new TemplateQuery<Product>(sqlQuery, conv);
		this.queryById = new TemplateQuery<Product>(sqlQueryById, conv, SqlIntegerBinder.SINGLETON);
		this.insert = new TemplateInsert(sqlInsert, new ISqlBinder<Product>(){
			@Override
      public void bind(PreparedStatement stmt, int idx, Product value) throws SQLException { 
				stmt.setString(1, value.get_productName()); 
				stmt.setString(2, value.get_categoryName()); 
				stmt.setString(3, value.get_quantityPerUnit()); 
				stmt.setDouble(4, value.get_unitPrice()); 
				stmt.setShort(5, value.get_unitsInStock());
				stmt.setShort(6, value.get_unitsOnOrder()); 
				stmt.setShort(7, value.get_reorderLevel()); 
				stmt.setBoolean(8, value.is_discontinued());
      }
		}){
			@Override
			protected void updateKeyOnDomainObject(Object entity, ResultSet ids) throws SQLException {
				Product o = (Product) entity;
				o.setId(ids.getInt(1));
			}
		};
  }

	@Override
  protected TemplateQuery<Product> getQueryCmd() {
		return query;
  }

	@Override
  protected TemplateQuery<Product> getQueryByIdCmd() {
		return queryById;
  }

	@Override
  protected TemplateUpdate getUpdateCmd() {
	  return null;
  }

	@Override
  protected TemplateUpdate getDeleteCmd() {
	  return null;
  }

	@Override
  protected TemplateInsert getInsertCmd() {
	  return insert;
  }
	@Override
  protected Integer doGetKeyFrom(ResultSet rs) throws SQLException {
	  return rs.getInt(1);
  }
}
