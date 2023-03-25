package tests;

import static org.junit.Assert.*;
import mpdUnitTest.EvaluationResult;
import mpdUnitTest.ITestMethodEvaluator;
import mpdUnitTest.TestClass;
import mpdUnitTest.TestMethod;
import mpdUnitTest.evaluators.MaximumTime;
import mpdUnitTest.evaluators.MaximumTimeEvaluator;

import org.junit.Test;

@TestClass("A class that tests the MaximumTimeEvaluator")
public class MaximumTimeEvaluatorTest {

	@TestMethod
	@MaximumTime(10)
	public static void testMethod3() throws InterruptedException{
		Thread.sleep(20);
	}
	
	@TestMethod
	@MaximumTime(100)
	public static void testMethod4() throws InterruptedException{
		testMethod3();
	}
	
	@Test
	public void testEvaluate() throws Exception {
		ITestMethodEvaluator evaluator = new MaximumTimeEvaluator();
		Class<?>[] noParameters = new Class<?>[0];
		EvaluationResult r1 = evaluator.evaluate(this.getClass().getDeclaredMethod("testMethod3", noParameters));
		EvaluationResult r2 = evaluator.evaluate(this.getClass().getDeclaredMethod("testMethod4", noParameters));
		
		assertFalse("The evaluation has wrongfully succeeded.", r1.Success);
		assertTrue("The evaluation has failed.", r2.Success);
	}

	@Test
	public void testGetAnnotationType() {
		ITestMethodEvaluator e = new MaximumTimeEvaluator();
		assertEquals("Wrong annotation type.", MaximumTime.class, e.getAnnotationType());
	}
}
