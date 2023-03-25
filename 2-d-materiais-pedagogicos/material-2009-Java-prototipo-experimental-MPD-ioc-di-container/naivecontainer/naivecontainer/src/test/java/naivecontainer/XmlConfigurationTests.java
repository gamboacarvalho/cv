package naivecontainer;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import javax.xml.parsers.ParserConfigurationException;

import naivecontainer.exceptions.NaiveContainerException;

import org.junit.Test;
import org.xml.sax.SAXException;

public class XmlConfigurationTests {
	
	interface Service1{}
	public static class Service1Impl1 implements Service1{}
	
	static public class Dependent{
		
		private final String _s1;
		private final String _s2;
		private final int _i;
		public String getS1(){return _s1;}
		public String getS2(){return _s2;}
		public int getInteger(){
			return _i;
		}
		
		public Dependent(@Named("s1") String s1, @Named("s2") String s2, Service1 srv, Integer i){
			_s1 = s1;
			_s2 = s2;
			_i = i;
		}
	}
	
	private static String xml = 
			"<bindings>" +
			"	<binding bind='java.lang.String' named='s1' toInstance='string1' />" +
			"	<binding bind='java.lang.String' named='s2' toInstance='string2' />" +
			"	<binding bind='java.lang.Integer' toInstance='123' />" +
			"	<binding bind='naivecontainer.XmlConfigurationTests$Service1' to='naivecontainer.XmlConfigurationTests$Service1Impl1' />" +
			"</bindings>";
		
	
	@Test
	public void can_read_bindings_from_xml_config() throws NaiveContainerException, ParserConfigurationException, SAXException, IOException, InvocationTargetException{
		InputStream is = new ByteArrayInputStream(xml.getBytes());
		Injector injector = new SimpleInjector(new XmlConfiguration(is));
		Dependent dep = injector.getInstance(Dependent.class);
		assertEquals("string1", dep.getS1());
		assertEquals("string2", dep.getS2());		
		assertEquals(123, dep.getInteger());
	}

}
