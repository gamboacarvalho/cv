package webfast.views;

import java.io.PrintStream;

import webfast.HandlerOutputComposite;

public class HtmlTh extends HandlerOutputComposite{
	
	public void text(String msg){addChild(new TextNodeConst(msg));}
	
	@Override
  public void doPrintBefore(PrintStream writer, int depth, Object model) {
	  writer.print("<th>");
  }
	@Override
  public void doPrintAfter(PrintStream writer, int depth) {
		writer.print("</th>");
  }
}
