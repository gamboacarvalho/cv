package webfast.views;

import java.io.PrintStream;

import webfast.HandlerOutput;

@SuppressWarnings("rawtypes")
public class HtmlScriptLink implements HandlerOutput{
	final String src;
	public HtmlScriptLink(String src) {
	  super();
	  this.src = src;
  }
	@Override
  public void print(PrintStream responseBody, int depth, Object model) {
		responseBody.println("<script type=\"text/javascript\" src=\"" + src + "\"></script>");
		for (int i = 0; i < depth; i++) responseBody.print("\t");
  }
}
