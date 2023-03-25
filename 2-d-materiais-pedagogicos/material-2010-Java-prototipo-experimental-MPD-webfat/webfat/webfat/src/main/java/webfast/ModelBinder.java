package webfast;

import java.io.PrintStream;

public interface ModelBinder<T>{
	void bind(T model, PrintStream out);
}
