package mpdUnitTest;

/**
 * Interface for a viewer of the results of a test session.
 * <br>The interface provides an event-based way to provide the results.
 *
 */
public interface ITestResultViewer {
	/**
	 * Indicates the beginning of a new test session.
	 * <br>A test session is composed of class tests.
	 */
	void startedTestSession();
	
	/**
	 * Indicates the end of the current test session.
	 * <br>The session was successful if all the class tests were successful.
	 * 
	 * @param testSessionSuccess	Indicates whether or not the session was successful.
	 */
	void endedTestSession(boolean testSessionSuccess);
	
	/**
	 * Indicates the beginning of a new class test.
	 * <br>A class test is composed of method tests and a description.
	 * 
	 * @param className			The name of the test class.
	 * @param description		The description of the test case.
	 */
	void startedClassTest(String className, String description);
	
	/**
	 * Indicates the end of the current class test.
	 * <br>The class tests were successful if all the inner
	 * test methods were successful.
	 * 
	 * @param classSuccess		Indicates whether or not the class test was successful.
	 */
	void endedClassTest(boolean classSuccess);
	
	/**
	 * Indicates the beginnig of a new method's test.
	 * <br>A method test is composed of method's evaluations.
	 * 
	 * @param methodName		The name of the test method.
	 */
	void startedMethodTest(String methodName);
		
	/**
	 * Indicates the end of the current method's test.
	 * <br>The method's test was successful if all the 
	 * method's evaluation were successful.
	 * 
	 * @param methodSuccess		Indicates whether or not the method's test was successful.
	 */
	void endedMethodTest(boolean methodSuccess);
	
	/**
	 * Indicates that a method evaluation was performed.
	 * 
	 * @param evaluationResult	An instance of <code>EvaluationResult</code>
	 * 							with the results of the evaluation.
	 */
	void methodEvaluation(EvaluationResult evaluationResult);
}
