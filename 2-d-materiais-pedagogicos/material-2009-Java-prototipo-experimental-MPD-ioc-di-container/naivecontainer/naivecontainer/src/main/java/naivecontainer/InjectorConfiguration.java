package naivecontainer;

import naivecontainer.exceptions.NaiveContainerException;

/** Something that configures injectors */
public interface InjectorConfiguration {
	public void configure(Injector injector) throws NaiveContainerException;
}
