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
		container.connectToServer(EventSocketClient.class, uri);
		while(true){
			Thread.sleep(1000);
		}
	}

	public void connectTo(String ip,String user,String password){
		String adress="ws://"+ip+":9595/events/";
		URI uri = URI.create(adress);
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		try {
			container.connectToServer(EventSocketClient.class, uri);
		} catch (DeploymentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}