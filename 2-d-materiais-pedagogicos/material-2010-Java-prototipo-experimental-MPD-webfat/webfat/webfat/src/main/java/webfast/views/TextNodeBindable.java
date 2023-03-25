package webfast.views;

import java.io.PrintStream;

import webfast.HandlerOutput;
import webfast.ModelBinder;

public class TextNodeBindable<T> implements HandlerOutput<T>{

	private final ModelBinder<T> binder;
	public TextNodeBindable(ModelBinder<T> binder) {
		this.binder = binder;
	}
	@Override
	public void print(PrintStream  responseBody, int depth,  T model) {
		binder.bind(model, responseBody);
	}
}
