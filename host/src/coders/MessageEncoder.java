package coders;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MessageEncoder implements Encoder.Text< Message > {
	DataEncoder enc=new DataEncoder();
	
	@Override
	public void init( final EndpointConfig config ) {
	}

	@Override
	public String encode( final Message message ) throws EncodeException {
		return enc.encodeMessage(message);
	}
	

	@Override
	public void destroy() {
	}
}
