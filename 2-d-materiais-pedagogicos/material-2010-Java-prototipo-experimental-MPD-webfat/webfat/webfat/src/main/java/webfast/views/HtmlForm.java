package webfast.views;

import java.io.PrintStream;

import webfast.HandlerOutputComposite;

public class HtmlForm extends HandlerOutputComposite{
	public HtmlForm text(String msg){addChild(new TextNodeConst(msg));return this;}
	public HtmlForm br(){addChild(new HtmlBr());return this;}
	public HtmlForm select(String name, String...options){addChild(new HtmlFormSelect(name, options));return this;}
	public HtmlForm inputText(String name){addChild(new HtmlFormInputText(name));return this;}	
	public HtmlForm inputText(String name, String id){addChild(new HtmlFormInputText(name, id));return this;}
	public HtmlForm inputSubmit(String value){addChild(new HtmlFormInputSubmit(value));return this;}
	public HtmlA a(String href){return addChild(new HtmlA(href));}
	
	final private String action;
	
	public HtmlForm(String action) {
	  this.action = action;
  }
	
	@Override
  public void doPrintBefore(PrintStream writer, int depth, Object model) {
	  writer.println(String.format("<form action=\"%s\" method=\"%s\" enctype=\"%s\">",
	  		action,
	  		"post",
	  		"application/x-www-form-urlencoded"	  		
	  		));
	  tabs(writer, ++depth);
  }
	@Override
  public void doPrintAfter(PrintStream writer, int depth) {
		writer.println("");
		tabs(writer, depth);
	  writer.println("</form>");
	  tabs(writer, depth);
  }
}
