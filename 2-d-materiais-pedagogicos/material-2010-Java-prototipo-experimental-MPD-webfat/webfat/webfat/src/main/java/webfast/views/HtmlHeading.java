package webfast.views;

import java.io.PrintStream;

import webfast.HandlerOutput;

@SuppressWarnings("rawtypes")
public class HtmlHeading implements HandlerOutput{

	private final String msg;
	private final int level;
	public HtmlHeading(int level, String msg) {
		this.level = level; 
		this.msg = msg;
	}
	@Override
	public void print(PrintStream  responseBody, int depth, Object model) {
		responseBody.print("<h" + level + ">");
		responseBody.print(msg);
		responseBody.println("</h" + level + ">");
		for (int i = 0; i < depth; i++) responseBody.print("\t");
	}
}
