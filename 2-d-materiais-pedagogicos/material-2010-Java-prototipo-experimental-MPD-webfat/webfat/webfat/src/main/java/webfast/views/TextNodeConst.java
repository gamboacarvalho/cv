package webfast.views;

import java.io.PrintStream;

import webfast.HandlerOutput;

@SuppressWarnings("rawtypes")
public class TextNodeConst implements HandlerOutput{

	private final String msg;
	public TextNodeConst(String msg) {
		this.msg = msg;
	}
	@Override
	public void print(PrintStream  responseBody, int depth, Object model) {
		responseBody.print(msg);
	}
}
