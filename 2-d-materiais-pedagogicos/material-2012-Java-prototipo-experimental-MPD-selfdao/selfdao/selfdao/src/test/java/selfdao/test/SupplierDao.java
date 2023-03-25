package selfdao.test;

import selfdao.DomainEntity;
import selfdao.SqlCmd;
import selfdao.SqlCmdType.Create;
import selfdao.SqlCmdType.Delete;
import selfdao.SqlCmdType.Update;
import selfdao.test.model.Supplier;


@DomainEntity(value = Supplier.class, key = "id")
public interface SupplierDao extends AutoCloseable{
	
	@SqlCmd(cmd = "")
	Iterable<Supplier> getAll();
	
	@SqlCmd(cmd = "")
	Supplier getById(int id);
	
	@SqlCmd(type = Update.class, cmd = "")
	void update();
	
	@SqlCmd(type = Delete.class, cmd = "")
	void delete(int id);
	
	@SqlCmd(type = Create.class, cmd = "")
	Supplier insert();
	
}
