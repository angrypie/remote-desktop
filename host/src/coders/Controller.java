package coders;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import javax.json.JsonObject;
import javax.websocket.Session;
import client.MainWindow;
import coders.Message;

public class Controller {
	private Robot robot;
	private MessageDecoder dec;
	private Session sess;
	private Thread thread;
	private SendFrames sendFrames;
	private String user;
	private String password;
	private boolean clientConnected;

	
	public Controller() {
		super();
		clientConnected=false;
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


	private void clientClose() {
		clientConnected=false;
		stopView();
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

	private void mouseMove(Message message){
		JsonObject json=dec.getJson((String)message.getData());
		int x=Integer.valueOf(json.getString("x"));
		int y=Integer.valueOf(json.getString("y"));
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
