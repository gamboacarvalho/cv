package webfast.routing;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Path {

	List<PathPart> parts = new LinkedList<PathPart>(); 
	List<PathPartParameter<?>> params = new LinkedList<PathPartParameter<?>>();
	
	public Path __(String constant){
		parts.add(new PathPartConstant(constant));
		return this;
	}
	public <T> Path _arg(Class<T> paramType) throws SecurityException, NoSuchMethodException{
		PathPartParameter<T> p = new PathPartParameter<T>(paramType);
		parts.add(p);
		params.add(p);
		return this;
	}
	public boolean match(String path) throws Exception{
		String[] pathParts = path.substring(1).split("/");
		if(pathParts.length != parts.size()) return false;
		int idx = 0;
		for (PathPart p : parts) {
	    if(!p.match(idx >= pathParts.length? null : pathParts[idx++]))
	    	return false;
    }
		return true;
	}
	public Iterable<PathPartParameter<?>> getParameters(){
		return Collections.unmodifiableList(params);
	} 
}
