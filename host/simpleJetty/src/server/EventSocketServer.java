package server;


import javax.swing.ImageIcon;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import coders.*;
import simpleUI.UIimageTest;

@ServerEndpoint(value = "/events/", encoders = { MessageEncoder.class }, decoders = { MessageDecoder.class } )
public class EventSocketServer
{
	UIimageTest frame=new UIimageTest();
	@OnOpen
	public void onWebSocketConnect(Session sess)
	{
		sess.setMaxTextMessageBufferSize(1000*2048);
		System.out.println("Socket Connected: " + sess);
	}

	@OnMessage
	public void onWebSocketText(Message message)
	{
		System.out.println("Server Received TEXT message: " + message.getMessage());
		ImageIcon icon=new ImageIcon(message.getIcon());
		frame.loadImg(icon);
	}

	@OnClose
	public void onWebSocketClose(CloseReason reason)
	{
		System.out.println("Socket Closed: " + reason);
	}

	@OnError
	public void onWebSocketError(Throwable cause)
	{
		cause.printStackTrace(System.err);
	}
}