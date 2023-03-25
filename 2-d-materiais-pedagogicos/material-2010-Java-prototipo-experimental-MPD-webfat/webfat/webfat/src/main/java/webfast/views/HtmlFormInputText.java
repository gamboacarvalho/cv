package webfast.views;

import java.io.PrintStream;
import webfast.HandlerOutput;

@SuppressWarnings("rawtypes")
public class HtmlFormInputText implements HandlerOutput{
	
	final String name;
	final String id;
	
	public HtmlFormInputText(String name) {
	  this.name = name;
	  this.id = null;
  }
	public HtmlFormInputText(String name, String id) {
	  this.name = name;
	  this.id = id;
  }
	@Override
	public void print(PrintStream responseBody, int depth, Object model) {
		responseBody.print("<input type=\"text\" name=\""+ name + "\"");
		if(id != null) responseBody.print(" id = \"" + id + "\"");
		responseBody.print("/>");
	}
}
