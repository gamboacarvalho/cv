package mpdUnitTest.startup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import mpdUnitTest.ITestMethodEvaluator;
import mpdUnitTest.ITestResultViewer;

/**
 * Loads and instantiates classes based on some startup configuration information.
 *
 */
public final class ResourceLoader {

	private List<StartupInfo> startupInfo = new ArrayList<StartupInfo>();
	
	// Cache for the loaded and instantiated classes
	private Class<?>[] testClasses;
	private ITestMethodEvaluator[] evaluators;
	private ITestResultViewer[] viewers;

	private boolean transited;
	
	/**
	 * Constructs an instance of <code>ResourceLoader</code> with no startup information.
	 */
	public ResourceLoader() {}
	
	/**
	 * Constructs an instance of <code>ResourceLoader</code> with the specified startup information
	 * @param startupInfo An initial set of startup information.
	 */
	public ResourceLoader(StartupInfo startupInfo) {
		this.startupInfo.add(startupInfo);
	}
	
	/**
	 * Constructs an instance of <code>ResourceLoader</code> with several sets of startup information
	 * @param startupInfo Several sets of startup information.
	 */
	public ResourceLoader(StartupInfo[] startupInfo) {
		for (StartupInfo info : startupInfo)
			this.startupInfo.add(info);
	}
	
	/**
	 * Constructs an instance of <code>ResourceLoader</code> with several sets of startup information
	 * @param startupInfo Several sets of startup information.
	 */
	public ResourceLoader(Collection<StartupInfo> startupInfo) {
		this.startupInfo.addAll(startupInfo);
	}
	
	/**
	 * Adds the specified startup information.
	 * @param startupInfo The startup information.
	 */
	public void add(StartupInfo startupInfo) {
		ensureValidAddState();
		this.startupInfo.add(startupInfo);
	}
	
	/**
	 * Adds all the specified startup information sets.
	 * @param startupInfo A set of startup information.
	 */
	public void addAll(StartupInfo[] startupInfo) {
		ensureValidAddState();
		for (StartupInfo info : startupInfo)
			this.startupInfo.add(info);
	}
	
	/**
	 * Adds all the specified startup information sets.
	 * @param startupInfo A set of startup information.
	 */
	public void addAll(Collection<StartupInfo> startupInfo) {
		ensureValidAddState();
		this.startupInfo.addAll(startupInfo);
	}
	
	/**
	 * Gets test classes
	 * whose names are in the startup info.
	 */
	public Class<?>[] getTestClasses() { 
		if (this.testClasses == null)
			this.testClasses = this.loadTestClasses();
		
		return testClasses;
	}
	
	/**
	 * Gets the instances of the implementations of <code>ITestMethodEvaluator</code>
	 * whose names are in the startup info.
	 */
	public ITestMethodEvaluator[] getEvaluators() {
		if (this.evaluators == null)
			this.evaluators = this.instantiateEvaluators();
		
		return this.evaluators;
	}
	
	/**
	 * Gets the instances of the implementations of <code>ITestResultViewer</code>
	 * whose names are in the startup info.
	 */
	public ITestResultViewer[] getViewers() { 
		if (this.viewers == null)
			this.viewers = this.instantiateViewers();
		
		return this.viewers;
	}
	
	/**
	 * Loads the classes with the specified
	 * class names in which to perform tests.
	 * <br>If a class cannot be loaded its 
	 * respective array entry will be left to null.
	 * 
	 * @see						java.lang.Class
	 * @return					An array of <code>Class</code> with the loaded classes.
	 */
	private Class<?>[] loadTestClasses() {
		List<Class<?>> classes = new LinkedList<Class<?>>();
		
		for (StartupInfo info : this.startupInfo) 
			this.loadClasses(classes, info.TestClassesNames);
		
		transitState();
		
		Class<?>[] classesArray = new Class<?>[classes.size()];
		classes.toArray(classesArray);
		return classesArray;
	}

	/**
	 * Instantiates the evaluators with the specified class names.
	 * <br>If a evaluator cannot be loaded or instantiated
	 * its respective array entry will be left to null.
	 * 
	 * @see						mpdUnitTest.ITestMethodEvaluator
	 * @return					An array with instances of <code>ITestMethodEvaluator</code>.
	 */
	private ITestMethodEvaluator[] instantiateEvaluators() {
		List<ITestMethodEvaluator> evaluators = new LinkedList<ITestMethodEvaluator>();
		
		for (StartupInfo info : this.startupInfo) 
			this.instantiateClasses(evaluators, info.EvaluatorNames);
		
		// Verify types		
		verifyListTypes(ITestMethodEvaluator.class, evaluators);

		transitState();
		
		ITestMethodEvaluator[] evaluatorArray = new ITestMethodEvaluator[evaluators.size()];
		evaluators.toArray(evaluatorArray);
		return evaluatorArray;
	}

