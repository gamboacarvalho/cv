package webfast.views;

import java.io.PrintStream;

import webfast.HandlerOutput;

@SuppressWarnings("rawtypes")
public class HtmlBr implements HandlerOutput{
	@Override
	public void print(PrintStream  responseBody, int depth, Object model) {
		responseBody.println();
		for (int i = 0; i < depth; i++) responseBody.print("\t");
		responseBody.println("<br/>");
		for (int i = 0; i < depth; i++) responseBody.print("\t");
	}
}
