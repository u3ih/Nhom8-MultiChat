package model;

public class Message {

	private String sender;
	private String mess;
	
//	public Message(String sender, String mess, String sendDate) {
//		this.sender = sender;
//		this.mess = mess;
//		this.sendDate = sendDate;
//	}
	
	public Message(String sender, String mess) {
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
