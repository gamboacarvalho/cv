package mpdUnitTest.startup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads all the configuration including
 * test classes, method evaluators and viewers.
 *
 */
public final class StartupConfigLoader {
	
	private static final String CONFIGURATION_FILENAME = "mpdUnitTest.config";
	
	private static final String TEST_CLASSES_ARG_PREFIX = "/c:";
	private static final String VIEWERS_ARG_PREFIX = "/v:";
	private static final String EVALUATORS_ARG_PREFIX = "/e:";

	private static final String COMMENT_PREFIX = "#";	
	
	/**
	 * Loads startup information from the default configuration file
	 * if one exists.
	 * @return Startup information from the default configuration file.
	 */
	public static StartupInfo loadStartupInfoFromDefaultFile() {
		if (new File(CONFIGURATION_FILENAME).exists())
			return loadStartupInfoFromFile(CONFIGURATION_FILENAME);
		String[] emptyStringArray = new String[0];
		return new StartupInfo(emptyStringArray, emptyStringArray, emptyStringArray);
	}

	/**
	 * Loads startup information from the specified program arguments. 
	 * @param args	Program arguments.
	 * @return		Startup information extracted from the specified program arguments.
	 */
	public static StartupInfo loadStartupInfoFromArgs(String[] args) {		
		List<String> 
			testClassNamesList = new ArrayList<String>(),
			viewerNamesList = new ArrayList<String>(),
			evaluatorNamesList = new ArrayList<String>();
		
		for (String arg : args) {			
			if (compareArg(arg, TEST_CLASSES_ARG_PREFIX))
				testClassNamesList.add(getArgContent(arg));
			else if (compareArg(arg, VIEWERS_ARG_PREFIX))
				viewerNamesList.add(getArgContent(arg));
			else if (compareArg(arg, EVALUATORS_ARG_PREFIX))
				evaluatorNamesList.add(getArgContent(arg));	
			else throw new RuntimeException("The argument " + arg + " is invalid.");
		}
		
		// Transform to array
		String[] testClassNames = listToStringArray(testClassNamesList);
		String[] viewersNames =listToStringArray(viewerNamesList);
		String[] evaluatorNames = listToStringArray(evaluatorNamesList);
		
		return new StartupInfo(testClassNames, viewersNames, evaluatorNames);
	}
	
	/**
	 * Loads startup information from the specified file.
	 * @param configurationFilename		The filename.
	 * @return Startup 					Information from the specified file.
	 */
	public static StartupInfo loadStartupInfoFromFile(String configurationFilename) {
		List<String> args = new ArrayList<String>();
		
		BufferedReader bReader = null;
		try {
			bReader = new BufferedReader(new FileReader(configurationFilename));
			String line = null;
			while ((line = bReader.readLine()) != null)
				if (!line.startsWith(COMMENT_PREFIX) && !line.isEmpty())
					args.add(line);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Cannot find or open the file " + configurationFilename + ".");
		} catch (IOException e) {
			throw new RuntimeException("Cannot read from the file " + configurationFilename + ".");
		} finally {
			if (bReader != null)
				try {
					bReader.close();
				} catch (IOException e) { e.printStackTrace(); }
		}
		
		return loadStartupInfoFromArgs(listToStringArray(args));
	}

	/**
	 * Transforms a list of <code>String</code> to a <code>List<String></code>.
	 * @param stringList	The list of strings.
	 * @return				An array of strings.
	 */
	private static String[] listToStringArray(List<String> stringList) {
		String[] testClassesNames = new String[stringList.size()];
		testClassesNames = stringList.toArray(testClassesNames);
		return testClassesNames;
	}

	/**
	 * Extracts the content from the specified argument value.
	 * e.g. "/a:value" returns "value".
	 * 
	 * @param arg	The argument.
	 * @return		The extracted value.
	 */
	private static String getArgContent(String arg) {
		int contentBeginIndex = 3;
		String content = arg.substring(contentBeginIndex );
		content = content.trim();
		return content;
	}

	/**
	 * Checks if the specified argument starts with the specified argument prefix.
	 * @param arg		The argument.
	 * @param prefix 	The argument prefix.
	 * @return			<code>true</code> if the argument starts with the prefix.
	 */
	private static boolean compareArg(String arg, String prefix) {
		String argPrefix = arg.substring(0, 3).toLowerCase();
		return argPrefix.startsWith(prefix);
	}
}
