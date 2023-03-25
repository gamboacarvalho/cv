package webfast.rounting.test;

import java.util.Iterator;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import webfast.routing.Path;
import webfast.routing.PathPartParameter;

public class PathTest {
	Path path;
	@Before
	public void setUp(){
		//
		// Arrange
		//
		path = new Path();
	}

	@Test(expected=NoSuchMethodException.class)
	public void check_an_invalid_parameter_type_without_valueof() throws SecurityException, NoSuchMethodException{
		class AClass_without_valueof{};
		path._arg(AClass_without_valueof.class);
	}
	@Test(expected=IllegalArgumentException.class)
	public void check_an_invalid_parameter_type_without_valueof_static() throws SecurityException, NoSuchMethodException{
		class AClass_without_valueof_static{
			public AClass_without_valueof_static valueOf(String arg){return null;}
		};
		path._arg(AClass_without_valueof_static.class);
	}
	@Test
	public void check_a_simple_constant_path() throws Exception{
		//
		// Arrange
		//
		path.__("actors");
		//
		// Act and Assert
		//
		Assert.assertTrue(path.match("/AcTOrs"));
		Assert.assertTrue(path.match("/actors"));
		Assert.assertFalse(path.match("/actores"));
	}
	@Test
	public void check_a_simple_constant_path_with_an_optional_integer_parameter() throws Exception{
		//
		// Arrange
		//
		path.__("actors")._arg(Integer.class);
		//
		// Act and Assert
		//
		Assert.assertTrue(path.match("/actors/6544"));
		Assert.assertFalse(path.match("/actores/kjhkh"));
	}
	@Test
	public void check_path_with_constants_and_parameters() throws Exception{
		//
		// Arrange
		//
		path
			.__("actors")
			._arg(Integer.class)
			.__("companies")
			._arg(String.class);
		//
		// Act and Assert
		//
		Assert.assertFalse(path.match("/AcTOrs/jgjg/companies/conchichina"));
		Assert.assertFalse(path.match("/AcTOrs/34"));
		Assert.assertFalse(path.match("/AcTOrs/34/companies/"));//missing parameter
		// Act and Assert
		//
		Assert.assertTrue(path.match("/AcTOrs/34/companies/conchichina"));
		Iterator<PathPartParameter<?>> params = path.getParameters().iterator();
		Assert.assertEquals(params.next().getParamValue(), 34);		
		Assert.assertEquals(params.next().getParamValue(), "conchichina");
		//
		// Act and Assert
		//
		Assert.assertTrue(path.match("/AcTOrs/466/companies/897"));
		params = path.getParameters().iterator();
		Assert.assertEquals(params.next().getParamValue(), 466);
		Assert.assertEquals(params.next().getParamValue(), "897");
	}
}
