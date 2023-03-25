package northwind.model;

import java.util.Date;

import orm.Entity;
import orm.annotations.JdbcCol;
import orm.annotations.JdbcMapper;
import orm.types.ValueHolder;

public class Employee  implements Entity<Integer>{

	private int EmployeeId;
	private String LastName;
	private String FirstName;
	private String Title;
	private Date birthDate;
	private ValueHolder<Employee> ReportsTo;
	private Iterable<Employee> employees;
	
	public Iterable<Employee> getEmployees() {
		return employees;
	}


	public void setEmployees(Iterable<Employee> employees) {
		this.employees = employees;
	}


	private Iterable<Order> orders;
	
	@JdbcMapper(table="Employees")
	public Employee(
			@JdbcCol(value="EmployeeId", isIdentity=true, isPk=true) int employeeId, 
			@JdbcCol(value="LastName") String lastName, 
			@JdbcCol(value="FirstName") String firstName,
			@JdbcCol(value="Title") String title, 
			@JdbcCol(value="BirthDate") Date birthDate, 
			@JdbcCol(value="ReportsTo", referencedKeyClass=Integer.class) ValueHolder<Employee> reportsTo,
			@JdbcCol(value="EmployeeId", referencedKeyClass=Integer.class) Iterable<Order> orders,
			@JdbcCol(value = "ReportsTo") Iterable<Employee> employees) {
		this.EmployeeId = employeeId;
		this.LastName = lastName;
		this.FirstName = firstName;
		this.Title = title;
		this.birthDate = birthDate;
		this.ReportsTo = reportsTo;
		this.orders = orders;
		this.employees = employees;
	}

	
	public int getEmployeeId() {
		return EmployeeId;
	}


	public void setEmployeeId(int employeeId) {
		EmployeeId = employeeId;
	}


	public String getLastName() {
		return LastName;
	}


	public void setLastName(String lastName) {
		LastName = lastName;
	}


	public String getFirstName() {
		return FirstName;
	}


	public void setFirstName(String firstName) {
		FirstName = firstName;
	}


	public String getTitle() {
		return Title;
	}


	public void setTitle(String title) {
		Title = title;
	}


	public Date getBirthDate() {
		return birthDate;
	}


	public void setBirthDate(Date dirthDate) {
		birthDate = dirthDate;
	}


	@Override
	public Integer getId() {
		return EmployeeId;
	}

	@Override
	public void setId(Integer id) {
		EmployeeId=id;
	}


	public Employee getReportsTo() {
		return ReportsTo.get();
	}


	public void setReportsTo(final Employee reportsTo) {
		ReportsTo = new ValueHolder<Employee>() {

			@Override
			public Employee get() {
				return reportsTo;
			}
		};
	}


	public Iterable<Order> getOrders() {
		return orders;
	}


	public void setOrders(Iterable<Order> orders) {
		this.orders = orders;
	}

}
