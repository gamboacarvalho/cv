package mpdUnitTest.evaluators;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import mpdUnitTest.EvaluationResult;

/**
 * Evaluator for a test method's return value.
 * This evaluator checks if the return value of
 * the test method has the same representation 
 * as the one indicated in the <code>ExpectedException</code>
 * annotation.
 * 
 * @see mpdUnitTest.evaluators.ExpectedException
 */
public final class ExpectedExceptionEvaluator extends AbstractEvaluator {

	@Override
	public EvaluationResult evaluate(Method testMethod) {
		// Get the name of the expected exception for the test method to throw
		ExpectedException annotation = (ExpectedException) this.getAnnotation(testMethod);
		
		// If there is no name, no exception is expected
		String expectedExceptionName = annotation.value();
		boolean exceptionExpected = expectedExceptionName != null && !expectedExceptionName.equals("");
		
		boolean success = false;
		String message = null;
		try{
			// Check is the method throws any exception 
			this.invokeStaticNoArgs(testMethod);
			
			// If no exception was expected OK, otherwise the evaluation failed 
			if (exceptionExpected)
				message = "Failed. No exception was thrown.";
			else {
				success = true;
				message = "OK.";
			}
		} catch (InvocationTargetException e) {
			// Get the exception thrown by the test method
			String exceptionClassName = e.getTargetException().getClass().getSimpleName();
			
			// If an exception was expected, and it is the right kind of exception, otherwise, the evaluation failed
			if (!exceptionExpected){
				message = "Failed. Exception thrown: " + exceptionClassName + ".";
			} else if (exceptionClassName.equals(expectedExceptionName)) {
				success = true;
				message = "OK.";
			} else {
				message = "Failed. Wrong exception thrown: " + exceptionClassName + ".";
			}
		}

		return new EvaluationResult(success, testMethod, getAnnotationType(), message);
	}

	@Override
	public Class<? extends Annotation> getAnnotationType() {
		return ExpectedException.class;
	}
}
