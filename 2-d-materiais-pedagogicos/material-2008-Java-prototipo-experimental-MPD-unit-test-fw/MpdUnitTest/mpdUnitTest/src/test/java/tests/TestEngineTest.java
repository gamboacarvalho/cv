package tests;

import static junit.framework.Assert.*;
import mpdUnitTest.EvaluationResult;
import mpdUnitTest.ITestMethodEvaluator;
import mpdUnitTest.ITestResultViewer;
import mpdUnitTest.TestClass;
import mpdUnitTest.TestEngine;
import mpdUnitTest.TestMethod;
import mpdUnitTest.evaluators.ExpectedException;
import mpdUnitTest.evaluators.ExpectedExceptionEvaluator;
import mpdUnitTest.evaluators.MaximumTime;
import mpdUnitTest.evaluators.MaximumTimeEvaluator;
import mpdUnitTest.evaluators.Returns;
import mpdUnitTest.evaluators.ReturnsEvaluator;

import org.junit.Test;

public class TestEngineTest {

	@TestClass("A dummy test class")
	public static class DummyTestClass {
		@TestMethod
		@Returns("1988")
		@ExpectedException("")
		@MaximumTime(10)
		public static int testMethod(){
			 ++testMethod_calls;
			 return 1988;
		}
		
		public static void nonTestMethod(){
			fail("This method should never be invoked.");
		}
	}
	
	public class DummyClass {
		public void testMethod(){
			fail("This method should never be invoked.");
		}
	}

	public class DummyViewer implements ITestResultViewer {

		@Override
		public void endedClassTest(boolean classSuccess) {
			assertTrue("class test was not successful", classSuccess);
		}

		@Override
		public void endedMethodTest(boolean methodSuccess) {
			assertTrue("Method test was not successful", methodSuccess);
		}

		@Override
		public void endedTestSession(boolean testSessionSuccess) {
			assertTrue("Test session was not successful", testSessionSuccess);
		}

		@Override
		public void methodEvaluation(EvaluationResult evaluationResult) {
			assertTrue("Method evaluation was not successful", evaluationResult.Success);
		}

		@Override
		public void startedClassTest(String className, String description) {
			assertEquals("Wrong class name", DummyTestClass.class.getSimpleName(), className);
			assertEquals("Wrong description", ((TestClass)(DummyTestClass.class.getAnnotation(TestClass.class))).value(), description);
		}

		@Override
		public void startedMethodTest(String methodName) {
			++startedMethodTest_calls;
		}

		@Override
		public void startedTestSession() {
			++startedTestSession_calls;
		}

	}

	private static int testMethod_calls = 0;
	private int startedMethodTest_calls = 0;
	private int startedTestSession_calls = 0;
	
	@Test
	public void testTestEngine() {
		ITestResultViewer[] viewers = { new DummyViewer() };
		ITestMethodEvaluator[] evaluators = { new ReturnsEvaluator(), new MaximumTimeEvaluator(), new ExpectedExceptionEvaluator()};
		TestEngine engine = new TestEngine(viewers, evaluators);
		
		Class<?>[] testClasses = { DummyClass.class, DummyTestClass.class };
		engine.test(testClasses);
		assertEquals("Wrong number of method calls", 3, testMethod_calls);
		assertEquals("Wrong number of method calls", 1, this.startedMethodTest_calls);
		assertEquals("Wrong number of method calls", 1, this.startedTestSession_calls);
	}
}
