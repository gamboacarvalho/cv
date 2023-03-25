package mpdUnitTest;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.util.HashSet;

/**
 * Uses the provided evaluators to test the provided classes and 
 * shows the results to the provided viewers.
 *
 */
public final class TestEngine {

	private final HashSet<ITestResultViewer> viewers;
	private final HashSet<ITestMethodEvaluator> evaluators;
	
	/**
	 * Creates a new instance of <code>TestEngine</code>.
	 *  
	 * @param viewers		Instances of <code>ITestResultViewer</code> that will be notified of the tests progresses.
	 * @param evaluators	Instances of <code>ITestMethodEvaluator</code> that will be used to evaluate the test methods.
	 */
	public TestEngine(ITestResultViewer[] viewers, ITestMethodEvaluator[] evaluators) {
		// Only keep the valid references
		this.evaluators = new HashSet<ITestMethodEvaluator>();
		for (ITestMethodEvaluator evaluator : evaluators)
			if (evaluator != null)
				this.evaluators.add(evaluator);
		
		this.verifyEvaluatorAnnotations();
		
		// Only keep the valid references
		this.viewers = new HashSet<ITestResultViewer>();
		for (ITestResultViewer viewer : viewers)
			if (viewer != null)
				this.viewers.add(viewer);

	}

	/**
	 * Starts a new testing session in the specified classes.
	 * <br>The progress of the test session is transmitted to the viewers.
	 * 
	 * @param testClasses	The classes to be tested.
	 */
	public void test(Class<?>[] testClasses) {
		// Notify the viewers that a new testing session has started
		this.notifyViewersStartedTestSession();
		
		// Test each class ...
		boolean testSessionSuccess = true;
		for (Class<?> testClass : testClasses)
			if (testClass != null) // ... unless its null
				testSessionSuccess &= this.test(testClass);
		
		// Notify the viewers of the final success
		this.notifyViewersEndedTestSession(testSessionSuccess);
	}

	/**
	 * Tests the specified class.
	 * <br>For a class to be tested it must be decorated with
	 * the <code>TestClass</code> annotation. If the annotation 
	 * isn't present the class will not be tested.
	 * <br>A class is tested by testing all of its declared methods.
	 * <br>The progress of the test is transmitted to the viewers.
	 * 
	 * @param testClass		The class to be tested.
	 * @return				The result of the test. 
	 * <code>true</code> if the test was successful, <code>false</code> otherwise. 
	 */
	private boolean test(Class<?> testClass) {
		// The annotation to look for
		Class<TestClass> annotationClass = TestClass.class;
		
		// Verify that the class needs testing (look for the TestClassAttribute)
		if (!testClass.isAnnotationPresent(annotationClass))
			return true;
		
		// Get the test class name and description
		String testClassName = testClass.getSimpleName();
		TestClass annotation = testClass.getAnnotation(annotationClass);
		String description = annotation.value();
		
		// Notify the viewers that the class will now be tested
		this.notifyViewersStartedClassTest(testClassName, description);
		
		// Fetch the classe's methods
		Method[] classTestMethods = testClass.getDeclaredMethods();
		
		// Test each method
		boolean classSuccess = true;
		for (Method m : classTestMethods)
			classSuccess &= test(m);
		
		// Notify the viewers of the class' success
		notifyViewersEndedClassTest(classSuccess);
		return classSuccess;
	}

	/**
	 * Tests the specified method.
	 * <br>For a method to be tested it must be decorated with
	 * the <code>TestMethod</code> annotation. If the annotation 
	 * isn't present the method will not be tested.
	 * The method is queried for the presence of the known 
	 * evaluator's annotations. For each annotation found
	 * in the method, the method is evaluated in the 
	 * annotation's respective evaluator.
	 * <br>The progress of the test is transmitted to the viewers.
	 * 
	 * @param testMethod	The method to be tested.
	 * @return				The result of the test. 
	 * <code>true</code> if the test was successful, <code>false</code> otherwise. 
	 */
	private boolean test(Method testMethod){
		// Verify that the method needs testing (look for the TestMethodAttribute)
		if (!testMethod.isAnnotationPresent(TestMethod.class))
			return true;
		
		// Notify the viewers that the method will now be tested
		this.notifyViewersStartedMethodTest(testMethod.getName());
		
		// Start testing the methods
		boolean methodSuccess = true;

		// For each evaluator
		for (ITestMethodEvaluator evaluator : this.evaluators) {				
			// Find the evaluator's corresponding annotation 
			Class<? extends Annotation> annotation = evaluator.getAnnotationType();
			
			// Annotation is not null. We checked.
			
			// Check if the annotation is present
			if (!testMethod.isAnnotationPresent(annotation))
				continue;
						
			// If the annotation exists execute the evaluator
			EvaluationResult result = evaluator.evaluate(testMethod);
			
			// If the evaluator fails to provide a result to an evaluation throw a runtime exception
			if (result == null) {
				String badImplName = evaluator.getClass().getName();
				throw new RuntimeException("ITestMethodEvaluator implementation named " + badImplName + " fails to provide a result to an evaluation.");
			}
			
			// Update the test method's success
			methodSuccess &= result.Success; 
			
			// Notify the viewers of the evaluation's result
			this.notifyViewersMethodEvaluation(result);
		}

		// Notify the viewers of the method's success
		this.notifyViewersEndedMethodTest(methodSuccess);
		
		return methodSuccess;
	}
	
