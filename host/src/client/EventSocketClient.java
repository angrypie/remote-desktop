package client;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import coders.Controller;
import coders.Message;
import coders.MessageDecoder;
import coders.MessageEncoder;


@ClientEndpoint( encoders = { MessageEncoder.class }, decoders = { MessageDecoder.class } )
public class EventSocketClient
{
	private Controller contr;

	@OnOpen
	public void onWebSocketConnect(Session sess)
	{
		sess.setMaxTextMessageBufferSize(1000*2024);
		System.out.println("Socket Connected: " + sess);
		if(contr==null)contr=new Controller();
		contr.setSession(sess);
		contr.registerHost();
	}

	@OnMessage
	public void onWebSocketText(Message message)
	{
		System.out.println("Client Received TEXT message: " + message.getAction());
		contr.newMessage(message);
	}

	@OnClose
	public void onWebSocketClose(CloseReason reason)
	{
		contr.clientClose();
		System.out.println("Socket Closed: " + reason);
	}

	@OnError
	public void onWebSocketError(Throwable cause)
	{
		cause.printStackTrace(System.err);
	}
	
	public void setUser(String user,String password){
		contr=new Controller();
		contr.setUser(user,password);
	}
}