package webfast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import collect.IterUtils;
import collect.Projection;

import webfast.ControllerHandler.InvokeMehod;
import webfast.routing.PathPartParameter;

/**
 * This Controller will match the method that is annotated with
 * ControllerHandler and that has the larger number of compatible
 * parameters. 
 * @author mcarvalho
 */
public class AbstractController implements Controller{
	private List<Method> getHandlers = new LinkedList<Method>(); 
	private List<Method> postHandlers = new LinkedList<Method>();
	private Map<String, String> formData;
	private Object currentModel;
	private String location;

	public AbstractController(){
		Class<? extends AbstractController> controllerClass = this.getClass();
		Method[] ms = controllerClass.getMethods();
		for (Method m : ms) {
			ControllerHandler handlerAnnot = m.getAnnotation(ControllerHandler.class);
			if(handlerAnnot != null){
				if(m.getReturnType() != HandlerOutput.class && m.getReturnType() != int.class)
					throw new IllegalArgumentException("Annotated method " + m.getName() + " must return a value of type HandlerOutput" );
				if(handlerAnnot.value() == InvokeMehod.Get)
					getHandlers.add(m);
				else
					postHandlers.add(m);
			}
		}
		// Sort descending 
		Collections.sort(getHandlers, new Comparator<Method>() {
			@Override
			public int compare(Method m1, Method m2) {
				return m2.getParameterTypes().length - m1.getParameterTypes().length;
			}
		});
	}
	private static Method selectHandler(Iterable<PathPartParameter<?>> params, List<Method> handlers){
		for (Method h : handlers) {
			int idx = 0;
			for (PathPartParameter<?> p : params) {
				if(idx >= h.getParameterTypes().length)
					break;
				if(!h.getParameterTypes()[idx++].isAssignableFrom(p.getParamValue().getClass())){
					break;
				}
			}
			if(h.getParameterTypes().length == idx){
				h.setAccessible(true);
				return h;
			}
		}
		return null;
	}
	@SuppressWarnings({"rawtypes", "unchecked"})
	public int handle(
			PrintStream responseBody,
			Iterable<PathPartParameter<?>> params,
			InputStream requestBody,
			Headers headers,
			String requestMethod) throws Exception
	{
		Method h = null;
		if(isGet(requestMethod))
			h = selectHandler(params, getHandlers);
		else if(isPost(requestMethod)){
			h = selectHandler(params, postHandlers);
			formData = new HashMap<String, String>();
			BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody));
			String line = null, formDatastr = "";
			while((line = reader.readLine()) != null){formDatastr += line;}
			String [] postParams = formDatastr.trim().split("&");
			for (String p : postParams) {
		    String [] pair = p.trim().split("=");
		    formData.put(pair[0], pair.length == 2 ? pair[1] : null);
	    }
			formData = Collections.unmodifiableMap(formData);
		}
		if(h != null){
			Object [] paramsArr = new Object[h.getParameterTypes().length];
			IterUtils.copy(params, paramsArr, new Projection<PathPartParameter<?>, Object>() {
				public Object invoke(PathPartParameter<?> item) {
					return item.getParamValue();
				}
			});
			if(h.getReturnType() == HandlerOutput.class){
				HandlerOutput output = (HandlerOutput) h.invoke(this, paramsArr);
				output.print(responseBody, 0, currentModel);
				return 200;
			}else{
				int httpCode = (Integer) h.invoke(this, paramsArr);
				if((httpCode / 100) == 3) 
					headers.put("location", Arrays.asList(location));
				return httpCode;
			}
		}
		else
			throw new ResolverException(404, "Specified arguments does not full fill the required parameters for controller " + this.getClass().getName());
	}
	@SuppressWarnings("rawtypes")
	protected HandlerOutput view(HandlerOutput out, Object model){
		this.currentModel = model;
		return out;
	}
	protected int redirect(int httpCode, String location){
		this.location = location;
		return httpCode;
	}
	private static boolean isGet(String reqMethod) {
	  return reqMethod.toLowerCase().equals("get");
  }
	private static boolean isPost(String reqMethod) {
	  return reqMethod.toLowerCase().equals("post");
  }
	public Map<String, String> getFormData(){
		return formData;
	}
	public String getFormData(String key){
		return formData.get(key);
	}
}
