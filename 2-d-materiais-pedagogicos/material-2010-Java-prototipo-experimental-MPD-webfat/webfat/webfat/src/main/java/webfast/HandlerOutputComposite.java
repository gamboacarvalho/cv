package webfast;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("rawtypes")
public abstract class HandlerOutputComposite implements HandlerOutput{
	
	List<HandlerOutput> children = new LinkedList<HandlerOutput>();
	
	public<T extends HandlerOutput> T addChild(T child){
		children.add(child);
		return child;
	}
	
	@SuppressWarnings("unchecked")
  @Override
  public void print(PrintStream  responseBody, int depth, Object model) { 
		doPrintBefore(responseBody, depth++, model);
		for (HandlerOutput elem : children) {
	    elem.print(responseBody, depth, model);
    }
		doPrintAfter(responseBody, --depth);
  }
	public abstract void doPrintBefore(PrintStream writer, int depth, Object model);
	public abstract void doPrintAfter(PrintStream writer, int depth);
	protected static void tabs(PrintStream writer, int depth){
		for (int i = 0; i < depth; i++) writer.print("\t");
	}
}