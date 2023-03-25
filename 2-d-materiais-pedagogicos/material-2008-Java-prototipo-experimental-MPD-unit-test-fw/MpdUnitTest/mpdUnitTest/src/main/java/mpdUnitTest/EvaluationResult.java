package mpdUnitTest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Represents the result of a method's single evaluation. 
 * <br>An instance of this class acts as a report for a method's evaluation.
 * <br>Instances of this class are supposed to be issued by the handler 
 * responsible for evaluating test methods marked with the specified annotation.
 *
 */
public final class EvaluationResult {
	/**
	 * The simple name of the class where the method is declarated.
	 */
	public final String ClassName;
	
	/**
	 * The name of the evaluated method.
	 */
	public final String MethodName;
	
	/**
	 * A message with information about the evaluation.
	 */
	public final String Message;
	
	/**
	 * The name of the annotation that caused this evaluation.
	 */
	public final String AnnotationName;
	
	/**
	 * The type of the annotation that caused this evaluation.
	 * @see java.lang.Class
	 */
	public final Class<? extends Annotation> AnnotationType;
	
	/**
	 * Indicates whether the evaluation had a positive result.
	 */
	public final boolean Success;

	/**
	 * Creates a new instance of EvaluationResult.
	 * 
	 * @param success			Whether or not the evaluation had a positive result.
	 * @param testMethod		The evaluated method.
	 * @param annotationType	The annotation type.
	 * @param message			Information about the evaluation
	 */
	public EvaluationResult(boolean success, Method testMethod,
			Class<? extends Annotation> annotationType, String message) {
		
		this.ClassName = testMethod.getDeclaringClass().getSimpleName();
		this.MethodName = testMethod.getName();
		this.Message = message;
		this.Success = success;
		this.AnnotationName = annotationType.getSimpleName();
		this.AnnotationType = annotationType;
	}
}
