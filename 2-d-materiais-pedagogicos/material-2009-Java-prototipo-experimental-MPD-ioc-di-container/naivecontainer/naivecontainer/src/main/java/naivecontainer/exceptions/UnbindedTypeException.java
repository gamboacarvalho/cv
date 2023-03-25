package naivecontainer.exceptions;


@SuppressWarnings("serial")
public class UnbindedTypeException extends NaiveContainerConfigurationException {

	private final Class<?> _type;
	public Class<?> getUnbindedType(){ return _type;}

	public UnbindedTypeException(Class<?> type) {
		super(String.format("Type %s is unbinded",type.getName()));
		_type = type;
	}
}
