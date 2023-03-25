package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import mpdUnitTest.EvaluationResult;
import mpdUnitTest.ITestMethodEvaluator;
import mpdUnitTest.TestClass;
import mpdUnitTest.TestMethod;
import mpdUnitTest.evaluators.ExpectedException;
import mpdUnitTest.evaluators.ExpectedExceptionEvaluator;

import org.junit.Test;

@TestClass("A class that tests the ExpectedExceptionEvaluator")
public class ExpectedExceptionEvaluatorTest {
	
	@TestMethod
	@ExpectedException("")
	public static void testMethod1(){
		// No exception thrown
	}
	
	@TestMethod
	@ExpectedException("IllegalStateException")
	public static void testMethod2(){
		throw new IllegalStateException();
	}
	
	@Test
	public void testEvaluate() throws Exception {
		ITestMethodEvaluator evaluator = new ExpectedExceptionEvaluator();
		Class<?>[] noParameters = new Class<?>[0];
		EvaluationResult r1 = evaluator.evaluate(this.getClass().getDeclaredMethod("testMethod1", noParameters));
		EvaluationResult r2 = evaluator.evaluate(this.getClass().getDeclaredMethod("testMethod2", noParameters));
		
		assertTrue("The evaluation has failed.", r1.Success);
		assertTrue("The evaluation has failed.", r2.Success);
		
	}

	@Test
	public void testGetAnnotationType() {
		ITestMethodEvaluator e = new ExpectedExceptionEvaluator();
		assertEquals("Wrong annotation type.", ExpectedException.class, e.getAnnotationType());
	}

}
