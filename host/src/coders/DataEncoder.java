package coders;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.zip.Deflater;

import javax.imageio.ImageIO;
import javax.json.Json;

public class DataEncoder {
	
	public String encodeMessage(Message message){
		String encode;
		if(message.getAction()=="IMG_FRAME")encode=encodeImage(message);
		else if(message.getAction().compareTo("MOUSE_MOVE")==0)encode=encodeMoveMouse(message);
		else if(message.getAction().compareTo("MOUSE_RCLICK")==0 || message.getAction().compareTo("MOUSE_LCLICK")==0)
			encode=encodeMouseClick(message);
		else encode=defaultMessage(message);
		return encode;
	}
	
	public String encodeMouseClick(Message message){
		return Json.createObjectBuilder()
				.add( "action", message.getAction() )
				.build().toString();
	}
	
	public String encodeMoveMouse(Message message){
		return Json.createObjectBuilder()
				.add("data", (String)message.getData())
				.add( "action", message.getAction() )
				.build().toString();
	}
	
	public static byte[] compress(byte[] data) throws IOException {  
		   Deflater deflater = new Deflater();  
		   deflater.setInput(data);  
		   ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);   
		   deflater.finish();  
		   byte[] buffer = new byte[1024];   
		   while (!deflater.finished()) {  
		    int count = deflater.deflate(buffer); // returns the generated code... index  
		    outputStream.write(buffer, 0, count);   
		   }  
		   outputStream.close();  
		   byte[] output = outputStream.toByteArray();  
		   //System.err.println("Original: " + data.length / 1024 + " Kb");  
		   //System.err.println("Compressed: " + output.length / 1024 + " Kb");  
		   return output;  
		  }  

	public String encodeImage(Message message){
		String imageString = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write((BufferedImage)message.getData(),"jpg", baos);
			byte[] imageAsRawBytes = baos.toByteArray();
			//imageAsRawBytes=compress(imageAsRawBytes);
			imageString = new String(Base64.getEncoder().encode(imageAsRawBytes));
		} catch (IOException ex) {
		}

		return Json.createObjectBuilder()
				.add("data", imageString)
				.add( "action", message.getAction() )
				.build()
				.toString();
	}
	
	public String defaultMessage(Message message){
		return Json.createObjectBuilder()
				.add( "action", message.getAction() )
				.build().toString();
	}
}
