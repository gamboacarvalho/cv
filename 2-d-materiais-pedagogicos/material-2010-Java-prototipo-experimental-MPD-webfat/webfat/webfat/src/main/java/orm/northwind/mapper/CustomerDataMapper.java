package orm.northwind.mapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import orm.core.IConverter;
import orm.core.IDBContext;
import orm.core.ISqlBinder;
import orm.core.SqlIntegerBinder;
import orm.core.SqlStringBinder;
import orm.core.TemplateInsert;
import orm.core.TemplateQuery;
import orm.core.TemplateUpdate;
import orm.mapper.AbstractDataMapper;
import orm.northwind.Customer;

public class CustomerDataMapper extends AbstractDataMapper<String, Customer>{
	private final IConverter<Customer> conv = new IConverter<Customer>() {
		@Override
    public Customer convert(ResultSet rs) throws SQLException {
	    return new Customer(
	    		rs.getString(1), 
	    		rs.getString(2), 
	    		rs.getString(3), 
	    		rs.getString(4), 
	    		rs.getString(5), 
	    		rs.getString(6), 
	    		rs.getString(7), 
	    		rs.getString(8), 
	    		rs.getString(9), 
	    		rs.getString(10), 
	    		rs.getString(11));
    }
	};
	private final String sqlQuery = "SELECT [CustomerID],[CompanyName],[ContactName],[ContactTitle],[Address],[City],[Region],[PostalCode],[Country],[Phone],[Fax] FROM [Northwind].[dbo].[Customers]";
	private final String sqlQueryById = sqlQuery + " WHERE CustomerID = ?";
	private final String sqlInsert = "INSERT INTO [Northwind].[dbo].[Customers]([CustomerID], [CompanyName],[ContactName],[ContactTitle],[Address],[City],[Region],[PostalCode],[Country],[Phone],[Fax]) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private final TemplateQuery<Customer> query;
	private final TemplateQuery<Customer> queryById;
	private final TemplateInsert insert;
	
	public CustomerDataMapper(IDBContext ctx) {
	  super(ctx);
	  this.query = new TemplateQuery<Customer>(sqlQuery, conv);
		this.queryById = new TemplateQuery<Customer>(sqlQueryById, conv, SqlStringBinder.SINGLETON);
		this.insert = new TemplateInsert(sqlInsert, new ISqlBinder<Customer>(){
			@Override
      public void bind(PreparedStatement stmt, int idx, Customer value) throws SQLException {
				stmt.setString(1, value.getId());
				stmt.setString(2, value.getCompanyName()); 
				stmt.setString(3, value.getContactName()); 
				stmt.setString(4, value.getContactTitle()); 
				stmt.setString(5, value.getAddress()); 
				stmt.setString(6, value.getCity());
				stmt.setString(7, value.getRegion()); 
				stmt.setString(8, value.getPostalCode()); 
				stmt.setString(9, value.getCountry());
				stmt.setString(10, value.getPhone());
				stmt.setString(11, value.getFax());
      }
		}){
			@Override
			protected void updateKeyOnDomainObject(Object entity, ResultSet ids) throws SQLException {
				// Nothing to do because the Key was already set in the Customer.
			}
		};
  }

	@Override
  protected TemplateQuery<Customer> getQueryCmd() {
		return query;
  }

	@Override
  protected TemplateQuery<Customer> getQueryByIdCmd() {
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
  protected String doGetKeyFrom(ResultSet rs) throws SQLException {
	  return rs.getString(1);
  }
}
