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
					//Session session = container.connectToServer(EventSocketClient.class,uri);
					
					/*for(int i=0;i<100000;i++){
						BufferedImage buf= new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize())) ;
						conn.sendObject(new Message("IMG_FRAME",buf));
						//conn.sendObject(new Message("MOUSE_MOVE","500,750"));
						//Thread.sleep(500);
						//conn.sendObject(new Message("MOUSE_RCLICK",null));
						//Thread.sleep(100000);
					}*/
					
					//while(true){
					//}
	}
		
		public void connectTo(String ip,String user,String password){
			String adress="ws://"+ip+"/events/";
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
