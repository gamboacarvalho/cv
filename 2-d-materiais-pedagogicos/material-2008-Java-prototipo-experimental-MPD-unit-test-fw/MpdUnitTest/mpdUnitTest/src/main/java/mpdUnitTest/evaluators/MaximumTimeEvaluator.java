package mpdUnitTest.evaluators;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import mpdUnitTest.EvaluationResult;

/**
 * Evaluator for a test method's execution time.
 * This evaluator checks if the execution time of
 * the test method is acceptable based on the
 * one indicated in the <code>MaximumTime</code>
 * annotation.
 * 
 * @see mpdUnitTest.evaluators.MaximumTime
 */
public final class MaximumTimeEvaluator extends AbstractEvaluator {

	@Override
	public EvaluationResult evaluate(Method testMethod) {
		// Get the maximum time for the method to run.
		MaximumTime annotation = (MaximumTime) this.getAnnotation(testMethod);
		long maxTime = annotation.value();
		
		boolean success = false;
		String message = null;
		
		// Time-stamp to measure the time that takes to invoke the method
		long timeStamp = System.currentTimeMillis();;
		
		try{
			this.invokeStaticNoArgs(testMethod);
		} catch (InvocationTargetException e) {
			// An unexpected exception was thrown in the test method
			// however, this evaluator has no regard for exception throwing
			// the only concern is the time spent in the method's executin
			// whether an exception is thrown or not.
		}
		
		// Use timeStamp to calculate delta time
		long deltaTime = System.currentTimeMillis() - timeStamp;
		
		// Check if the time is within the accepted range (from 0 to the maximum time)
		if (deltaTime > maxTime)
			message = "Failed. Max time: " + maxTime + "ms. Time taken: " + deltaTime + "ms.";
		else {
			success = true;
			message = "OK. Time taken: " + deltaTime + "ms";
		}
		
		return new EvaluationResult(success, testMethod, getAnnotationType(), message);
	}

	@Override
	public Class<? extends Annotation> getAnnotationType() {
		return MaximumTime.class;
	}
}
