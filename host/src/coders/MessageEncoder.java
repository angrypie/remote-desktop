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
		String encodeMessage = null;
		if(message.getAction()=="IMG_FRAME")encodeMessage=enc.encodeImage(message);
		else if(message.getAction().compareTo("MOUSE_MOVE")==0)encodeMessage=enc.encodeMoveMouse(message);
		else if(message.getAction().compareTo("MOUSE_RCLICK")==0 || message.getAction().compareTo("MOUSE_LCLICK")==0)
			encodeMessage=enc.encodeMouseClick(message);
		return encodeMessage;
	}
	

	@Override
	public void destroy() {
	}
}
