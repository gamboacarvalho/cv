package mpdUnitTest.evaluators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A method decorated with this annotation is evaluated 
 * for exception throwing.
 * <br>The method is considered OK if it throws the 
 * exception specified in this annotation.
 * <br>If no exception is specified the method is expected
 * not to throw any exception.
 * <br>The <code>ITestMethodEvaluator</code> responsible for evaluating
 * methods decorated with this annotation is <code>ExpectedExceptionEvaluator</code>.
 * 
 * @see mpdUnitTest.evaluators.ExpectedExceptionEvaluator
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExpectedException {
	/**
	 * @return	The expected exception. Or null, or empty, if no exception is expected. 
	 */
	String value();
}
