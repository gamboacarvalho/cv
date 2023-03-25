package webfast.northwind;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import orm.northwind.Employee;
import orm.northwind.Order;
import orm.uow.IRepository;
import webfast.AbstractController;
import webfast.ControllerHandler;
import webfast.ControllerHandler.InvokeMehod;
import webfast.HandlerOutput;

public class OrdersController extends AbstractController{
	final IRepository repo;
	
	public OrdersController(IRepository repo) {
	  super();
	  this.repo = repo;
	}
	@ControllerHandler(InvokeMehod.Get)
	public HandlerOutput<?> getAll(int empId) throws SQLException{
		return new OrdersListView(repo.where(Order.class, "EmployeeId = " + empId));
	}
}
