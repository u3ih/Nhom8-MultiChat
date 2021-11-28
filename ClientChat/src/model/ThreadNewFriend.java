package model;

import view.ChatForm;

public class ThreadNewFriend {
	
	private ChatForm chat;
	private boolean exit = false;
	
	public ThreadNewFriend(ChatForm chat) {
		this.chat = chat;
	}

	public void run() {
		
		chat.setVisible(true);
//		chat.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
	}

	public void stop() {
	
		exit = true;
	}
	
	
}
