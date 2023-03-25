package webfast.northwind;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import orm.northwind.Employee;
import orm.uow.IRepository;
import webfast.AbstractController;
import webfast.ControllerHandler;
import webfast.ControllerHandler.InvokeMehod;
import webfast.HandlerOutput;

public class EmployeesController extends AbstractController{
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
	final IRepository repo;
	final EmployeeDetailsView empsView;
	
	public EmployeesController(IRepository repo) {
	  super();
	  this.repo = repo;
	  this.empsView = new EmployeeDetailsView();
  }

	@ControllerHandler(InvokeMehod.Get)
	public HandlerOutput<?> getEmployerById(int id) throws SQLException{
		return view(empsView, repo.loadById(Employee.class, id));
	}
	@ControllerHandler(InvokeMehod.Get)
	public HandlerOutput<?> getAll() throws SQLException{
		return new EmployeesListView(repo.load(Employee.class));
	}
	@ControllerHandler(InvokeMehod.Post)
	public int createEmployee() throws SQLException, ParseException{
		String strDate = getFormData("birthDate");
		Employee e = new Employee(
				getFormData("firstName"), 
				getFormData("lastName"),
				getFormData("title"),
				getFormData("city"),
				strDate != null? dateFormat.parse(strDate) : null);
		repo.save();
		return redirect(302, "/employees");
	}
}
