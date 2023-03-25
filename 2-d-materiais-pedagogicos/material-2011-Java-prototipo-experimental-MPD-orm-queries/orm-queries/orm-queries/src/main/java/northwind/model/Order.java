package northwind.model;

import java.util.Date;

import orm.Entity;
import orm.annotations.JdbcCol;
import orm.annotations.JdbcMapper;
import orm.types.ValueHolder;

public class Order implements Entity<Integer>{
	
	private int OrderId;
	private ValueHolder<Employee> Employee;
	private Date OrderDate;
	private String ShipName;
	private String ShipAddress;
	private Iterable<OrderDetails> OrderDetails;
	@JdbcMapper(table = "Orders")
	public Order(
			@JdbcCol(value="OrderID", isIdentity = true, isPk = true) int orderId,
			@JdbcCol(value="EmployeeId", referencedKeyClass=Integer.class) ValueHolder<Employee> employee,
			@JdbcCol(value="OrderDate") Date orderDate,
			@JdbcCol(value="ShipName") String shipName,
			@JdbcCol(value="ShipAddress") String shipAddress,
			@JdbcCol(value="OrderId") Iterable<OrderDetails> orderDetails
			){
		this.OrderId = orderId;
		this.Employee = employee;
		this.OrderDate = orderDate;
		this.ShipName = shipName;
		this.ShipAddress = shipAddress;
		this.OrderDetails = orderDetails;
	}
	
	@Override
	public Integer getId() {
		return OrderId;
	}

	@Override
	public void setId(Integer id) {
		OrderId = id;
	}

	public ValueHolder<Employee> getEmployee() {
		return Employee;
	}

	public void setEmployee(ValueHolder<Employee> employee) {
		Employee = employee;
	}

	public Date getOrderDate() {
		return OrderDate;
	}

	public void setOrderDate(Date orderDate) {
		OrderDate = orderDate;
	}

	public String getShipName() {
		return ShipName;
	}

	public void setShipName(String shipName) {
		ShipName = shipName;
	}

	public String getShipAddress() {
		return ShipAddress;
	}

	public void setShipAddress(String shipAddress) {
		ShipAddress = shipAddress;
	}

	public Iterable<OrderDetails> getOrderDetails() {
		return OrderDetails;
	}

	public void setOrderDetails(Iterable<OrderDetails> orderDetails) {
		OrderDetails = orderDetails;
	}

}
