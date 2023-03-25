package app;


import java.net.InetSocketAddress;

import orm.core.DBContextSingleConnection;
import orm.core.IDBContext;
import orm.mapper.IDataMapper;
import orm.northwind.Employee;
import orm.northwind.Order;
import orm.northwind.mapper.EmployeeDataMapper;
import orm.northwind.mapper.OrdersMapper;
import orm.uow.IRepository;
import orm.uow.RepositoryOrdered;
import orm.uow.UnitOfWork;

import webfast.Controller;
import webfast.Resolver;
import webfast.ResourcesHandler;
import webfast.northwind.EmployeesController;
import webfast.northwind.OrdersController;

import com.sun.net.httpserver.HttpServer;

public class WebApp {
	static final String connectionUrl = 
		"jdbc:sqlserver://localhost:1433;" +
		"databaseName=Northwind;" + 
		"user=myAppUser;password=fcp";

	public static void main(String[] args) throws Exception {
		//
		// Set up repository
		//
		IDBContext db = new DBContextSingleConnection(connectionUrl);
		OrdersMapper ordersMapper = new OrdersMapper(db); 
		IDataMapper<Integer, Employee> mapperEmployee = new EmployeeDataMapper(db, ordersMapper);
		ordersMapper.setEmployeeMapper(mapperEmployee);
		UnitOfWork uow = new UnitOfWork();
		IRepository repo = new RepositoryOrdered(uow);
		uow.setRepository(repo);
		repo.add(Employee.class, mapperEmployee);
		repo.add(Order.class, ordersMapper);
		//
		// Arrange
		//
		Controller ctrEmployees = new EmployeesController(repo);
		Controller ctrOrders = new OrdersController(repo);
		Resolver wfast = new Resolver();
		wfast.attach(ctrEmployees).path("employees");
		wfast.attach(ctrEmployees).path("employees")._arg(Integer.class);
		wfast.attach(ctrOrders).path("employees")._arg(Integer.class).__("orders");
		//
		// Run Http Server
		//
		HttpServer server = HttpServer.create(
			new InetSocketAddress("127.0.0.1", 8080), 
			20);
		server.createContext("/", wfast);
		server.createContext("/resources/", new ResourcesHandler());
		System.out.println("WebFast server will start...");
		server.start();
		System.out.println("WebFast server started");
	}
}
