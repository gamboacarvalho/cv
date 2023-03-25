package selfdao.test;

import selfdao.DomainEntity;

import selfdao.SqlCmd;
import selfdao.SqlCmdType.Create;
import selfdao.SqlCmdType.Delete;
import selfdao.SqlCmdType.Update;
import selfdao.test.model.Order;


@DomainEntity(value = Order.class, key = "id")
public interface OrderDao extends AutoCloseable{
	
	@SqlCmd(cmd = "")
	Iterable<Order> getAll();
	
	@SqlCmd(cmd = "")
	Order getById(int id);
	
	@SqlCmd(type = Update.class, cmd = "")
	void update();
	
	@SqlCmd(type = Delete.class, cmd = "")
	void delete(int id);
	
	@SqlCmd(type = Create.class, cmd = "")
	Order insert();
	
}