	/**
	 * Instantiates the viewers with the specified class names.
	 * <br>If a viewer cannot be loaded or instantiated
	 * its respective array entry will be left to null.
	 * 
	 * @see					mpdUnitTest.ITestResultViewer
	 * @return				An array with instances of <code>ITestResultViewer</code>.
	 */
	private ITestResultViewer[] instantiateViewers() {
		List<ITestResultViewer> viewers = new LinkedList<ITestResultViewer>();
		
		for (StartupInfo info : this.startupInfo) 
			this.instantiateClasses(viewers, info.ViewersNames);
		
		// Verify types		
		verifyListTypes(ITestResultViewer.class, viewers);
				
		transitState();
		
		ITestResultViewer[] viewerArray = new ITestResultViewer[viewers.size()];
		viewers.toArray(viewerArray);
		return viewerArray; 
	}

	/**
	 * Instantiates the specified classes 
	 * and casts them to the type <code>T</code>.
	 * <br>If a class cannot be loaded or instantiated
	 * its respective array entry will be left to null.
	 * The types are added to the specified list.
	 *  
	 * @see 				java.lang.Class
	 * @param <T>			The base type of the desired instances.
	 * @param classList		The list where to add the types.
	 * @param classNames	The types to instantiate.
	 */
	@SuppressWarnings("unchecked")
	private <T> void instantiateClasses(List<T> classList, String[] classNames) {
		// Load the classes by their names
		List<Class<?>> classes = this.loadClasses(classNames);
		
		// For each class
		for (int i=0; i < classes.size(); ++i) {
			Class<?> clazz = classes.get(i);
			
			// If the class wasn't loaded, we cannot instantiate it,
			// the array entry will be left to null
			if (clazz == null) 
				continue;
			
			T t = null;
			
			try {
				// Try to instantiate the class
				t = (T) clazz.newInstance();
			} 
			
			// Here, in either case, we cannot instantiate the class
			// The array entry will be left to null
			catch (InstantiationException e) { 
				this.showWarning("Class could not be instantiated: " + classNames[i]);
			} 
			catch (IllegalAccessException e) { 
				this.showWarning("Class doesn't have a nullary constructor, instantiation is not possible: " + classNames[i]);
			}
			
			classList.add(t);
		}
	}

	/**
	 * Loads type information about the specified class names.
	 * <br>If a class cannot be loaded its respective
	 * array entry will be null.
	 * 
	 * @see java.lang.Class
	 * @param classNames	The names of the classes to load.
	 * @return				An array of instances of <code>Class</code> respective to the specified class names.
	 */
	private List<Class<?>> loadClasses(String[] classNames) {
		List<Class<?>> classList = new ArrayList<Class<?>>(classNames.length);
		this.loadClasses(classList, classNames);
		return classList;
	}
	
	/**
	 * Loads type information about the specified class names.
	 * <br>If a class cannot be loaded its respective
	 * array entry will be null.
	 * 
	 * @see java.lang.Class
	 * @param classList		The list where to add the loaded classes.
	 * @param classNames	The names of the classes to load.
	 */
	private void loadClasses(List<Class<?>> classList, String[] classNames) {
		// For each class name
		for (int i=0; i<classNames.length; ++i) {
			String className = classNames[i];
			Class<?> c = null;
			try {
				// Try to load the respective class
				c = Class.forName(className);
			} catch (ClassNotFoundException e) {
				// The class cannot be loaded
				// The entry will be left to null
				classList.add(c);
				this.showWarning("Class not found: " + className);
			}
			
			classList.add(c);
		}
	}

	/**
	 * Verifies if all the elements in the specified list are assignable to the specified expected type.
	 * @param expectedType			The expected type.
	 * @param objectList			The list of object to verify.
	 * @throws RuntimeException		If any of the objects are not assignable to th especified type.
	 */
	private void verifyListTypes(Class<?> expectedType, List<?> objectList)
			throws RuntimeException {
		for (Object o : objectList) {
			Class<?> objectType = o.getClass();
			boolean isCompatible = expectedType.isAssignableFrom(objectType);
			if (!isCompatible)
				throw new RuntimeException(objectType + " is not assignable to " + expectedType.getSimpleName());
		}
	}
	
	/**
	 * Shows the specified warning to the system default output stream.
	 * @param message	The warning to show.
	 */
	private void showWarning(String message){
		System.out.println("Warning: " + message + ".");
	}
	
	/**
	 * Checks if the current state allows for adding more startup information.
	 * @throws UnsupportedOperationException if some loading has already been done.
	 */
	private void ensureValidAddState() {
		if (this.transited) 
			throw new UnsupportedOperationException("Cannot add more startup information after some resource loading has been requested");
	}
	
	/**
	 * After this method is called no more adding of startup information is allowed.
	 */
	private void transitState() {
		this.transited = true;
	}
}
