package orm.northwind;

import java.sql.SQLException;
import java.util.Date;

import orm.core.ValueHolder;
import orm.uow.Entity;
import orm.uow.Entity.EntityState;

public class Employee extends Entity<Integer>{
	private String _FirstName, _LastName, _Title, _City;
	private Date _BirthDate;
	private ValueHolder<Employee> _reportsTo;
	private Iterable<Order> _orders;
	public Employee(String firstName, String lastName, String title, String city, Date birthDate) {
		if(firstName == null) throw new IllegalArgumentException("The first name cannot be null!!!");
		if(lastName == null) throw new IllegalArgumentException("The last name cannot be null!!!");
	  _FirstName = firstName;
	  _LastName = lastName;
	  _Title = title;
	  _City = city;
	  _BirthDate = birthDate;
	  //onUpdate(this, EntityState.New);
  }
	public Employee(int employeeID, String firstName, String lastName,
      String title, String city, Date birthDate, ValueHolder<Employee> reportsTo, Iterable<Order> orders) {
		super(employeeID); // Clean state
		_FirstName = firstName;
	  _LastName = lastName;
	  _Title = title;
	  _City = city;
	  _reportsTo = reportsTo;
	  _orders = orders;
	  _BirthDate = birthDate;
  }
	public void set_FirstName(String _FirstName) {
		if(_FirstName == null) throw new IllegalArgumentException("The first name cannot be null!!!");
  	this._FirstName = _FirstName;
  	onUpdate(this, EntityState.Updated);
  }
	public void set_LastName(String _LastName) {
		if(_LastName == null) throw new IllegalArgumentException("The last name cannot be null!!!");
  	this._LastName = _LastName;
  	onUpdate(this, EntityState.Updated);
  }
	public void set_Title(String _Title) {
  	this._Title = _Title;
  	onUpdate(this, EntityState.Updated);
  }
	public void set_City(String _City) {
  	this._City = _City;
  	onUpdate(this, EntityState.Updated);
  }
	public String getFirstName() {
  	return _FirstName;
  }
	public String getLastName() {
  	return _LastName;
  }
	public String getTitle() {
  	return _Title;
  }
	public String getCity() {
  	return _City;
  }
	public Date getBirthDate() {
  	return _BirthDate;
  }
	public void setBirthDate(Date _BirthDate) {
  	this._BirthDate = _BirthDate;
  }
	public Employee reportsTo() throws SQLException{
		return _reportsTo !=null? _reportsTo.get():null;
	}
	public Iterable<Order> getOrders(){
		return _orders;
	}
	
	public void addOrder(Order o){
		o.setEmployee(this);
	}
	@Override
  public String toString() {
	  return "Employee [EmployeeID=" + _id + ", FirstName=" + _FirstName
	      + ", LastName=" + _LastName + ", Title=" + _Title + "]";
  }
	public void delete(){
		for (Order o : getOrders()) {
	    o.setEmployee(null);
    }
		onUpdate(this, EntityState.ToDelete);
	}
}
