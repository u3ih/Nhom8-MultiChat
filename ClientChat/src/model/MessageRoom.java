package model;

public class MessageRoom {

	private String sender;
	private String mess;
	

	
	public MessageRoom(String sender, String mess) {
		this.sender = sender;
		this.mess = mess;
	}

	public String getSender() {
		return sender;
	}
	
	public String getMess() {
		return mess;
	}
	
}
