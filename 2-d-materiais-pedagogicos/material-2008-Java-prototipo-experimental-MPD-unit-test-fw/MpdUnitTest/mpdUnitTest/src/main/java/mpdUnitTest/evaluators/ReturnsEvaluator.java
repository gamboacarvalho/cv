package mpdUnitTest.evaluators;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import mpdUnitTest.EvaluationResult;


/**
 * Evaluator for a test method's return value.
 * <br>This evaluator checks if the return value of
 * the test method has the same representation 
 * as the one indicated in the <code>Returns</code>
 * annotation.
 * 
 * @see mpdUnitTest.evaluators.Returns
 */
public final class ReturnsEvaluator extends AbstractEvaluator { 
	
	public Class<? extends Annotation> getAnnotationType(){
		return Returns.class;
	}
	
	@Override
	public EvaluationResult evaluate(Method testMethod) {
		// Get the representation of the expected return value
		Returns annotation = (Returns) this.getAnnotation(testMethod);
		String expected = annotation.value();
		
		boolean success = false;
		String message = "OK.";
		try{
			Object objResult = this.invokeStaticNoArgs(testMethod);
			if (objResult != null){
				// Get the representation of the return value
				String result =  objResult .toString();
				
				// Compare
				if (!expected.equals(result))
					message = "Failed. Expected: " + expected + " . Result: " + result + " .";
				else 
					success = true;
			} else if (!expected.isEmpty()) {
				message = "Failed. Expected: " + expected + " . Result: <null> .";
			}
		} catch (InvocationTargetException e){
			// An unexpected exception was thrown in the test method
			Throwable innerException = e.getTargetException();
			message = "Failed. Exception was thrown: " + innerException.getClass().getSimpleName() + " : " + innerException.getMessage();
		}
		
		return new EvaluationResult(success, testMethod, getAnnotationType(), message);
	}
}
