package orm.northwind;

import orm.uow.Entity;
import orm.uow.Entity.EntityState;

public class Order extends Entity<Integer>{
	private String _shipName;
	private String _shipAddress;
	private String _shipCity;
	private Employee _employee;
	public Order(int orderId, String shipName, String shipAddress, String shipCity, Employee emp) {
	  super(orderId); // Clean State 
	  _shipName = shipName;
	  _shipAddress = shipAddress;
	  _shipCity = shipCity;
	  _employee = emp;
  }

	public Order(String shipName, String shipAddress, String shipCity, Employee emp) {
	  super(); // New State
	  _shipName = shipName;
	  _shipAddress = shipAddress;
	  _shipCity = shipCity;
	  _employee = emp;
	  //onUpdate(this, EntityState.New);
  }
	public String getShipName() {
  	return _shipName;
  }
	public String getShipAddress() {
  	return _shipAddress;
  }
	public String getShipCity() {
  	return _shipCity;
  }
	public Employee getEmployee(){
		return _employee;
		
	}
	public void setEmployee(Employee e){
		_employee = e;
		onUpdate(this, EntityState.Updated);
	}
}
