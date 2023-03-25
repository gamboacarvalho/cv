package webfast.views;

import java.io.PrintStream;

import webfast.HandlerOutput;

@SuppressWarnings("rawtypes")
public class HtmlLinkCss implements HandlerOutput{
	final String href;
	public HtmlLinkCss(String href) {
	  super();
	  this.href = href;
  }
	@Override
  public void print(PrintStream responseBody, int depth, Object model) {
		responseBody.println("<link rel=\"Stylesheet\" type=\"text/css\" href=\"" + href + "\"/>");
		for (int i = 0; i < depth; i++) responseBody.print("\t");
  }
}
