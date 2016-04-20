package coders;


public class Message {
	private String action;
	private Object data;
	
	public Message() {
	}
	
	public Message(final String action,final Object data) {
		this.action = action;
		this.data = data;
	}
	
	public String getAction() {
		return action;
	}
	
	public Object getData() {
		return data;
	}

	public void setAction( final String action ) {
		this.action = action;
	}
	
	public void setData( final Object data) {
		this.data = data;
	}

}
