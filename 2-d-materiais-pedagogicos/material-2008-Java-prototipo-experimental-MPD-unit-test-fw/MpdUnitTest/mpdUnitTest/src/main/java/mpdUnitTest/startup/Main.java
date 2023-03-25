package mpdUnitTest.startup;

import mpdUnitTest.ITestMethodEvaluator;
import mpdUnitTest.ITestResultViewer;
import mpdUnitTest.TestEngine;

/**
 * The executable class.
 *
 */
public final class Main {
	
	/**
	 * The program's point of entry.
	 * <br>1. Loads the startup info whether from program arguments
	 * or from a configuration file.
	 * <br>2. Loads the test classes.
	 * <br>3. Instantiates the viewers.
	 * <br>4. Instantiates the method evaluators.
	 * <br>5. Starts the test engine.
	 * 
	 * @param args	The program's arguments.
	 */
	public static void main(String[] args) {
		// Load startup info
		StartupInfo confSi = StartupConfigLoader.loadStartupInfoFromDefaultFile();
		StartupInfo argsSi = StartupConfigLoader.loadStartupInfoFromArgs(args);
		
		ResourceLoader resourceLoader = new ResourceLoader(confSi);
		resourceLoader.add(argsSi);
	
		// Load the test classes
		Class<?>[] testClasses = resourceLoader.getTestClasses();
				
		// Load and instantiate the viewers
		ITestResultViewer[] viewers = resourceLoader.getViewers();
		
		// Load and instantiate the evaluators
		ITestMethodEvaluator[] evaluators = resourceLoader.getEvaluators();
		
		if (testClasses.length == 0)
			throw new RuntimeException("No test classes were indicated from program arguments nor from the configuration file.");
		if (viewers.length == 0)
			throw new RuntimeException("No viewers were indicated from program arguments nor from the configuration file.");
		if (evaluators.length == 0)
			throw new RuntimeException("No evaluators were indicated from program arguments nor from the configuration file.");
		
		// Start the engine
		TestEngine testEngine = new TestEngine(viewers, evaluators);
		
		// Test the classes
		testEngine.test(testClasses);
	}
}
