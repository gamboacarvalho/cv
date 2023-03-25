package webfast.views;

import java.io.PrintStream;
import webfast.HandlerOutput;

@SuppressWarnings("rawtypes")
public class HtmlFormSelect implements HandlerOutput{
	final String name;
	final String[] options;
	public HtmlFormSelect(String name, String...options) {
	  this.name = name;
	  this.options = options;
  }
	@Override
	public void print(PrintStream responseBody, int depth, Object model) {
		responseBody.println("<select name=\""+ name+ "\">");
		tabs(responseBody, ++depth);
		for (String op : options) {
			responseBody.println("<option>" + op + "</option>");
			tabs(responseBody, depth);
    }
		
		tabs(responseBody, --depth);
		responseBody.println("</select>");
		tabs(responseBody, depth);
	}
	private static void tabs(PrintStream responseBody, int depth){
		for (int i = 0; i < depth; i++) responseBody.print("\t");
	}
}
