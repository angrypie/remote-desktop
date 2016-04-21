package coders;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.json.Json;

public class DataEncoder {
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

	public String encodeImage(Message message){
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
}
