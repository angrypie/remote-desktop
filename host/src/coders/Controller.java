package coders;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;
import javax.websocket.Session;
import coders.Message;

public class Controller {
	private Robot robot;
	private MessageDecoder dec;
	private Session sess;
	private Thread thread;
	private SendFramesWindows sendFramesWindows;
	private SendFramesLinux sendFramesLinux;
	private String user;
	private String password;
	private boolean clientConnected;
	private int osCode;


	public Controller() {
		super();
		clientConnected=false;
		String os=System.getProperty("os.name");
		if(os.contains("Windows")){
			osCode=0;
		}
		else if(os.contains("Linux")){
			osCode=1;
		}
	}

	public void newMessage(Message message){
		String action=message.getAction();
		if(action.compareTo("MOUSE_MOVE")==0)mouseMove(message);
		else if(action.compareTo("MOUSE_RPRESS")==0)mousePress(3);
		else if(action.compareTo("MOUSE_RRELEASE ")==0)mouseRelease(3);
		else if(action.compareTo("MOUSE_LPRESS")==0)mousePress(1);
		else if(action.compareTo("MOUSE_LRELEASE")==0)mouseRelease(1);
		else if(action.compareTo("START_STREAM")==0)startView();
		else if(action.compareTo("STOP_STREAM")==0)stopView();
		else if(action.compareTo("CLIENT_CLOSE")==0)clientClose();
		else if(action.compareTo("CLIENT_CONNECT")==0)clientConnect();
		else message.setData(null);
	}

	private void clientConnect() {
		if(clientConnected==true){
			sess.getAsyncRemote().sendObject(new Message("CLIENT_DENIED",null));
		}else{
			sess.getAsyncRemote().sendObject(new Message("CLIENT_ACCESS",null));
			clientConnected=true;
		}


	}

	private void mouseRelease(int i) {
		if(i==1){
			robot.mouseRelease(InputEvent.BUTTON1_MASK);
		}else if(i==3){
			robot.mouseRelease(InputEvent.BUTTON3_MASK);
		}

	}


	private void mousePress(int i) {
		if(i==1){
			robot.mousePress(InputEvent.BUTTON1_MASK);
		}else if(i==3){
			robot.mousePress(InputEvent.BUTTON3_MASK);
		}		
	}


	public void clientClose() {
		clientConnected=false;
		stopView();
	}


	private void stopView() {
		if(osCode==0)stopViewWindows();
		else if(osCode==1)stopViewLinux();
		else return;
	}


	private void stopViewLinux() {
		if(thread!=null && sendFramesLinux!=null){
			sendFramesLinux.stopStream();
		}
	}

	private void stopViewWindows() {
		if(thread!=null && sendFramesWindows!=null){
			sendFramesWindows.stopStream();
		}
	}

	private void startView(){
		if(osCode==0)startViewWindows();
		else if(osCode==1)startViewLinux();
		else return;
		thread.start();
	}

	private void startViewLinux() {
		sendFramesLinux=new SendFramesLinux(sess, 0);
		thread=new Thread(sendFramesLinux);
	}

	private void startViewWindows() {
		sendFramesWindows =new SendFramesWindows(sess, 0);
		thread=new Thread(sendFramesWindows);
	}

	private void mouseMove(Message message){
		JsonObject obj = (JsonObject)message.getData();
		int x=obj.getInt("x");
		int y=obj.getInt("y");
		robot.mouseMove(x, y);
	}

	public void setSession(Session sess) {
		this.sess=sess;
		try {
			this.robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	public void registerHost() {
		sess.getAsyncRemote().sendObject(new Message("HOST_REGISTER",user));
	}

	public void setUser(String user, String password) {
		this.user=user;
		this.password=password;
	}
}
