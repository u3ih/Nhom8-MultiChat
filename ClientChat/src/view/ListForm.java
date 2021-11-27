package view;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout.Alignment;

import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.event.AncestorListener;
import javax.swing.table.DefaultTableModel;

import controller.ClientManager;
import model.ActionType;
import model.Result;
import model.ResultCode;
import model.Room;
import model.ThreadNewRoom;
import model.User;

import javax.swing.event.AncestorEvent;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;

/**
 *
 * @author ADMIN
 */
public class ListForm extends javax.swing.JFrame implements Observer {

    /**
     * Creates new form ListForm
     */
	 ClientManager mclientManager;
	 String mNickName;
	 LoginForm loginForm;
	 private String createNameRoom = "";
	 private String findIDRoom = "";
	 private HashMap<String,ThreadNewRoom> listThread = new HashMap<String,ThreadNewRoom>();
	 private RoomListForm r;
	 private FriendListForm p;
	
    public ListForm(ClientManager clientManager,LoginForm loginForm) {
        initComponents();
        this.mclientManager = clientManager;
        this.loginForm = loginForm;
        clientManager.addObserver(this);
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		btntim(e);
        	}
        });
        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        jButton2.setText("jButton2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
        	public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            
        });
        jButton1.setText("Thêm bạn");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
       // jLabel6.setText("Wjpu Lord");

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/10207-man-student-light-skin-tone-icon-64.png")));
        
        JButton createRoom = new JButton("Tạo phòng chat");
        
        JButton findRoom = new JButton("Tìm phòng chat");
        
        createRoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	String s = (String)JOptionPane.showInputDialog(getContentPane(),"Nhap ten phong","Tao phong moi",JOptionPane.PLAIN_MESSAGE);
            	createNameRoom = s;
            	mclientManager.CreateRoom(s);
            }
        });
        
        findRoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	String s = (String)JOptionPane.showInputDialog(getContentPane(),"Nhap id phong","Tim phong chat",JOptionPane.PLAIN_MESSAGE);
            	findIDRoom = s;
            	mclientManager.JoinRoom(s);
            }
        });
        
        
        
        
        
        
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        
        tabbedPane.addAncestorListener(new AncestorListener() {
			
			@Override
			public void ancestorRemoved(AncestorEvent event) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void ancestorMoved(AncestorEvent event) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void ancestorAdded(AncestorEvent event) {
				 p=new FriendListForm(mclientManager);
                tabbedPane.add(p,"danh sách bạn");
                r= new RoomListForm(mclientManager,listThread);
                tabbedPane.add(r,"danh sách phòng");
				
			}
		});
        
        JButton btnNewButton = new JButton("View Profile");
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
					jButtonViewInfoPerformed(e);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	}
        });
        
        JButton btnLogout = new JButton("Đăng xuất");
        btnLogout.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		btnLogoutActionPerformed(e);
        	}

			
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addGap(18)
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 423, Short.MAX_VALUE)
        				.addGroup(layout.createSequentialGroup()
        					.addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addGroup(layout.createParallelGroup(Alignment.LEADING)
        						.addComponent(jLabel6, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
        						.addGroup(layout.createSequentialGroup()
        							.addComponent(createRoom)
        							.addGap(18)
        							.addComponent(findRoom)))
        					.addPreferredGap(ComponentPlacement.RELATED, 93, Short.MAX_VALUE)))
        			.addContainerGap())
        		.addGroup(layout.createSequentialGroup()
        			.addGap(29)
        			.addComponent(jButton1)
        			.addGap(93)
        			.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
        			.addComponent(btnLogout)
        			.addGap(21))
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addGap(14)
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE)
        				.addGroup(layout.createSequentialGroup()
        					.addComponent(jLabel6, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
        					.addGap(18)
        					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        						.addComponent(createRoom)
        						.addComponent(findRoom))))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 390, GroupLayout.PREFERRED_SIZE)
        			.addGap(72)
        			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(jButton1)
        				.addComponent(btnNewButton)
        				.addComponent(btnLogout))
        			.addContainerGap(36, Short.MAX_VALUE))
        );
        getContentPane().setLayout(layout);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    protected void formWindowClosing(WindowEvent evt) {
		// TODO Auto-generated method stub
    	mclientManager.callOffline(mclientManager.mNickname);
    	mclientManager.Logout();
    	mclientManager.Dispose();
	}

	private void btnLogoutActionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
    	mclientManager.Logout();
    	mclientManager.Dispose();
    	loginForm.setEmptyText();
        loginForm.setVisible(true);
        this.dispose();
	}
    protected void btntim(ActionEvent e) {
		// TODO Auto-generated method stub
    	friendAddForm faf= new friendAddForm(this, mclientManager,p);
		faf.setVisible(true);
	}

	protected void formWindowOpened(WindowEvent evt) {
		jLabel6.setText(mclientManager.mNickname);
		mclientManager.callOnline(mclientManager.mNickname);
//		mclientManager.GetListFriend(mclientManager.mNickname);
	}

	protected void jButtonViewInfoPerformed(ActionEvent e) throws IOException {
		// TODO Auto-generated method stub
		ViewProfileForm v= new ViewProfileForm(mclientManager);
		v.setVisible(true);
	}
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ListForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ListForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ListForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ListForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel6;
    private JTabbedPane tabbedPane;
    
    
    
	@Override
	public void update(Observable o, Object arg) {

        Result result = (Result)arg;
        if(result.mResultCode.equals(ResultCode.ERROR))
        {
            JOptionPane.showMessageDialog(null, result.mContent, "Thất bại", JOptionPane.ERROR_MESSAGE);
            return;
        }
        switch (result.mActionType)
        {
            case ActionType.CREATE_ROOM:
            {
            	String[] lines = result.mContent.split(";", -1);
                ThreadNewRoom newThreadRoom = new ThreadNewRoom(new ChatGroupForm(mclientManager,lines[0],createNameRoom,1 ));
                newThreadRoom.run();;
                listThread.put(lines[0], newThreadRoom);
                r.setListModel(new Room(lines[0],createNameRoom,1,"null"));
                mclientManager.GetListRoom();
                break;
            }
            case ActionType.JOIN_ROOM:
            {
            	String[] lines = result.mContent.split(";", -1);
            	if(listThread.containsKey(findIDRoom)) return;
            	else {
                ThreadNewRoom newThreadRoom = new ThreadNewRoom(new ChatGroupForm(mclientManager,findIDRoom,lines[0],Integer.parseInt(lines[1]) ));
                newThreadRoom.run();
                mclientManager.GetListRoom();
                r.addThread(findIDRoom, newThreadRoom);
            	}
                //r.setListModel(new Room(findIDRoom,lines[0],Integer.parseInt(lines[1])));
                break;
           }
            case ActionType.CALL_ONLINE:
            {
            	String[] lines = result.mContent.split(";", -1);
            	User u= new User();
            	u.setFirstName(lines[0]);
            	u.setMidName(lines[1]);
            	u.setLastName(lines[2]);
            	u.setBirthDay(lines[3]);
            	u.setAge(Integer.parseInt(lines[4]));
            	u.setGender(lines[5]);
            	u.setOnline(Boolean.parseBoolean(lines[6]));
            	u.setId(Integer.parseInt(lines[7]));
            	p.checkFriend(u);
            	break;
            }
            case ActionType.Close_WINDOW_CHAT:
            {
            	if (listThread.containsKey(result.mContent)) listThread.remove(result.mContent);
            	break;
            }
            
//            case ActionType.GET_LIST_FRIEND:
//            {
//            	textField.setText(result.mContent);
//            	String[] lines = result.mContent.split("<row>");
//            	int size = lines.length;
//            	for(int i=0;i<size;i++) {
//            		String[] lin = lines[i].split("<col>");
//                	
//                	//p.setListModel(new User(lin[0],lin[1],lin[2],lin[3],Integer.parseInt(lin[4]),lin[5]));
//            	}
//          	break;
//            }
        }   
	}
}
