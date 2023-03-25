package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import mpdUnitTest.EvaluationResult;
import mpdUnitTest.ITestMethodEvaluator;
import mpdUnitTest.TestClass;
import mpdUnitTest.TestMethod;
import mpdUnitTest.evaluators.ExpectedException;
import mpdUnitTest.evaluators.ExpectedExceptionEvaluator;
import mpdUnitTest.evaluators.Returns;
import mpdUnitTest.evaluators.ReturnsEvaluator;

import org.junit.Test;

@TestClass("A class that tests the ReturnsEvaluator")
public class ReturnsEvaluatorTest {
	
	@TestMethod
	@Returns("MPD")
	public static Object testMethod5(){
		return "MPD";
	}
	
	@TestMethod
	@Returns("Programming")
	public static String testMethod6(){
		return "Martelada";
	}
	
	@Test
	public void testEvaluate() throws Exception {
		ITestMethodEvaluator evaluator = new ReturnsEvaluator();
		Class<?>[] noParameters = new Class<?>[0];
		
		EvaluationResult r1 = evaluator.evaluate(this.getClass().getDeclaredMethod("testMethod5", noParameters));
		EvaluationResult r2 = evaluator.evaluate(this.getClass().getDeclaredMethod("testMethod6", noParameters));
		
		assertTrue("The evaluation has failed.", r1.Success);
		assertFalse("The evaluation has wrongfully succeeded.", r2.Success);
		
	}

	@Test
	public void testGetAnnotationType() {
		ITestMethodEvaluator e = new ExpectedExceptionEvaluator();
		assertEquals("Wrong annotation type.", ExpectedException.class, e.getAnnotationType());
	}
}
