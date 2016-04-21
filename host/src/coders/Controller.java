package coders;

import java.awt.AWTException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;

import javax.json.JsonObject;
import javax.swing.ImageIcon;
import coders.Message;
import simpleUI.UIimageTest;

public class Controller {
	UIimageTest frame;
	Robot robot;
	MessageDecoder dec;
	
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
		if(message.getAction().compareTo("IMG_FRAME")==0)getFrame(message);
		else if(message.getAction().compareTo("MOUSE_MOVE")==0)mouseMove(message);
		else if(message.getAction().compareTo("MOUSE_LCLICK")==0)mouseClick(1);
		else if(message.getAction().compareTo("MOUSE_RCLICK")==0)mouseClick(3);
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
		//String point[]=message.getData().toString().split(",");
		
		//int x=Integer.valueOf(point[0]);

		//int y=Integer.valueOf(point[1]);
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
}
