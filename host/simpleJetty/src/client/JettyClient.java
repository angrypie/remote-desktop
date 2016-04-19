package client;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.net.URI;

import javax.websocket.ContainerProvider;
import javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.eclipse.jetty.util.component.LifeCycle;

import coders.Message;

public class JettyClient{
		public static void main(String[] args) throws Exception{
			URI uri = URI.create("ws://localhost:9595/events/");
				WebSocketContainer container = ContainerProvider.getWebSocketContainer();
					Session session = container.connectToServer(EventSocketClient.class,uri);
					Basic conn=session.getBasicRemote();
					
					for(int i=0;i<10;i++){
						BufferedImage buf= new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize())) ;
						conn.sendObject(new Message("IMG_FRAME",buf));
						conn.sendObject(new Message("MOUSE_MOVE","500,750"));
						Thread.sleep(500);
						conn.sendObject(new Message("MOUSE_RCLICK",null));
						Thread.sleep(1000);
					}
					// Close session
					session.close();
					// Force lifecycle stop when done with container.
					// This is to free up threads and resources that the
					// JSR-356 container allocates. But unfortunately
					// the JSR-356 spec does not handle lifecycles (yet)
					if (container instanceof LifeCycle)((LifeCycle)container).stop();
	}
}
