package coders;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import javax.imageio.ImageIO;
import javax.json.JsonObject;

public class DataDecoder {
	
	public Object getData(JsonObject json,String action){
		Object data;
		if(action.compareTo("IMG_FRAME")==0)data=decodeImage(json);
		else if(action.compareTo("MOUSE_MOVE")==0) data=decodeMouseMove(json);
		else if(action.compareTo("MOUSE_RPRESS")==0)data=decodeSystemMessage(json);
		else if(action.compareTo("MOUSE_RRELEASE ")==0)data=decodeSystemMessage(json);
		else if(action.compareTo("MOUSE_LPRESS")==0)data=decodeSystemMessage(json);
		else if(action.compareTo("MOUSE_LRELEASE")==0)data=decodeSystemMessage(json);
		else if(action.compareTo("START_STREAM")==0) data=decodeSystemMessage(json);
		else if(action.compareTo("STOP_STREAM")==0) data=decodeSystemMessage(json);
		else if(action.compareTo("CLIENT_CLOSE")==0) data=decodeSystemMessage(json);
		else if(action.compareTo("CLIENT_CONNECT")==0) data=decodeSystemMessage(json);
		else data=null;
		return data;
	}
	
	public Object decodeImage(JsonObject json){
		String imageString=json.getString("data");
		BufferedImage bImage = null;
		try {
			byte[] output = Base64.getDecoder().decode(imageString);
			ByteArrayInputStream bais = new ByteArrayInputStream(output);
			bImage = ImageIO.read(bais);
		} catch (IOException ex) {
		}
		return bImage;
	}
	
	public Object decodeMouseMove(JsonObject json){
		return json.getString("data");
	}
	
	public Object decodeSystemMessage(JsonObject json){
		return null;
	}
}
