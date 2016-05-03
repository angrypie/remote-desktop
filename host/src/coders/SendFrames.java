package coders;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.websocket.Session;
import javax.websocket.EncodeException;
import javax.websocket.RemoteEndpoint.Basic;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.FrameGrabber.Exception;

import coders.Message;


public class SendFrames implements Runnable{
	private int delay=0;
	private BufferedImage buf;
	private long start=0;
	private int count=0;
	private Basic sync;
	private volatile boolean startStream=true; 
	FFmpegFrameGrabber grabber;
	Java2DFrameConverter converter;
	Frame frame = null;

	public SendFrames(Session sess,int delay) {
		sync=sess.getBasicRemote();
		this.delay=delay;
		converter=new Java2DFrameConverter();
		String os=System.getProperty("os.name");
		Rectangle rect=new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		if(os.contains("Windows")){
			grabber = new FFmpegFrameGrabber("video=screen-capture-recorder");
			grabber.setBitsPerPixel(8);
			grabber.setImageWidth(rect.width);
			grabber.setImageHeight(rect.height);
			grabber.setFormat("dshow");
			grabber.setFrameRate(30);
		}
		else if(os.contains("Linux")){
			grabber = new FFmpegFrameGrabber(":0.0+" + 0 + "," + 0);
			grabber.setBitsPerPixel(8);
			grabber.setImageWidth(rect.width);
			grabber.setImageHeight(rect.height);
			grabber.setFormat("x11grab");
			grabber.setFrameRate(30);
		}
	}

	public void stopStream(){
		startStream=false;
	}


	@Override
	public void run() {
		start=System.currentTimeMillis();
		try {
			grabber.start();
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		while(startStream==true){
			try {
				frame=grabber.grabFrame();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			buf=converter.getBufferedImage(frame);
			try {
				sync.sendObject(new Message("IMG_FRAME",buf));
			} catch (IOException | EncodeException e1) {
				e1.printStackTrace();
			}
			count++;
			if(System.currentTimeMillis()-start>=1000){
				System.out.println("fps="+count);
				count=0;
				start=System.currentTimeMillis();
			}
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			grabber.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
