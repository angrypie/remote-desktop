package coders;import java.awt.AWTException;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.websocket.Session;
import javax.imageio.ImageIO;
import javax.websocket.EncodeException;
import javax.websocket.RemoteEndpoint.Basic;

import coders.Message;


public class SendFrames implements Runnable{
	private int delay=0;
	private BufferedImage buf;
	private Robot robot;
	private Rectangle rect;
	private BufferedImage mouseCursor;
	private long start=0;
	private int count=0;
	private Basic sync;
	private volatile boolean startStream=true; 

	public SendFrames(Session sess,int delay) {
		try {
			this.mouseCursor=ImageIO.read(new File("./content/black_cursor.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		this.sync=sess.getBasicRemote();
		this.delay=delay;
		try {
			robot=new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		rect=new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
	}

	private void showCursor(BufferedImage buf){
		Graphics2D grfx = buf.createGraphics();
		PointerInfo p=MouseInfo.getPointerInfo();
		int mouseX = p.getLocation().x;
		int mouseY = p.getLocation().y;
		grfx.drawImage(mouseCursor, mouseX-5, mouseY-2,
				null);
		grfx.dispose();
	}
	
	public void stopStream(){
		startStream=false;
	}


	@Override
	public void run() {
		start=System.currentTimeMillis();
		while(startStream==true){
			buf= robot.createScreenCapture(rect) ;
			showCursor(buf);
			//async.sendObject(new Message("IMG_FRAME",buf));
			try {
				sync.sendObject(new Message("IMG_FRAME",buf));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (EncodeException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			count++;
			if(System.currentTimeMillis()-start>=1000){
				System.out.println("fps="+count);
				count=0;
				start=System.currentTimeMillis();
			}
		}
	}
}
