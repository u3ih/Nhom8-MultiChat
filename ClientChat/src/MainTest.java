import javax.swing.JFrame;

import controller.ClientManager;
import view.ChatGroupForm;
import view.ListForm;
import view.LoginForm;

public class MainTest {
	public static void main(String[] args) {
		Thread t1 = new createChatRoom(new ChatGroupForm(new ClientManager(new LoginForm()),"fgd", "fds", 1));
		Thread t2 = new createChatRoom(new ChatGroupForm(new ClientManager(new LoginForm()),"acvx", "dsax", 1));
		t1.start();
		t2.start();
		
	}
	
	private static class createChatRoom extends Thread{
		
		private ChatGroupForm chat;
		
		public createChatRoom(ChatGroupForm chat) {
			this.chat = chat;
		}
		
		@Override
		public void run() {
			
			chat.setVisible(true);
			chat.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			int x = 0;
			while(true) {
				try {
					Thread.sleep(1000);
					System.out.println("1");
					x++;
					if(x>3) interrupt();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}
}
