package naivecontainer;

public class Dependency<T> {

	private final Class<T> _klass;
	public Class<T> getType(){ return _klass;}
	
	public Dependency(Class<T> klass){
		_klass = klass;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object otherObj){
		if(otherObj instanceof Dependency){
			Dependency<T> other = (Dependency<T>)otherObj;
			return this.getType().equals(other.getType());
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return _klass.hashCode();
	}
		
}
