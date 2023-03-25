package mpdUnitTest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Interface for a method evaluator.
 * <br>A method evaluator must define its annotation type.
 * An evaluator's annotation type defines when to evaluate a method.
 * The methods annotated with the specified annotation type
 * will be delegated from a <code>TestEngine</code> to the implementations
 * of this interface so an evaluation can be performed.
 * 
 * @see mpdUnitTest.TestEngine
 *
 */
public interface ITestMethodEvaluator {
	/**
	 * Gets the evaluator's corresponding annotation type.
	 * <br>Only the test methods annotated with this type of 
	 * annotation will be delegated to this evaluator.
	 * @return	The annotation type.
	 */
	public Class<? extends Annotation> getAnnotationType();
	
	/**
	 * Performs an evaluation to the specified method.
	 * <br>This method should only be invoked by <code>TestEngine</code>
	 * and only when the method is decorated with the evaluator's
	 * corresponding annotation.
	 * <br>The result is an instance of <code>EvaluationResult</code>
	 * with information about the evaluation.
	 * 
	 * @param testMethod	The method to evaluate.
	 * @return				An instance of <code>EvaluationResult</code> 
	 * 						with the results of the evaluation.
	 * 
	 * @see mpdUnitTest.EvaluationResult
	 */
	EvaluationResult evaluate(Method testMethod);
}
