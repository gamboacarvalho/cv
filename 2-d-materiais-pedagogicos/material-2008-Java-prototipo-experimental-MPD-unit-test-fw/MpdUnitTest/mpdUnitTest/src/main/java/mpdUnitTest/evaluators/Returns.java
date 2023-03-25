package mpdUnitTest.evaluators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A method decorated with this annotation is evaluated for it's
 * return value. 
 * <br>The <code>ITestMethodEvaluator</code> responsible for evaluating
 * methods decorated with this annotation is <code>ReturnsEvaluator</code>.
 * 
 * @see mpdUnitTest.evaluators.ReturnsEvaluator
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Returns {
	/**
	 * @return	The representation of the expected return value.
	 */
	String value();
}
