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
			Object data=null;
			String action=json.getString("action");
			message.setAction(action);
			if(action.compareTo("IMG_FRAME")==0){
				data=decodeImage(json);
			}else if(action.compareTo("MOUSE_MOVE")==0) data=json.getString("data");
			else if(action.compareTo("MOUSE_RCLICK")==0 || action.compareTo("MOUSE_LCLICK")==0)data=null;
			message.setData(data);
		}

		return message;
	}

	private Object decodeImage(JsonObject json){
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

	@Override
	public boolean willDecode( final String str ) {
		System.out.println("willDecode");
		return true;
	}

	@Override
	public void destroy() {
	}
}