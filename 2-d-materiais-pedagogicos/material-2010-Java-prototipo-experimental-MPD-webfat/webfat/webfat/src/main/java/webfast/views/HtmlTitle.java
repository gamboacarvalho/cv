package webfast.views;

import java.io.PrintStream;

import webfast.HandlerOutput;

@SuppressWarnings("rawtypes")
public class HtmlTitle implements HandlerOutput{

	private final String msg;
	public HtmlTitle(String msg) {
		this.msg = msg;
	}
	@Override
	public void print(PrintStream  responseBody, int depth, Object model) {
		responseBody.println("<title>" + msg + "</title>");
		for (int i = 0; i < depth; i++) responseBody.print("\t");
	}
}
