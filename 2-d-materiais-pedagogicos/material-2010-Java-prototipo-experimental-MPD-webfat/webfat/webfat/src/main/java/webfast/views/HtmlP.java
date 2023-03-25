package webfast.views;

import java.io.PrintStream;

import webfast.HandlerOutput;

public class HtmlP implements HandlerOutput{
	final String msg;

	public HtmlP(String msg) {
	  super();
	  this.msg = msg;
  }

	@Override
  public void print(PrintStream responseBody, int depth, Object model) {
	  responseBody.print("<p>"+ msg +"</p>");
  }
}
