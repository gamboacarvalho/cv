package webfast;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ResourcesHandler implements HttpHandler {
	@Override
	public void handle(HttpExchange exch) throws IOException {
		String reqPath = exch.getRequestURI().getPath();
		if((reqPath.indexOf("/resources")) < 0) 
			error(exch, 404, "Request is not for a resource!");
		if((!reqPath.endsWith(".js") && !reqPath.endsWith(".css")))
			error(exch, 404, "Resource not supported!");
		
		String resourcePath = System.getProperty("user.dir") + exch.getRequestURI().getPath();
		InputStream in = new FileInputStream(resourcePath);
		OutputStream out = exch.getResponseBody();
		
		// first sends the header response
		exch.sendResponseHeaders(200, 0);
		
		// Transfer bytes from in to out
    byte[] buf = new byte[1024];
    int len;
    while ((len = in.read(buf)) > 0) {
        out.write(buf, 0, len);
    }
    in.close();
    out.flush();
    out.close();
		exch.close();
	}
	public void error(HttpExchange exch, int httpCode, String message) throws IOException{
		// first sends the header response
		exch.sendResponseHeaders(httpCode, 0);
		// Sends body response
		PrintStream out = new PrintStream(exch.getResponseBody());
		out.print(message);
		out.flush();
		out.close();
		exch.close();
		return;
	}
}
