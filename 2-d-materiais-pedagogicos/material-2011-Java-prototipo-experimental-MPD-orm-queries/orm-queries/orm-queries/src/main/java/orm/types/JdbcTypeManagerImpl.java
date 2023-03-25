package orm.types;


import com.google.inject.Inject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import orm.UnsupportedDataType;
import orm.mapper.MapperFactory;
import orm.metadata.JdbcColumnInfo;

public class JdbcTypeManagerImpl implements JdbcTypeManager {
	private Map<Class<?>, JdbcTypeFactory<? extends JdbcType<?>>> factories;
	
	@Inject
	public JdbcTypeManagerImpl(Set<JdbcTypeFactory> types){
		this.factories = new HashMap<Class<?>, JdbcTypeFactory<?>>();
		for(JdbcTypeFactory type:types)
			for(Class<?> cls : type.getHandledTypes())
				factories.put(cls, type);
	}
	
	

	public JdbcType<?> get(Class<?> clazz, JdbcColumnInfo columnInfo, MapperFactory factory){
	   if(factories.containsKey(clazz)){
		   return factories.get(clazz).getInstance(this, columnInfo, factory);   
	   }
	   throw new UnsupportedDataType("O tipo pretendido nao suportado " + clazz.getName());
	}
	
	public boolean containsType(Class<?> clazz){
		return factories.containsKey(clazz);
	}
	

}
