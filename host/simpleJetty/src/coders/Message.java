package coders;

import java.awt.image.BufferedImage;

public class Message {
	private String message;
	private BufferedImage icon;
	
	public Message() {
	}
	
	public Message( final BufferedImage icon, final String message ) {
		this.icon = icon;
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
	public BufferedImage getIcon() {
		return icon;
	}

	public void setMessage( final String message ) {
		this.message = message;
	}
	
	public void setIcon( final BufferedImage icon) {
		this.icon = icon;
	}

}
