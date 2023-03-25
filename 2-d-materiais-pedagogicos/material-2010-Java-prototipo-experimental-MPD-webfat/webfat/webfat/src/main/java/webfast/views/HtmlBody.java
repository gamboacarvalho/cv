package webfast.views;

import java.io.PrintStream;
import webfast.HandlerOutputComposite;
import webfast.ModelBinder;

public class HtmlBody extends HandlerOutputComposite{	
	public HtmlBody heading(int level, String msg){addChild(new HtmlHeading(level, msg));return this;}
	public HtmlBody text(String msg){addChild(new TextNodeConst(msg));return this;}
	public<T> HtmlBody text(ModelBinder<T> binder){addChild(new TextNodeBindable<T>(binder));return this;}
	public HtmlBody br(){addChild(new HtmlBr());return this;}
	public HtmlBody hr(){addChild(new HtmlHr());return this;}
	public HtmlDiv div(){return addChild(new HtmlDiv());}
	public HtmlForm form(String action){return addChild(new HtmlForm(action));}
	public HtmlTable table(){return addChild(new HtmlTable());}
	public HtmlP p(String msg) { return addChild(new HtmlP(msg));}
	public HtmlA a(String href){return addChild(new HtmlA(href));}
	
	@Override
  public void doPrintBefore(PrintStream writer, int depth, Object model) {
		tabs(writer, depth);
	  writer.println("<body>");
	  tabs(writer, ++depth);
  }
	@Override
  public void doPrintAfter(PrintStream writer, int depth) {
		writer.println("");
		tabs(writer, depth);
		writer.println("</body>");
  }
}
