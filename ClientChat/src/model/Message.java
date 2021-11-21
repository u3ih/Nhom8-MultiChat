package model;

public class Message {

	private String sender;
	private String mess;
	private String sendDate;
	
	public Message(String sender, String mess, String sendDate) {
		this.sender = sender;
		this.mess = mess;
		this.sendDate = sendDate;
	}
	
	public String getSender() {
		return sender;
	}
	
	public String getMess() {
		return mess;
	}
	
	public String getDate() {
		return sendDate;
	}
}
