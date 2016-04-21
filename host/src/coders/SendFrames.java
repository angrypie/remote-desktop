package coders;import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.websocket.Session;
import javax.websocket.EncodeException;
import javax.websocket.RemoteEndpoint.Basic;

import coders.Message;


public class SendFrames implements Runnable{
	private Basic conn;
	private int delay=0;
	private BufferedImage buf;
	private Robot robot;
	private Rectangle rect;

	public SendFrames(Session sess,int delay) {
		this.conn=sess.getBasicRemote();
		this.delay=delay;
		try {
			robot=new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		rect=new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
	}
	
	private void showCursor(BufferedImage buf){
		PointerInfo p=MouseInfo.getPointerInfo();
		int mouseX = p.getLocation().x;
		int mouseY = p.getLocation().y;

		Graphics2D g2d = buf.createGraphics();
		g2d.setColor(Color.red);
		Polygon polygon1 = new Polygon(new int[] { mouseX,
				mouseX + 10, mouseX, mouseX }, new int[] { mouseY,
				mouseY + 10, mouseY + 15, mouseY }, 4);

		Polygon polygon2 = new Polygon(new int[] { mouseX + 1,
				mouseX + 10 + 1, mouseX + 1, mouseX + 1 },
				new int[] { mouseY + 1, mouseY + 10 + 1,
						mouseY + 15 + 1, mouseY + 1 }, 4);
		g2d.setColor(Color.black);
		g2d.fill(polygon1);

		g2d.setColor(Color.red);
		g2d.fill(polygon2);
		g2d.dispose();
	}


	@Override
	public void run() {
		while(true){
			buf= robot.createScreenCapture(rect) ;
			showCursor(buf);
			try {
				conn.sendObject(new Message("IMG_FRAME",buf));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (EncodeException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
