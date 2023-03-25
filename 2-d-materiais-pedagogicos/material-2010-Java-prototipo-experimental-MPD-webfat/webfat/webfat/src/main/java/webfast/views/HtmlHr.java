package webfast.views;

import java.io.PrintStream;

import webfast.HandlerOutput;

@SuppressWarnings("rawtypes")
public class HtmlHr implements HandlerOutput{
	@Override
	public void print(PrintStream  responseBody, int depth, Object model) {
		responseBody.println();
		for (int i = 0; i < depth; i++) responseBody.print("\t");
		responseBody.println("<hr/>");
		for (int i = 0; i < depth; i++) responseBody.print("\t");
	}
}
