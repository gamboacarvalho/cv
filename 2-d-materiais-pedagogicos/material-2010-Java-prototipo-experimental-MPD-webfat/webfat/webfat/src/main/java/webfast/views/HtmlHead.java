package webfast.views;

import java.io.PrintStream;
import webfast.HandlerOutputComposite;
import webfast.ModelBinder;

public class HtmlHead extends HandlerOutputComposite{	
	public HtmlHead title(String msg){addChild(new HtmlTitle(msg));return this;}
	public HtmlHead scriptLink(String src){addChild(new HtmlScriptLink(src));return this;}
	public HtmlScriptBlock scriptBlock(){return addChild(new HtmlScriptBlock());}
	public HtmlHead linkCss(String href){addChild(new HtmlLinkCss(href));return this;}
	
	@Override
  public void doPrintBefore(PrintStream writer, int depth, Object model) {
	  tabs(writer, depth);
	  writer.println("<head>");
	  tabs(writer, depth+1);
  }
	@Override
  public void doPrintAfter(PrintStream writer, int depth) {
		writer.println("");
		tabs(writer, depth);
		writer.println("</head>");
  }
}
