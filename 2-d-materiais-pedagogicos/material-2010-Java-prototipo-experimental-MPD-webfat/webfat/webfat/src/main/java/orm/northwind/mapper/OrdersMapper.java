package orm.northwind.mapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import orm.core.ICmd;
import orm.core.IConverter;
import orm.core.IDBContext;
import orm.core.ISqlBinder;
import orm.core.IValueLoader;
import orm.core.SqlIntegerBinder;
import orm.core.TemplateInsert;
import orm.core.TemplateQuery;
import orm.core.TemplateUpdate;
import orm.core.ValueHolder;
import orm.mapper.AbstractDataMapper;
import orm.mapper.IDataMapper;
import orm.northwind.Employee;
import orm.northwind.Order;

public class OrdersMapper extends AbstractDataMapper<Integer, Order>{
	private final IConverter<Order> conv = new IConverter<Order>() {
		public Order convert(ResultSet rs) throws SQLException {
			return  new Order(
					rs.getInt(1), 
					rs.getString(2), 
					rs.getString(3), 
					rs.getString(4),
					empsMapper.loadById(rs.getInt(5)));
		}
	};
	private final String sqlQuery = "SELECT [OrderID], [ShipName],[ShipAddress],[ShipCity], EmployeeId FROM [Northwind].[dbo].[Orders]";
	private final String sqlQueryById = sqlQuery + " WHERE OrderId = ?";
	private final String sqlInsert = "INSERT INTO Orders ([ShipName],[ShipAddress],[ShipCity], EmployeeId ) VALUES (?, ?, ?, ?)";
	private final String sqlUpdate  = "UPDATE Orders SET [ShipName] = ?,[ShipAddress] = ?,[ShipCity] = ?, EmployeeId = ? WHERE OrderId = ?";
	private final TemplateQuery<Order> query;
	private final TemplateQuery<Order> queryById;
	private final TemplateInsert insert;
	private final TemplateUpdate update;
	private IDataMapper<Integer, Employee> empsMapper;

	public OrdersMapper(IDBContext ctx) {
		super(ctx);
		this.query = new TemplateQuery<Order>(sqlQuery, conv);
		this.queryById = new TemplateQuery<Order>(sqlQueryById, conv, SqlIntegerBinder.SINGLETON);
		this.update = new TemplateUpdate(sqlUpdate, new ISqlBinder<Order>(){
      public void bind(PreparedStatement stmt, int idx, Order value)throws SQLException {
      	stmt.setString(1, value.getShipName());
      	stmt.setString(2, value.getShipAddress());
      	stmt.setString(3, value.getShipCity());
      	Employee e = value.getEmployee();
      	if(e == null)
      		stmt.setNull(4, Types.INTEGER);
      	else
      		stmt.setInt(4, e.getId());
      	stmt.setInt(5, value.getId());
      }					
		});
		this.insert = new TemplateInsert(
				sqlInsert,
				new ISqlBinder<Order>(){
          public void bind(PreparedStatement stmt, int idx, Order value)throws SQLException {
          	Employee e = value.getEmployee();
          	stmt.setString(1, value.getShipName());
          	stmt.setString(2, value.getShipAddress());
          	stmt.setString(3, value.getShipCity());
          	if(e == null)
          		stmt.setNull(4, Types.INTEGER);
          	else
          		stmt.setInt(4, e.getId());
          }					
				}
		){
			protected void updateKeyOnDomainObject(Object entity, ResultSet ids)throws SQLException {
				Order o = (Order) entity;
				o.setId(ids.getInt(1));
			}
		};
	}
	public void  setEmployeeMapper(IDataMapper<Integer, Employee> m){
		empsMapper = m;
	}
	@Override
	protected TemplateQuery<Order> getQueryCmd() {
		return query;
	}

	@Override
	protected TemplateQuery<Order> getQueryByIdCmd() {
		// TODO Auto-generated method stub
		return queryById;
	}
	@Override
	protected TemplateUpdate getUpdateCmd() {
		return update;
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
