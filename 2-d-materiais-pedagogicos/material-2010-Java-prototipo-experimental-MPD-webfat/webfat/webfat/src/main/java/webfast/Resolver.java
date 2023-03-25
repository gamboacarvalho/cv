package webfast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import webfast.routing.Path;

public class Resolver implements HttpHandler {
	private Map<Path, Controller> routes = new HashMap<Path, Controller>();
	
	public static interface Node{
		Path path(String constant);
	}
	
	public Node attach(final Controller ctr){
		return new Node() {
			@Override
			public Path path(String constant) {
				Path p = new Path();
				p.__(constant);
				routes.put(p, ctr);
				return p;
			}
		};
	}

	@Override
  public void handle(HttpExchange exch) throws IOException{
		ByteArrayOutputStream mem = null;
  	PrintStream out = new PrintStream(exch.getResponseBody());
	  for (Map.Entry<Path, Controller> e: routes.entrySet()) {
	  	Path path = e.getKey();
	  	Controller ctr = e.getValue();
	    try {
	      if(path.match(exch.getRequestURI().getPath())){
	      	mem = new ByteArrayOutputStream();
	      	int httCode = ctr.handle(
	      			new PrintStream(mem), 
	      			path.getParameters(), 
	      			exch.getRequestBody(), 
	      			exch.getResponseHeaders(),
	      			exch.getRequestMethod());
	        // first sends the header response
	    		exch.sendResponseHeaders(httCode, 0);
	    		// Sends body response
	        mem.writeTo(exch.getResponseBody());
	      	mem.flush();
	      	exch.close();
	      	return;	  
	      }
      } catch (ResolverException excp) {
        // first sends the header response
      	exch.sendResponseHeaders(excp.httCode, 0);
      	// Sends body response
      	out.print(excp.message);
      	out.flush();
      	out.close();
      	exch.close();
      	return;
      }catch (Exception excp) {
        // just sends the header response
      	exch.sendResponseHeaders(500, 0);
      	// Sends body response
      	excp.printStackTrace(out);
      	out.flush();
      	out.close();
      	exch.close();
      	return;
      }
    }
    // first sends the header response
		exch.sendResponseHeaders(404, 0);
		// Sends body response
  	out.print("webfast.Resolver did not find any Controller for the specified path: " + 
  			exch.getRequestURI().getPath());
  	out.flush();
  	out.close();
  	exch.close();
  }
}
