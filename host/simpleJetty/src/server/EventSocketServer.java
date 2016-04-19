package server;


import java.awt.AWTException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.event.InputEvent;
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
	Robot robot;
	@OnOpen
	public void onWebSocketConnect(Session sess)
	{
		sess.setMaxTextMessageBufferSize(1000*2048);
		System.out.println("Socket Connected: " + sess);
		if(robot==null)
			try {
				robot=new Robot();
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	@OnMessage
	public void onWebSocketText(Message message)
	{
		if(message.getAction().compareTo("IMG_FRAME")==0)getFrame(message);
		else if(message.getAction().compareTo("MOUSE_MOVE")==0)mouseMove(message);
		else if(message.getAction().compareTo("MOUSE_LCLICK")==0)mouseClick(1);
		else if(message.getAction().compareTo("MOUSE_RCLICK")==0)mouseClick(3);
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

	private void mouseMove(Message message){
		String point[]=message.getData().toString().split(",");
		int x=Integer.valueOf(point[0]);
		int y=Integer.valueOf(point[1]);
		robot.mouseMove(x, y);
	}

	private void mouseClick(int button){
		if(button==1){
			robot.mousePress(InputEvent.BUTTON1_MASK);
			robot.mouseRelease(InputEvent.BUTTON1_MASK);
		}else{
			robot.mousePress(InputEvent.BUTTON3_MASK);
			robot.mouseRelease(InputEvent.BUTTON3_MASK);
		}
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