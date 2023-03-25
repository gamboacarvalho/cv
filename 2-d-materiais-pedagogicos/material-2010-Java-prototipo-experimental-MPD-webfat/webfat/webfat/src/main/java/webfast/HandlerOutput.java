package webfast;

import java.io.PrintStream;

public interface HandlerOutput<T>{
	void print(PrintStream  responseBody, int depth, T model);
}
