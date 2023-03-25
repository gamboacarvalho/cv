package webfast.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;

public class MockHttpExchange extends HttpExchange{
	private final URI uri;
	private final ByteArrayOutputStream respStream;
	private int responseCode;
	public MockHttpExchange(String path, ByteArrayOutputStream respStream) throws URISyntaxException {
	  this.uri = new URI(path);
	  this.respStream = respStream;
  }
  public URI getRequestURI() {return uri;}
  public OutputStream getResponseBody() {return respStream;}
  public int getResponseCode() {return responseCode;}
  public void sendResponseHeaders(int responseCode, long arg1) throws IOException {
  	this.responseCode = responseCode;
  }

	public void close() {}
  public Object getAttribute(String arg0){return null;}
  public HttpContext getHttpContext(){return null;}
  public InetSocketAddress getLocalAddress(){return null;}
  public HttpPrincipal getPrincipal(){return null;}
  public String getProtocol(){return null;}
  public InetSocketAddress getRemoteAddress(){return null;}
  public InputStream getRequestBody() {return null;}
  public Headers getRequestHeaders() {return null;}
  public String getRequestMethod() {return null;}
  public Headers getResponseHeaders() {return null;}
  public void setAttribute(String arg0, Object arg1) {}
  public void setStreams(InputStream arg0, OutputStream arg1) {}
}