	/**
	 * Verifies if all of the evaluators succeed in providing a valid annotation type reference
	 * and also, one that is retained in runtime.  
	 */
	private void verifyEvaluatorAnnotations() {
		for (ITestMethodEvaluator evaluator : this.evaluators) {
			
			Class<? extends Annotation> annotation = evaluator.getAnnotationType();
			
			String badImplName = evaluator.getClass().getSimpleName();
			
			// If the evaluator fails to provide its annotation throw a runtime exception.
			if (annotation == null) 
				throw new RuntimeException("ITestMethodEvaluator implementation named " + badImplName + " fails to provide an annotation type.");
						
			Class<Retention> retentionAnnotation = Retention.class;
			Retention r = annotation.getAnnotation(retentionAnnotation);
			
			// If the annotation provided by the evaluator is not retained at runtime throw a runtime exception.
			if (r == null || !r.value().equals(RetentionPolicy.RUNTIME))
				throw new RuntimeException("ITestMethodEvaluator implementation named " + badImplName + " fails to provide a run time retained annotation type. REMINDER: For an annotation to be retained in runtime it must be annotated with @Retention(RetentionPolicy.RUNTIME).");
		}
	}
	
	/**
	 * Notifies all viewers that a test session has started.
	 */
	private void notifyViewersStartedTestSession() {
		for (ITestResultViewer viewer : this.viewers)
			viewer.startedTestSession();
	}

	/**
	 * Notifies all viewers that the test session 
	 * has finished with the specified result.
	 * 
	 * @param testSessionSuccess	The result of the test session.
	 */
	private void notifyViewersEndedTestSession(boolean testSessionSuccess) {
		for (ITestResultViewer viewer : this.viewers)
			viewer.endedTestSession(testSessionSuccess);
	}
	
	/**
	 * Notifies all viewers that a class with the
	 * specified name and description is now
	 * being tested.
	 * 
	 * @param className		The name of the class being tested.
	 * @param description	The description of the class being tested.
	 */
	private void notifyViewersStartedClassTest(String className, String description) {
		for (ITestResultViewer viewer : this.viewers)
			viewer.startedClassTest(className, description);
	}
	
	/**
	 * Notifies all viewers that the current class 
	 * test is finished with the specified result.
	 * 
	 * @param classSuccess	The result of the test.
	 */
	private void notifyViewersEndedClassTest(boolean classSuccess) {
		for (ITestResultViewer viewer : this.viewers)
			viewer.endedClassTest(classSuccess);
	}

	/**
	 * Notifies all viewers that a method with the 
	 * specified name is now being tested.
	 * 
	 * @param methodName	The name of the method being tested.
	 */
	private void notifyViewersStartedMethodTest(String methodName) {
		for (ITestResultViewer viewer : this.viewers)
			viewer.startedMethodTest(methodName);
	}
	
	/**
	 * Notifies all viewers that the current method 
	 * test is finished with the specified result.
	 * 
	 * @param methodSuccess	The result of the test.
	 */
	private void notifyViewersEndedMethodTest(boolean methodSuccess) {
		for (ITestResultViewer viewer : this.viewers)
			viewer.endedMethodTest(methodSuccess);
	}

	/**
	 * Notifies all viewers that an evaluation was performed
	 * on the current method with the specified result.
	 * 
	 * @see mpdUnitTest.EvaluationResult
	 * @param evaluationResult	Instance of <code>EvaluationResult</code> with the
	 * result of the evaluation.
	 */
	private void notifyViewersMethodEvaluation(EvaluationResult evaluationResult) {
		for (ITestResultViewer viewer : this.viewers)
			viewer.methodEvaluation(evaluationResult);
	}
}
