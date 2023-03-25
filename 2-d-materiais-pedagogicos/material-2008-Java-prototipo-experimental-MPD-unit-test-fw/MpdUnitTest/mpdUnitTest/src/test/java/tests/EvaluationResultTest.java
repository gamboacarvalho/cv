package tests;

import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import mpdUnitTest.EvaluationResult;

import org.junit.Test;

public class EvaluationResultTest {

	@Test
	public void testEvaluationResult() {
		boolean success = true;;
		Method testMethod = this.getClass().getDeclaredMethods()[0];
		Class<? extends Annotation> annotationType = mpdUnitTest.evaluators.Returns.class;
		String message = "This is a test";
		
		EvaluationResult er = new EvaluationResult(success, testMethod, annotationType, message);
		
		assertEquals("The success property doesn't have the expected value.", er.Success, success);
		assertEquals("The test method is not the same.", er.MethodName, testMethod.getName());
		assertEquals("The class name is wrong.", er.ClassName, testMethod.getDeclaringClass().getSimpleName());
		assertSame("The annotation type is not the same.", er.AnnotationType, annotationType);
		assertEquals("The annotation name is wrong.", er.AnnotationName, annotationType.getSimpleName());
		assertSame("The message is not the same.", er.Message, message);
	}
}
