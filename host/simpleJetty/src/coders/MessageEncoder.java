package coders;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.json.Json;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MessageEncoder implements Encoder.Text< Message > {
	BufferedImage img;
	@Override
	public void init( final EndpointConfig config ) {
	}

	@Override
	public String encode( final Message message ) throws EncodeException {
		String imageString = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(message.getIcon(),"jpg", baos);
			byte[] imageAsRawBytes = baos.toByteArray();
			imageString = new String(Base64.getEncoder().encode(imageAsRawBytes));
		} catch (IOException ex) {
		}

		return Json.createObjectBuilder()
				.add("icon", imageString)
				.add( "message", message.getMessage() )
				.build()
				.toString();
	}

	@Override
	public void destroy() {
	}
}
