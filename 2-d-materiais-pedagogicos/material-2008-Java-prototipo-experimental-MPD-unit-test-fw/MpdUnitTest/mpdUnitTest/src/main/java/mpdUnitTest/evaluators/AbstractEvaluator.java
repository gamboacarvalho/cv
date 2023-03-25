package mpdUnitTest.evaluators;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import mpdUnitTest.ITestMethodEvaluator;

/**
 * Simplifies the implementation of <code>ITestMethodEvaluator</code>.
 * 
 */
public abstract class AbstractEvaluator implements ITestMethodEvaluator {

	/**
	 * Simplifies the invocation of a static method that requires no arguments.
	 * 
	 * @param testMethod					The method to invoke.
	 * @return								The result of the invocation of the method.
	 * @throws InvocationTargetException	If the underlying method throws an exception. 
	 */
	protected Object invokeStaticNoArgs(Method testMethod) throws InvocationTargetException {
		Object objectInstance = null;
		Object result = null;
		try {
			result = testMethod.invoke(objectInstance);
		} catch (IllegalArgumentException e) {
			throwRuntimeException();
		} catch (IllegalAccessException e) {
			throwRuntimeException();
		}
		return result; 		 
	}

	/**
	 * Throws a <code>RuntimeException</code> saying that the method is not supported.
	 * @throws RuntimeException always.
	 */
	private void throwRuntimeException() throws RuntimeException {
		throw new RuntimeException("The " + this.getAnnotationType().getSimpleName() + " annotation is only supported in static methods without arguments.");
	}
	


	/**
	 * Fetches the evaluator's annotation in the specified method.
	 * 
	 * @param testMethod						The method that is decorated with the annotation.
	 * @return									The annotation.
	 * @throws UnsupportedOperationException	If the annotation isn't present.
	 */
	protected Annotation getAnnotation(Method testMethod) {
		Annotation annotation = testMethod.getAnnotation(this.getAnnotationType());
		if (annotation == null)
			throw new UnsupportedOperationException("Cannot evaluate a method that is not decorated with the " + this.getAnnotationType().getSimpleName() + " annotation.");
		return annotation;
	}
}
