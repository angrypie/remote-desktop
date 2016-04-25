package coders;

import java.awt.AWTException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;

import javax.json.JsonObject;
import javax.swing.ImageIcon;
import javax.websocket.Session;

import coders.Message;
import simpleUI.UIimageTest;

public class Controller {
	private UIimageTest frame;
	private Robot robot;
	private MessageDecoder dec;
	private Session sess;
	private Thread thread;
	private SendFrames sendFrames;
	
	public Controller() {
		super();
	}
	
	
	public void startController(){
		try {
			this.robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		dec=new MessageDecoder();
	}

	public void newMessage(Message message){
		String action=message.getAction();
		if(action.compareTo("IMG_FRAME")==0)getFrame(message);
		else if(action.compareTo("MOUSE_MOVE")==0)mouseMove(message);
		else if(action.compareTo("MOUSE_LCLICK")==0)mouseClick(1);
		else if(action.compareTo("MOUSE_RCLICK")==0)mouseClick(3);
		else if(action.compareTo("START_STREAM")==0)startView();
		else if(action.compareTo("STOP_STREAM")==0)stopView();
		else if(action.compareTo("CLIENT_CLOSE")==0)clientClose();
		else message.setData(null);
	}
	
	private void clientClose() {
		if(thread!=null && sendFrames!=null){
			sendFrames.stopStream();
		}
	}


	private void stopView() {
		if(thread!=null && sendFrames!=null){
			sendFrames.stopStream();
		}
	}


	private void startView(){
		sendFrames=new SendFrames(sess, 0);
		thread=new Thread(sendFrames);
		thread.start();
	}
	
	private void getFrame(Message message){
		if(frame==null)frame = new UIimageTest();
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
		JsonObject json=dec.getJson((String)message.getData());
		int x=Integer.valueOf(json.getString("x"));
		int y=Integer.valueOf(json.getString("y"));
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

	public void setSession(Session sess) {
		this.sess=sess;
	}
}
