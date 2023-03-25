package selfdao.test;

import java.util.Date;

import selfdao.DomainEntity;
import selfdao.SqlCmd;
import selfdao.SqlCmdType.Create;
import selfdao.SqlCmdType.Delete;
import selfdao.SqlCmdType.Update;
import selfdao.test.model.Employee;


@DomainEntity(value = Employee.class, key = "id")
public interface EmployeeDao extends AutoCloseable{
	
	@SqlCmd(cmd = "SELECT EmployeeId, Title, FirstName, LastName, BirthDate FROM Employees")
	Iterable<Employee> getAll();
	
	@SqlCmd(cmd = "SELECT EmployeeId, Title, FirstName, LastName, BirthDate FROM Employees WHERE EmployeeId = ?")
	Employee getById(int id);
	
	@SqlCmd(type = Update.class, cmd = "UPDATE Employees SET Title = ?, FirstName = ?, LastName = ?, BirthDate = ? WHERE EmployeeId = ?")
	void update(String title, String firstName, String lastName, Date birthDate, int id);
	
	@SqlCmd(type = Delete.class, cmd = "DELETE FROM Employees WHERE EmployeeId = ?")
	void delete(int id);
	
	@SqlCmd(type = Create.class, cmd = "INSERT INTO Employees (Title, FirstName, LastName, BirthDate) VALUES (?, ?, ?, ?)")
	Employee insert(String title, String firstName, String lastName, Date birthDate);
	
}
