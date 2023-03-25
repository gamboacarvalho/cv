package webfast.views;

import java.io.PrintStream;
import webfast.HandlerOutputComposite;

public class HtmlTr extends HandlerOutputComposite{
	
	public HtmlTd td(){return addChild(new HtmlTd());}
	public HtmlTh th(){return addChild(new HtmlTh());}
	@Override
  public void doPrintBefore(PrintStream writer, int depth, Object model) {
	  writer.println("<tr>");
	  tabs(writer, ++depth);
  }
	@Override
  public void doPrintAfter(PrintStream writer, int depth) {
		writer.println("");
		tabs(writer, depth);
		writer.println("</tr>");
		tabs(writer, depth);
  }
}
