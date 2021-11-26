package model;

import javax.swing.JFrame;

import view.ChatGroupForm;

public class ThreadNewRoom {
	
	private ChatGroupForm chat;
	private boolean exit = false;
	
	public ThreadNewRoom(ChatGroupForm chat) {
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
