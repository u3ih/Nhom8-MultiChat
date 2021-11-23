
package view;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.ActionType;
import model.Result;
import model.ResultCode;
import model.Room;
import model.ThreadNewRoom;

import javax.swing.JScrollPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import controller.ClientManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author ADMIN
 */
public class RoomListForm extends javax.swing.JPanel implements Observer {

    /**
     * Creates new form friendListForm
     */
	ClientManager clientManager;
	private JList<Room> list;
	private DefaultListModel<Room> listRoomModel;
	private JScrollPane scrollPane;
	private GroupLayout groupLayout;
	private HashMap<String,ThreadNewRoom> listThread = new HashMap<String,ThreadNewRoom>();
	
    public RoomListForm(ClientManager clientManager) {
    	initComponents();
    	this.clientManager = clientManager;
    	clientManager.addObserver(this);
    	listRoomModel = (DefaultListModel<Room>) list.getModel();
    	clientManager.GetListRoom();
//    	list.addListSelectionListener(new ListSelectionListener() {
//			
//			@Override
//			public void valueChanged(ListSelectionEvent e) {
//				int s = list.getSelectedIndex();
//				Room room = listRoomModel.elementAt(s);
//				ThreadNewRoom newThreadRoom = new ThreadNewRoom(new ChatGroupForm(clientManager,room.getIdRoom(),room.getNameRoom(),room.getCountPeople()));
//                newThreadRoom.run();
//                clientManager.getMess(room.getIdRoom());
//			}
//		});
        
    }
    

	public void setListModel(Room room) {
    	listRoomModel.addElement(room);
    }
    
public void initList(Result result) {
        
	   
    if(result.mContent.length()>0)
    {
        listRoomModel.clear();
        String[] rows = result.mContent.split("<row>");
        for (int i = 0; i < rows.length; i++) //hàng đầu là trống
        {
            String[] cols = rows[i].split("<col>");
            listRoomModel.addElement(new Room(cols[0],cols[1],Integer.parseInt(cols[2]),cols[3]));
            
        }
    }
	
    }

    
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
    	list = new JList<Room>(new DefaultListModel<Room>());
    	list.addMouseListener(new MouseAdapter() {
    		@Override
    		public void mouseClicked(MouseEvent e) {
    			int s = list.getSelectedIndex();
				Room room = listRoomModel.elementAt(s);
                if(listThread.containsKey(room.getIdRoom())) {
                	return;
                }
                else {
    				ThreadNewRoom newThreadRoom = new ThreadNewRoom(new ChatGroupForm(clientManager,room.getIdRoom(),room.getNameRoom(),room.getCountPeople()));
                	newThreadRoom.run();				
                    listThread.put(room.getIdRoom(), newThreadRoom);
                    
                }
                
                list.setSelectedIndex(0);
    		}
    	});
    	
    	list.setCellRenderer(new RoomListElement());
    	scrollPane.setViewportView(list);
    	this.setLayout(groupLayout);
    }// </editor-fold>//GEN-END:initComponents

	@Override
	public void update(Observable o, Object arg) {
		
		Result result = (Result)arg;
        if(result.mResultCode.equals(ResultCode.ERROR))
        {
            JOptionPane.showMessageDialog(null, result.mContent, "Tháº¥t báº¡i", JOptionPane.ERROR_MESSAGE);
            return;
        }
        switch (result.mActionType)
        {
        	case ActionType.GET_LIST_ROOM:
        	{
        		initList(result);
        		break;
        	}
        	case ActionType.NOTIFY_JUST_JOIN_ROOM:
            {
            	String[] lines = result.mContent.split(";", -1);
            	String userJoin = lines[1];
                for(int i=0;i<listRoomModel.size();i++){
                	if(listRoomModel.elementAt(i).getNameRoom().equals(lines[0])) 
                	{
                		int count = listRoomModel.elementAt(i).getCountPeople()+1;
                		listRoomModel.elementAt(i).setCountPeople(count);
                		return;
                	}
                }
                
                break;
            }
        	case ActionType.SEND_MESSAGE:
            {
                String[] lines = result.mContent.split(";", -1);
                String sender = lines[1];
                String messContent = lines[2];
                for(int i=0;i<listRoomModel.size();i++){
                	if(listRoomModel.elementAt(i).getNameRoom().equals(lines[0])) 
                	{
                		listRoomModel.elementAt(i).setLastMess(sender +": "+ messContent);
                		return;
                	}
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
