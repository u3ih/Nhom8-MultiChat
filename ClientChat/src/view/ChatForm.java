package view;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;

import controller.ClientManager;
import model.ActionType;
import model.DataFile;
import model.Result;
import model.ResultCode;

/**
 *
 * @author ADMIN
 */
public class ChatForm extends javax.swing.JFrame implements Observer{

	ClientManager mClientManager;
    String mUserFriend;
    JFrame fileJFrame = new JFrame();
    private javax.swing.JList<String> list;
    private DefaultListModel mod;
    
    public ChatForm(ClientManager clientManager, String mUserFriend) {
        initComponents();
        this.mClientManager = clientManager;
        this.mUserFriend = mUserFriend;
        mClientManager.addObserver(this);
        //mClientManager.getMess(mMaPhong);
    }

   
    private void closeWindow() {
    	mClientManager.deleteObserver(this);
    	mClientManager.closeWindow(mUserFriend);
    }
    
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtNoiDungChat = new javax.swing.JTextArea();
        jTextField2 = new javax.swing.JTextField();
        btnSendFile = new javax.swing.JButton();
        btnSendFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
		            JFileChooser ch = new JFileChooser();
		            int c = ch.showOpenDialog(fileJFrame);
		            if (c == JFileChooser.APPROVE_OPTION) {
		                File file = ch.getSelectedFile();
		                FileInputStream in = new FileInputStream(file);
		                byte b[] = new byte[in.available()];
		                in.read(b);
		                DataFile data = new DataFile();
		                //data.setStatus(jComboBox1.getSelectedItem() + "");
		                data.setName(file.getName());
		                data.setFile(b);
		                mClientManager.SendFileFriend(mUserFriend, data);
		            }
		        } catch (Exception exp) {
		            JOptionPane.showMessageDialog(fileJFrame, exp, "Error", JOptionPane.ERROR_MESSAGE);
		        }
			}
		});
        jScrollPane3 = new javax.swing.JScrollPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
            	closeWindow();
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
            	setTitle("Đang chat với: " + mUserFriend);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/icons8-phone-24.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        txtNoiDungChat.setEditable(false);
        txtNoiDungChat.setColumns(20);
        txtNoiDungChat.setRows(5);
        jScrollPane1.setViewportView(txtNoiDungChat);

        jTextField2.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        btnSendFile.setText("Chuyển file");
        
        btnSendGroup = new JButton("Gửi");
        btnSendGroup.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String mess = jTextField2.getText().trim();
                if(mess.length() ==0) {return;}
                //System.out.println(mUserFriend+": " + mess);
                
                mClientManager.SendMessFriend(mUserFriend, mess);
                jTextField2.setText("");
        	}
        });
        
        JLabel lblNewLabel = new JLabel("file đã nhận");
        
        scrollPane = new JScrollPane();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addComponent(jTextField2, GroupLayout.PREFERRED_SIZE, 371, GroupLayout.PREFERRED_SIZE)
        				.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 371, GroupLayout.PREFERRED_SIZE)
        				.addComponent(btnSendFile))
        			.addGap(18)
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
        					.addGroup(layout.createSequentialGroup()
        						.addComponent(btnSendGroup, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
        						.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        						.addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE))
        					.addGroup(layout.createSequentialGroup()
        						.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
        						.addGap(100)
        						.addComponent(jScrollPane3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
        				.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 168, GroupLayout.PREFERRED_SIZE))
        			.addContainerGap(17, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addGap(21)
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(layout.createSequentialGroup()
        					.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 386, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(btnSendFile))
        				.addGroup(layout.createSequentialGroup()
        					.addGap(13)
        					.addGroup(layout.createParallelGroup(Alignment.LEADING)
        						.addGroup(layout.createSequentialGroup()
        							.addGap(29)
        							.addComponent(jScrollPane3, GroupLayout.PREFERRED_SIZE, 205, GroupLayout.PREFERRED_SIZE))
        						.addGroup(layout.createSequentialGroup()
        							.addGap(215)
        							.addComponent(lblNewLabel)))
        					.addGap(18)
        					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 149, GroupLayout.PREFERRED_SIZE)))
        			.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        					.addComponent(jTextField2, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE)
        					.addComponent(btnSendGroup, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
        				.addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE))
        			.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        
        list = new javax.swing.JList<>();
        list.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		if (e.getClickCount() == 2) {
                    if (!list.isSelectionEmpty()) {
                        if (SwingUtilities.isLeftMouseButton(e)) {
                        	save();
                        }
                    }
                }
        	}
        });
        mod = new DefaultListModel();
        list.setModel(mod);
        scrollPane.setViewportView(list);
        
        getContentPane().setLayout(layout);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        
    }
    
    private void save() {
        DataFile data = (DataFile) mod.getElementAt(list.getSelectedIndex());
        JFileChooser ch = new JFileChooser();
        int c = ch.showSaveDialog(this);
        if (c == JFileChooser.APPROVE_OPTION) {
            try {
                FileOutputStream out = new FileOutputStream(ch.getSelectedFile());
                out.write(data.getFile());
                out.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    
    private javax.swing.JButton jButton1;
    private javax.swing.JButton btnSendFile;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea txtNoiDungChat;
    private javax.swing.JTextField jTextField2;
    private JButton btnSendGroup;
    private JScrollPane scrollPane;
    private JList list_1;
    // End of variables declaration//GEN-END:variables
	@Override
	public void update(Observable o, Object arg) {
Result result = (Result)arg;
        
        if(result.mResultCode.equals(ResultCode.ERROR))
        {
            JOptionPane.showMessageDialog(null, result.mContent, "ThÃ¡ÂºÂ¥t bÃ¡ÂºÂ¡i", JOptionPane.ERROR_MESSAGE);
            return;
        }
        switch (result.mActionType)
        {
        case ActionType.SEND_MESSAGE:
        {
            String[] lines = result.mContent.split(";", -1);
            //if(lines[0].equals(mMaFriend)) {
            	String sender = lines[1];
                String messContent = lines[2];
                //System.out.println(sender +" " + messContent +"\n");
                messContent = messContent.replaceAll("<br>", "\n");  //khi tin nhÃ¡ÂºÂ¯n gÃ¡Â»Â­i Ã„â€˜i Ã„â€˜Ä‚Â£ thay kÄ‚Â½ tÃ¡Â»Â± xuÃ¡Â»â€˜ng dÄ‚Â²ng bÃ¡ÂºÂ±ng <br> nÄ‚Âªn khi nhÃ¡ÂºÂ­n vÃ¡Â»ï¿½ thÄ‚Â¬ thay ngÃ†Â°Ã¡Â»Â£c lÃ¡ÂºÂ¡i
                if(sender.equals(mClientManager.mNickname))
                	txtNoiDungChat.append("Me: " + messContent + "\n");
                else
                	txtNoiDungChat.append(sender + ": " + messContent + "\n");
            //}
            break;
        }
//        case ActionType.GET_MESS:
//    	{
//    		if(result.mContent.length()>0)
//    	    {
//    	        String[] lines = result.mContent.split(";",-1);
//    	        if(lines[0].equals(mMaPhong)) {
//    	        	String[] rows = lines[1].split("<row>");
//    	        		for (int i = 0; i < rows.length; i++) //hÄ‚Â ng Ã„â€˜Ã¡ÂºÂ§u lÄ‚Â  trÃ¡Â»â€˜ng
//    	        		{
//    	        			String[] cols = rows[i].split("<col>");
//    	            
//    	        			String sender = cols[0];
//    	        			String messContent = cols[1];
//    	        			if(sender.equals(mClientManager.mNickname))
//    	        				txtNoiDungChat.append("Me: " + messContent + "\n");    
//    	        			else
//    	        				txtNoiDungChat.append(sender + ": " + messContent + "\n");
//    	            
//    	        }}
//    	    }
//    		break;
//    	}
            case ActionType.SEND_FILE:
            {
            	String[] lines = result.mContent.split(";", -1);
            	String sender = lines[0];
                String username = lines[1];
                
                if(username.equals(mClientManager.mNickname))
                    txtNoiDungChat.append("Me Send file: "+ result.file.getName() +"\n");
                else
                    txtNoiDungChat.append(username + " send file " + result.file.getName() + "\n");
                //System.out.println(result.file.getName());
                mod.addElement(result.file);
                break;
            }
//            case ActionType.GET_ROOM_MEMBER:
//        	{
//        		System.out.println(result.mContent + "\n");
//        		if(result.mContent.length()>0)
//        	    {
//        			String[] lines = result.mContent.split(";",-1);
//        			System.out.println(lines[0]+" ");
//        			if(lines[0].equals(mMaPhong)) {
//        				jTextArea2.setText("");
//        	        for (int i = 1; i < lines.length; i++)
//        	        {
//        	        	System.out.println(lines[i]+" ");
//        	            jTextArea2.append(lines[i] + "\n");
//        	            
//        	        }
//        			}
//        	    }
//        		break;
//        	}
//            case ActionType.UPDATE_NUMBER_USER:
//            {
//                String soNguoi = result.mContent;
//                mSoNguoi = Integer.parseInt(soNguoi);
//                setTitle("ID: " + mMaPhong + " Name: " + mTenPhong + " So nguoi trong phong: " + mSoNguoi);
//                break;
//            }
           case ActionType.NOTIFY_JUST_JOIN_ROOM:
            {
            	String[] lines = result.mContent.split(";", -1);
            	if(lines[0].equals(mUserFriend)) {
            		String userJoin = lines[1];
            		
            	}
//                jTextArea2.append(userJoin + "\n");
                break;
            }
        }
		
	}
}
