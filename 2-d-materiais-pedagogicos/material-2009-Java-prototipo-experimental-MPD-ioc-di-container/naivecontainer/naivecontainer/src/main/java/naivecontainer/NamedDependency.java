package naivecontainer;

public class NamedDependency<T> extends Dependency<T> {

	private final String _name;
	public String getName(){ return _name;}
	
	public NamedDependency(String name, Class<T> klass){
		super(klass);
		_name = name;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object otherObj){
		if(otherObj instanceof NamedDependency){
			NamedDependency<T> other = (NamedDependency<T>)otherObj;
			return super.equals(other) && this.getName().equals(other.getName());
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return _name.hashCode() ^ getType().hashCode();
	}		
}
