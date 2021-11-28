
package view;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import controller.ClientManager;
import model.ActionType;
import model.Result;
import model.ResultCode;
import model.Room;
import model.ThreadNewFriend;
import model.ThreadNewRoom;
import model.User;

/**
 *
 * @author ADMIN
 */
public class FriendListForm extends javax.swing.JPanel implements Observer{

    /**
     * Creates new form friendListForm
     */
	ClientManager clientManager;
	private JList<User> list;
	private DefaultListModel<User> listUserFriendModel;
	private JScrollPane scrollPane;
	private GroupLayout groupLayout;
	private HashMap<String,ThreadNewFriend> listThread = new HashMap<String,ThreadNewFriend>();
	
    public FriendListForm(ClientManager clientManager, HashMap<String,ThreadNewFriend> listThread) {
        initComponents();
        this.clientManager = clientManager;
        this.listThread = listThread;
    	clientManager.addObserver(this);
    	listUserFriendModel = (DefaultListModel<User>) list.getModel();
    	clientManager.GetListFriend(clientManager.mNickname);

    }
    
    public void addThread(String id, ThreadNewFriend thread) {
    	listThread.put(id, thread);
    }
    
    public void checkFriend(User user) {
    	int tmp=-1;
    	for(int i=0;i<listUserFriendModel.size();i++) {
    		if(listUserFriendModel.get(i).getId() == user.getId()) {
    			tmp=i;
    		}
    		
    	}
    	if(tmp != -1) {
    		user.setOnline(true);
    		listUserFriendModel.set(tmp, user);
    	}
    	
    }
    public void setListModel(User u) {
    	listUserFriendModel.addElement(u);
    }
    public void initList(Result result) {
        
 	   
        if(result.mContent.length()>0)
        {
            //ds ban có dạng firstname<col>midname<col>lastname<col>birthday<col>age<col>gender<col><row>
            //                firstname<col>midname<col>lastname<col>birthday<col>age<col>gender<col><row>
        	for(int i=0;i<listUserFriendModel.getSize();i++) {
        		listUserFriendModel.remove(i);
	        }
            String[] rows = result.mContent.split("<row>");
            for (int i = 0; i < rows.length; i++) //hàng đầu là trống
            {
                String[] cols = rows[i].split("<col>");
               // listUserFriendModel.addElement(new Room(cols[0],cols[1],Integer.parseInt(cols[2])));
                listUserFriendModel.addElement(new User(Integer.parseInt(cols[7]),cols[0],cols[1],cols[2],cols[3],Integer.parseInt(cols[4]),cols[5],Boolean.parseBoolean(cols[6])));
            }
        }
    	
        }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

    	scrollPane = new JScrollPane();
    	groupLayout = new GroupLayout(this);
    	groupLayout.setHorizontalGroup(
    		groupLayout.createParallelGroup(Alignment.LEADING)
    			.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
    				.addContainerGap()
    				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
    				.addContainerGap())
    	);
    	groupLayout.setVerticalGroup(
    		groupLayout.createParallelGroup(Alignment.LEADING)
    			.addGroup(groupLayout.createSequentialGroup()
    				.addContainerGap()
    				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
    				.addContainerGap())
    	);
    	list = new JList<User>(new DefaultListModel<User>());
    	list.setDragEnabled(true);
    	list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	list.addMouseListener(new MouseAdapter() {
    		@Override
    		public void mouseClicked(MouseEvent e) {
    			int s = list.getSelectedIndex();
				User user = listUserFriendModel.elementAt(s);
				//System.out.println(user);
                if(listThread.containsKey(user.getUsername())) {
                	return;
                }
                else {
    				ThreadNewFriend newThreadFriend = new ThreadNewFriend(new ChatForm(clientManager, user.getUsername()));
    				newThreadFriend.run();
                    listThread.put(user.getUsername(), newThreadFriend);
                }
                
                list.setSelectedIndex(0);
    		}
    	});
    	
    	list.setCellRenderer(new UserFriendListElement());
    	scrollPane.setViewportView(list);
    	this.setLayout(groupLayout);

    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		Result result = (Result)arg;
        if(result.mResultCode.equals(ResultCode.ERROR))
        {
            JOptionPane.showMessageDialog(null, result.mContent, "Thất bại", JOptionPane.ERROR_MESSAGE);
            return;
        }
        switch (result.mActionType)
        {
        	case ActionType.GET_LIST_FRIEND:
        	{
        		initList(result);
        		break;
        	}
        	case ActionType.SEND_MESSAGE:
            {
                String[] lines = result.mContent.split(";", -1);
                String sender = lines[1];
                String messContent = lines[2];
                for(int i=0;i<listUserFriendModel.size();i++){
//                	if(listUserFriendModel.elementAt(i).getUsername().equals(lines[0])) 
//                	{
//                		User u = listUserFriendModel.elementAt(i);
//                		//u.setLastMess(sender +": "+ messContent);
//                		//System.out.println(r.toString());
//                		listUserFriendModel.remove(i);
//                		listUserFriendModel.addElement(u);
//                		
//                		return;
//                	}
                }
                break;
            }
        	case ActionType.Close_WINDOW_CHAT:
            {
            	if (listThread.containsKey(result.mContent)) listThread.remove(result.mContent);
            }
        }
	}		
}
