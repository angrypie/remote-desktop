package coders;

import java.io.StringReader;
import java.util.Collections;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class MessageDecoder implements Decoder.Text< Message > {
	private JsonReaderFactory factory = Json.createReaderFactory( Collections.< String, Object >emptyMap() );
	private DataDecoder dec=new DataDecoder();

	@Override
	public void init( final EndpointConfig config ) {
	}

	@Override
	public Message decode( final String str ) throws DecodeException {
		final Message message = new Message();
		JsonObject json=getJson(str);
		String action=json.getString("action");
		message.setAction(action);
		message.setData(dec.getData(json,action));

		return message;
	}

	public JsonObject getJson(final String str){
		final JsonReader reader = factory.createReader( new StringReader( str ) );
		final JsonObject json = reader.readObject();
		return json;

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