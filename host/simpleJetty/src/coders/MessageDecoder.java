package coders;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Base64;
import java.util.Collections;

import javax.imageio.ImageIO;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class MessageDecoder implements Decoder.Text< Message > {
	private JsonReaderFactory factory = Json.createReaderFactory( Collections.< String, Object >emptyMap() );

	@Override
	public void init( final EndpointConfig config ) {
	}

	@Override
	public Message decode( final String str ) throws DecodeException {
		final Message message = new Message();

		try( final JsonReader reader = factory.createReader( new StringReader( str ) ) ) {
			final JsonObject json = reader.readObject();
			String imageString=json.getString("icon");
			BufferedImage bImage = null;
	        try {
	            byte[] output = Base64.getDecoder().decode(imageString);
	            ByteArrayInputStream bais = new ByteArrayInputStream(output);
	            bImage = ImageIO.read(bais);
	        } catch (IOException ex) {
	        
	        }
	        System.out.println(bImage);
			message.setIcon(bImage);
			message.setMessage( json.getString( "message" ) );
		}

		return message;
	}

	@Override
	public boolean willDecode( final String str ) {
		System.out.println("willDecode");
		return true;
	}

	@Override
	public void destroy() {
	}
}