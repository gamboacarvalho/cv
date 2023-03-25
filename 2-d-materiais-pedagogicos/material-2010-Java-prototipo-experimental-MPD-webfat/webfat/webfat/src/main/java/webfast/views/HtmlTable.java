package webfast.views;

import java.io.PrintStream;

import webfast.HandlerOutputComposite;

public class HtmlTable extends HandlerOutputComposite{
	public HtmlTr tr(){
		return addChild(new HtmlTr());
	}
	@Override
  public void doPrintBefore(PrintStream writer, int depth, Object model) {
	  writer.println("<table>");
	  tabs(writer, ++depth);
  }
	@Override
  public void doPrintAfter(PrintStream writer, int depth) {
		writer.println("");
		tabs(writer, depth);
		writer.print("</table>");
		tabs(writer, depth);
  }
}
