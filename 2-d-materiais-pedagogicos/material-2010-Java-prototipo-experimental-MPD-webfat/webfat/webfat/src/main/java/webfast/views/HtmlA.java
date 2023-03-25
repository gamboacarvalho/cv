package webfast.views;

import java.io.PrintStream;

import webfast.HandlerOutput;
import webfast.HandlerOutputComposite;
import webfast.ModelBinder;

public class HtmlA extends HandlerOutputComposite{
	public HtmlA text(String msg){addChild(new TextNodeConst(msg)); return this;}

	final String href;
	final ModelBinder hrefBinder;
	
	public HtmlA(String href) {
	  this.href = href;
	  this.hrefBinder = null;
  }
	public HtmlA(ModelBinder hrefBinder) {
	  this.href = null;
	  this.hrefBinder = hrefBinder;
  }

	@Override
  public void doPrintBefore(PrintStream writer, int depth, Object model) {
	  writer.print("<a href=\"");
	  if(href == null)
	  	hrefBinder.bind(model, writer);
	  else
	  	writer.print(href);
	  writer.print("\">");
  }
	@Override
  public void doPrintAfter(PrintStream writer, int depth) {
		writer.print("</a>");
  }
}
