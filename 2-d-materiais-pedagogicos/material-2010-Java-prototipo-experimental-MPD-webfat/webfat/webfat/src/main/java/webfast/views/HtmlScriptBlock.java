package webfast.views;

import java.io.PrintStream;
import webfast.HandlerOutputComposite;

public class HtmlScriptBlock extends HandlerOutputComposite{
	
	public HtmlScriptBlock code(String msg){addChild(new TextNodeConst(msg));return this;}
	
	@Override
  public void doPrintBefore(PrintStream writer, int depth, Object model) {
	  writer.println("<script>");
	  tabs(writer, ++depth);
  }
	@Override
  public void doPrintAfter(PrintStream writer, int depth) {
		writer.println("");
		tabs(writer, depth);
		writer.println("</script>");
		tabs(writer, depth);
  }
}
