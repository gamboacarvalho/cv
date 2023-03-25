package webfast.views;

import java.io.PrintStream;
import webfast.HandlerOutputComposite;
import webfast.ModelBinder;

public class HtmlView extends HandlerOutputComposite{
	public HtmlHead head(){return addChild(new HtmlHead());}
	public HtmlBody body(){return addChild(new HtmlBody());}
	
	@Override
  public void doPrintBefore(PrintStream writer, int depth, Object model) {
	  writer.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
	  writer.println("<html xmlns=\"http://www.w3.org/1999/xhtml\" >");
  }
	@Override
  public void doPrintAfter(PrintStream writer, int depth) {
		writer.println("</html>");
  }
}
