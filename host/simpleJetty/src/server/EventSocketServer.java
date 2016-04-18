package server;


import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

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
		if(message.getAction().compareTo("IMG_FRAME")==0)getFrame(message);
		
	}
	
	private void getFrame(Message message){
		System.out.println("Server Received frame: " + message.getAction());
		BufferedImage img=new BufferedImage(frame.getLblNewLabel().getWidth(), frame.getLblNewLabel().getHeight(), BufferedImage.TRANSLUCENT);
		Graphics2D g2 = img.createGraphics();
		if(message.getData()==null)System.out.println("get NULL data!");
		ImageIcon imageIcon=new ImageIcon((BufferedImage)message.getData());
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(imageIcon.getImage(), 0, 0, frame.getLblNewLabel().getWidth(), frame.getLblNewLabel().getHeight(), null);
		g2.dispose();
		ImageIcon icon=new ImageIcon(img,imageIcon.getDescription() );
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