package mpdUnitTest.startup;

/**
 * Holds startup information .
 *
 */
public final class StartupInfo {
	public final String[] TestClassesNames;
	public final String[] ViewersNames;
	public final String[] EvaluatorNames;
	/**
	 * Creates a new instance of <code>StartupInfo</code> 
	 * with the specified configuration parameters.
	 * 
	 * @param testClassesNames	The names of the classes to test.
	 * @param viewersNames		The names of the viewers to use.
	 * @param evaluatorNames	The names of the evaluators to consider.
	 */
	public StartupInfo(String[] testClassesNames, String[] viewersNames, String[] evaluatorNames) {
		// Verify the arguments
		if (testClassesNames == null)
			throw new IllegalArgumentException("The argument testClasses cannot be null.");
		if (viewersNames == null)
			throw new IllegalArgumentException("The argument viewersNames cannot be null.");
		if (evaluatorNames == null)
			throw new IllegalArgumentException("The argument evaluatorNames cannot be null.");
		// Keep the arguments
		this.TestClassesNames = testClassesNames;
		this.ViewersNames = viewersNames;
		this.EvaluatorNames = evaluatorNames;
	}
}
