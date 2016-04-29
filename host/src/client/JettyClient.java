package client;

import java.io.IOException;
import java.net.URI;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.WebSocketContainer;

public class JettyClient{
	public static void main(String[] args) throws Exception{
		URI uri = URI.create("ws://localhost:9595/events/");
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		EventSocketClient clientEvent=new EventSocketClient();
		clientEvent.setUser("testUser", "password");
		container.connectToServer(clientEvent, uri);
		while(true){
			Thread.sleep(1000);
		}
	}

	public int connectTo(String ip,String user,String password){
		String adress="ws://"+ip+":9595/events/";
		URI uri = URI.create(adress);
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		EventSocketClient clientEvent=new EventSocketClient();
		clientEvent.setUser(user, password);
		try {
			container.connectToServer(clientEvent, uri);
		} catch (DeploymentException e) {
			return -1;
		} catch (IOException e) {
			return -1;
		}
		return 0;
	}
}
