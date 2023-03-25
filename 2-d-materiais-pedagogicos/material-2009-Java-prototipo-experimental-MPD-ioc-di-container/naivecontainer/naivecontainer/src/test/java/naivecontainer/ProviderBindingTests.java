package naivecontainer;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;

import naivecontainer.exceptions.NaiveContainerException;

import org.junit.Test;

public class ProviderBindingTests {
	
	public static class Dependent{
		
		private final ServerSocket _socket;
		public ServerSocket getSocket(){
			return _socket;
		}

		public Dependent(ServerSocket socket){
			_socket = socket;
		}		
	}
	
	public static class ServerSocketProvider implements Provider<ServerSocket>{
		
		private final int _port;
		public ServerSocketProvider(@Named("LocalPort") int port){
			_port = port;
		}
		@Override
		public ServerSocket provide() throws InvocationTargetException {
			try {
				return new ServerSocket(_port);
			} catch (IOException e) {
				throw new InvocationTargetException(e);
			}
		}		
	}
	
	@Test
	public void i_should_really_improve_my_test_naming() throws NaiveContainerException, InvocationTargetException{
		
		Injector injector = new SimpleInjector(SimpleConfiguration.with()
				.bind(int.class).withName("LocalPort").to(8080)
				.bind(ServerSocket.class).toProvider(ServerSocketProvider.class));
		
		Dependent dep = injector.getInstance(Dependent.class);
		assertEquals(8080,dep.getSocket().getLocalPort());
	}
}
