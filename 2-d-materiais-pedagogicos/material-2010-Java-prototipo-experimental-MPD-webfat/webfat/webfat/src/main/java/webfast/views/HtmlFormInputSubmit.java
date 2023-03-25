package webfast.views;

import java.io.PrintStream;
import webfast.HandlerOutput;

@SuppressWarnings("rawtypes")
public class HtmlFormInputSubmit implements HandlerOutput{
	final String value;
	public HtmlFormInputSubmit(String value) {
	  this.value = value;
  }
	@Override
	public void print(PrintStream responseBody, int depth, Object model) {
		responseBody.print("<input type=\"submit\" value=\""+ value + "\"/>");
	}
}
