package coders;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.json.JsonObject;

public class DataDecoder {
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
	
	public Object decodeMouseClick(JsonObject json){
		return null;
	}
}
