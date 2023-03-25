package webfast.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.sun.net.httpserver.HttpExchange;

import webfast.AbstractController;
import webfast.ControllerHandler;
import webfast.ControllerHandler.InvokeMehod;
import webfast.HandlerOutput;
import webfast.Resolver;

public class ResolverTest {
	Resolver wfast;
	
	public static class ActorsController extends AbstractController{
		@SuppressWarnings("rawtypes")
    @ControllerHandler(InvokeMehod.Get)
		public HandlerOutput<?> list(){
			return new HandlerOutput() {
				@Override public void print(PrintStream responseBody, int depth, Object model) {
					responseBody.print("list of actors");  
        }
			};
		}
		@SuppressWarnings("rawtypes")
    @ControllerHandler(InvokeMehod.Get)
		public HandlerOutput<?> listOfActorsByCityAndAge(final String city, final Integer age){
			return new HandlerOutput() {
				@Override public void print(PrintStream responseBody, int depth, Object model) {
					responseBody.print(String.format("city: %s age: %d", city, age));  
        }
			};
		}
	};
	public static class CompaniesController extends AbstractController{
		@SuppressWarnings("rawtypes")
    @ControllerHandler(InvokeMehod.Get)
		public HandlerOutput list(final Integer actorId){
			return new HandlerOutput() {
				@Override public void print(PrintStream responseBody, int depth, Object model) {
					responseBody.print("list of companies for actor: " + actorId);  
				}
			};
		}
		@SuppressWarnings("rawtypes")
    @ControllerHandler(InvokeMehod.Get)
		public HandlerOutput getCompanyById(final Integer actorId, final Integer companyId){
			return new HandlerOutput() {
				@Override public void print(PrintStream responseBody, int depth, Object model) {
					responseBody.print(String.format("actorId: %d companyId: %d", actorId, companyId));  
				}
			};
		}
	};
	@Before
	public void setUp() throws SecurityException, NoSuchMethodException{
		//
		// Arrange
		//
		AbstractController ctrActors = new ActorsController();
		AbstractController ctrCompanies = new CompaniesController();
		wfast = new Resolver();
		wfast.attach(ctrActors).path("actors");
		wfast.attach(ctrActors).path("actors")._arg(String.class)._arg(Integer.class);
		wfast.attach(ctrCompanies).path("actors")._arg(Integer.class).__("companies");
		wfast.attach(ctrCompanies).path("actors")._arg(Integer.class).__("companies")._arg(Integer.class);
	}
	@Test
	public void check_invalid_request() throws IOException, URISyntaxException{
		//
		// Arrange
		//
		ByteArrayOutputStream respStream = new ByteArrayOutputStream();
		//
		// Act and Assert
		//
		HttpExchange exch = new MockHttpExchange("ljqajd/askdhkahs", respStream);
		wfast.handle(exch);
		Assert.assertEquals(404, exch.getResponseCode());
	}
	@Test
	public void check_insuficient_parameters() throws IOException, URISyntaxException{
		//
		// Arrange
		//
		ByteArrayOutputStream respStream = new ByteArrayOutputStream();
		//
		// Act and Assert
		//
		HttpExchange exch = new MockHttpExchange("actors/lisboa", respStream);
		wfast.handle(exch);
		Assert.assertEquals(404, exch.getResponseCode());
	}
	@Test
	public void check_companies_simple_request() throws IOException, URISyntaxException{
		//
		// Arrange
		//
		ByteArrayOutputStream respStream = new ByteArrayOutputStream();
		//
		// Act and Assert
		//
		wfast.handle(new MockHttpExchange("http://127.0.0.1:8080/actors/23/companies", respStream));
		Assert.assertEquals("list of companies for actor: 23", respStream.toString());
	}
	@Test
	public void check_companies_request_with_parameter() throws IOException, URISyntaxException{
		//
		// Arrange
		//
		ByteArrayOutputStream respStream = new ByteArrayOutputStream();
		//
		// Act and Assert
		//
		wfast.handle(new MockHttpExchange("http://127.0.0.1:8080/actors/23/companies/56", respStream));
		Assert.assertEquals("actorId: 23 companyId: 56", respStream.toString());
	}
	@Test
	public void check_actors_simple_request() throws IOException, URISyntaxException{
		//
		// Arrange
		//
		ByteArrayOutputStream respStream = new ByteArrayOutputStream();
		//
		// Act and Assert
		//
		wfast.handle(new MockHttpExchange("http://127.0.0.1:8080/actors", respStream));
		Assert.assertEquals("list of actors", respStream.toString());
	}
	@Test
	public void check_actors_request_with_parameters() throws IOException, URISyntaxException{
		//
		// Arrange
		//
		ByteArrayOutputStream respStream = new ByteArrayOutputStream();
		//
		// Act and Assert
		//
		wfast.handle(new MockHttpExchange("http://127.0.0.1:8080/actors/lisboa/56", respStream));
		Assert.assertEquals("city: lisboa age: 56", respStream.toString());
	}
}
