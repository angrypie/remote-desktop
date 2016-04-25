package coders;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.json.Json;

public class DataEncoder {
	
	public String encodeMessage(Message message){
		String encode;
		if(message.getAction()=="IMG_FRAME")encode=encodeImage(message);
		else if(message.getAction().compareTo("MOUSE_MOVE")==0)encode=encodeMoveMouse(message);
		else if(message.getAction().compareTo("MOUSE_RCLICK")==0 || message.getAction().compareTo("MOUSE_LCLICK")==0)
			encode=encodeMouseClick(message);
		else if(message.getAction().compareTo("HOST_REGISTER")==0)encode=encodeRegisterMessage(message);
		else encode=defaultMessage(message);
		return encode;
	}
	
	private String encodeRegisterMessage(Message message) {
		return Json.createObjectBuilder().add("action",message.getAction())
				.add("data", (String)message.getData()).build().toString();
	}

	private String encodeMouseClick(Message message){
		return Json.createObjectBuilder()
				.add( "action", message.getAction() )
				.build().toString();
	}
	
	private String encodeMoveMouse(Message message){
		return Json.createObjectBuilder()
				.add("data", (String)message.getData())
				.add( "action", message.getAction() )
				.build().toString();
	}

	private String encodeImage(Message message){
		String imageString = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write((BufferedImage)message.getData(),"jpg", baos);
			byte[] imageAsRawBytes = baos.toByteArray();
			imageString = new String(Base64.getEncoder().encode(imageAsRawBytes));
		} catch (IOException ex) {
		}

		return Json.createObjectBuilder()
				.add("data", imageString)
				.add( "action", message.getAction() )
				.build()
				.toString();
	}
	
	private String defaultMessage(Message message){
		return Json.createObjectBuilder()
				.add( "action", message.getAction() )
				.build().toString();
	}
}
