package webfast;

import java.io.InputStream;
import java.io.PrintStream;

import com.sun.net.httpserver.Headers;

import webfast.routing.PathPartParameter;

public interface Controller {
	int handle(
			PrintStream responseBody, 
			Iterable<PathPartParameter<?>> params,
			InputStream requestBody,
			Headers headers,
			String requestMethod) throws Exception;
}
