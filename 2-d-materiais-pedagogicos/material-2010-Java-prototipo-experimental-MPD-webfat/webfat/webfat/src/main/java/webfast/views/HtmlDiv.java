package webfast.views;

import java.io.PrintStream;

import webfast.HandlerOutputComposite;
import webfast.ModelBinder;

public class HtmlDiv extends HandlerOutputComposite{
	public HtmlTable table(){return addChild(new HtmlTable());}
	public HtmlDiv text(String msg){addChild(new TextNodeConst(msg));return this;}
	public<T> HtmlDiv text(ModelBinder<T> binder){addChild(new TextNodeBindable<T>(binder));return this;}
	public HtmlDiv br(){addChild(new HtmlBr());return this;}
	public HtmlDiv hr(){addChild(new HtmlHr());return this;}
	public HtmlDiv div(){return addChild(new HtmlDiv());}
	public HtmlForm form(String action){return addChild(new HtmlForm(action));}
	public HtmlA a(String href){return addChild(new HtmlA(href));}
	public<T> HtmlA a(ModelBinder<T> href){return addChild(new HtmlA(href));}
	
	@Override
  public void doPrintBefore(PrintStream writer, int depth, Object model) {
	  writer.println("<div>");
	  for (int i = 0; i < depth+1; i++) writer.print("\t");
  }
	@Override
  public void doPrintAfter(PrintStream writer, int depth) {
		writer.println("");
		for (int i = 0; i < depth; i++) writer.print("\t");
		writer.println("</div>");
  }
}
