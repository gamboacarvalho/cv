package webfast.views;

import java.io.PrintStream;

import webfast.HandlerOutputComposite;

public class HtmlTd extends HandlerOutputComposite{
	
	public HtmlA a(String href){return addChild(new HtmlA(href));}
	public HtmlTd text(String msg){addChild(new TextNodeConst(msg)); return this;}
	
	@Override
  public void doPrintBefore(PrintStream writer, int depth, Object model) {
	  writer.print("<td>");
  }
	@Override
  public void doPrintAfter(PrintStream writer, int depth) {
		writer.print("</td>");
  }
}
