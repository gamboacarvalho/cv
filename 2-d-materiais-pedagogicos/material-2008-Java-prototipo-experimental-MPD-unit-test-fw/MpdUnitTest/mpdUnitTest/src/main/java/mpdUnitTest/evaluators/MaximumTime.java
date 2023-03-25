package mpdUnitTest.evaluators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A method decorated with this annotation is evaluated for it's
 * execution time. The evaluation is considered failed if the
 * time necessary for the method to run is superior to the 
 * one indicated in the annotation. 
 * <br>The <code>ITestMethodEvaluator</code> responsible for evaluating
 * methods decorated with this annotation is <code>MaximumTimeEvaluator</code>.
 * 
 * @see mpdUnitTest.evaluators.MaximumTimeEvaluator
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MaximumTime {
	/**
	 * @return The maximum amount of time for the method to run in milliseconds.
	 */
	int value();
}
