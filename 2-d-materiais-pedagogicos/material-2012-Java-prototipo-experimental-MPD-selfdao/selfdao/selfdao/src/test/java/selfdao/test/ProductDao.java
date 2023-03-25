package selfdao.test;

import selfdao.DomainEntity;
import selfdao.SqlCmd;
import selfdao.SqlCmdType.Create;
import selfdao.SqlCmdType.Delete;
import selfdao.SqlCmdType.Update;
import selfdao.test.model.Product;


@DomainEntity(value = Product.class, key = "id")
public interface ProductDao extends AutoCloseable{
	
	@SqlCmd(cmd = "SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM Products")
	Iterable<Product> getAll();
	
	@SqlCmd(cmd = "SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM Products WHERE ProductID = ?")
	Product getById(int id);
	
	@SqlCmd(type = Update.class, cmd = "UPDATE Products SET ProductName = ?, UnitPrice = ?, UnitsInStock = ? WHERE ProductID = ?")
	void update(String name, double price, int stock, int id);
	
	@SqlCmd(type = Delete.class, cmd = "DELETE FROM Products WHERE ProductID = ?")
	void delete(int id);
	
	@SqlCmd(type = Create.class, cmd = "INSERT INTO Products (ProductName, UnitPrice, UnitsInStock) VALUES (?, ?, ?)")
	Product insert(String name, double price, int stock);
	
}
