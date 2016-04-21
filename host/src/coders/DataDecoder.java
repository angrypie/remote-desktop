package coders;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import javax.imageio.ImageIO;
import javax.json.JsonObject;

public class DataDecoder {
	
	public Object getData(JsonObject json,String action){
		Object data;
		if(action.compareTo("IMG_FRAME")==0)data=decodeImage(json);
		else if(action.compareTo("MOUSE_MOVE")==0) data=decodeMouseMove(json);
		else if(action.compareTo("MOUSE_RCLICK")==0 || action.compareTo("MOUSE_LCLICK")==0)data=decodeAction(json);
		else if (action.compareTo("START_VIEW")==0) data=decodeAction(json);
		else data=null;
		return data;
	}
	
	public Object decodeImage(JsonObject json){
		String imageString=json.getString("data");
		BufferedImage bImage = null;
		try {
			byte[] output = Base64.getDecoder().decode(imageString);
			/*try {
				output=decompress(output);
			} catch (DataFormatException e) {
				e.printStackTrace();
			}*/
			ByteArrayInputStream bais = new ByteArrayInputStream(output);
			bImage = ImageIO.read(bais);
		} catch (IOException ex) {
		}
		return bImage;
	}
	
	 public static byte[] decompress(byte[] data) throws IOException, DataFormatException {  
		   Inflater inflater = new Inflater();   
		   inflater.setInput(data);  
		   ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);  
		   byte[] buffer = new byte[1024];  
		   while (!inflater.finished()) {  
		    int count = inflater.inflate(buffer);  
		    outputStream.write(buffer, 0, count);  
		   }  
		   outputStream.close();  
		   byte[] output = outputStream.toByteArray();   
		   return output;  
		  }  
	
	public Object decodeMouseMove(JsonObject json){
		return json.getString("data");
	}
	
	public Object decodeAction(JsonObject json){
		return null;
	}
}
