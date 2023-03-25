package orm.northwind.mapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Iterator;

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

public class EmployeeDataMapper extends AbstractDataMapper<Integer, Employee>{
	
	private final IConverter<Employee> empConv = new IConverter<Employee>() {
		public Employee convert(ResultSet rs) throws SQLException {
			final int key = rs.getInt(1);
			final int reportsToId = rs.getInt(6);
			return  new Employee(
					key, 
					rs.getString(2), 
					rs.getString(3), 
					rs.getString(4),
					rs.getString(5),
					rs.getDate(7),
					new ValueHolder<Employee>(new IValueLoader<Employee>() {
						public Employee load() throws SQLException {
							return loadById(reportsToId);
						}							
					}),
					new Iterable<Order>() {
						public Iterator<Order> iterator() {
							try {
								return ordersMapper.where("EmployeeId = " + key).iterator();
							} catch (SQLException e) {
								throw new IllegalStateException(e);
							}
						}
					});
		}
	};
	final static String sqlEmpployee = "Select EmployeeId, FirstName, LastName, Title, City, ReportsTo, BirthDate from Employees";
	final static String sqlEmpployeeByid = sqlEmpployee + " WHERE EmployeeId = ?";
	final static String sqlInsertEmployee = "insert into Employees (FirstName, LastName, Title, City, ReportsTo, BirthDate ) values (?, ?, ?, ?, ?, ?)"; 
	final static String sqlUpdateEmployee = "UPDATE Employees SET LastName = ?, FirstName = ?,Title = ?, City  = ?, BirthDate = ? WHERE EmployeeId = ?";
	final static String sqlDeleteEmployees = "DELETE FROM [Northwind].[dbo].[EmployeeTerritories] WHERE EmployeeID = ? ; DELETE FROM [Northwind].[dbo].[Employees] WHERE EmployeeID = ?";
	
	final TemplateQuery<Employee> query;
	final TemplateQuery<Employee> queryById;
	final TemplateInsert insert;
	final TemplateUpdate update;
	final TemplateUpdate delete;
	
	final IDataMapper<Integer, Order> ordersMapper;

	public EmployeeDataMapper(IDBContext ctx, IDataMapper<Integer, Order> ordersMapper) {
		super(ctx);
		this.ordersMapper = ordersMapper;
		query = new TemplateQuery<Employee>(sqlEmpployee, empConv);
		queryById = new TemplateQuery<Employee>(sqlEmpployeeByid, empConv, SqlIntegerBinder.SINGLETON);
		insert = new TemplateInsert(
				sqlInsertEmployee, 
				new ISqlBinder<Employee>(){
					public void bind(PreparedStatement stm, int idx, Employee value) throws SQLException {
						Employee reportsTo = value.reportsTo(); 
						stm.setString(1, value.getFirstName());
						stm.setString(2, value.getLastName());
						stm.setString(3, value.getTitle());
						stm.setString(4, value.getCity());
						if(reportsTo == null)
							stm.setNull(5, Types.INTEGER);
						else
							stm.setInt(5, reportsTo.getId());
						if(value.getBirthDate() != null)
							stm.setDate(6, new java.sql.Date(value.getBirthDate().getTime()));
						else
							stm.setNull(6, java.sql.Types.DATE); 
					}
				}
		){
			protected void updateKeyOnDomainObject(Object entity, ResultSet ids) throws SQLException {
				Employee e = (Employee) entity;
				e.setId(ids.getInt(1));
			}
		};
		update = new TemplateUpdate(sqlUpdateEmployee, new ISqlBinder<Employee>(){
			public void bind(PreparedStatement stm, int idx, Employee value) throws SQLException {
				stm.setString(1, value.getLastName());
				stm.setString(2, value.getFirstName());
				stm.setString(3, value.getTitle());
				stm.setString(4, value.getCity());
				stm.setDate(5, new java.sql.Date(value.getBirthDate().getTime()));
				stm.setInt(6, value.getId());
			}
		});
		delete = new TemplateUpdate(sqlDeleteEmployees, new ISqlBinder<Employee>(){
			public void bind(PreparedStatement stm, int idx, Employee value) throws SQLException {
				stm.setInt(1, value.getId());
				stm.setInt(2, value.getId());
			}
		}); 
	}
	

	@Override
	protected TemplateQuery<Employee> getQueryCmd() {
		return query;
	}

	@Override
	protected TemplateQuery<Employee> getQueryByIdCmd() {
		return queryById;
	}

	@Override
	protected TemplateUpdate getUpdateCmd() {
		return update;
	}

	@Override
	protected TemplateUpdate getDeleteCmd() {
		return delete;
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
