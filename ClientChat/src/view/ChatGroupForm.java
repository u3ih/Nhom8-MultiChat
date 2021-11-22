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
public class ChatGroupForm extends javax.swing.JFrame implements Observer{

    /**
     * Creates new form chatGroupForm
     * 
     */
	ClientManager mClientManager;
    String mMaPhong = "";
    String mTenPhong = "";
    int mSoNguoi = 1;
    JFrame fileJFrame = new JFrame();
    private javax.swing.JList<String> list;
    private DefaultListModel mod;
    
    public ChatGroupForm(ClientManager clientManager,String mMaPhong,String mTenPhong,int mSoNguoi) {
        initComponents();
        this.mClientManager = clientManager;
        this.mMaPhong = mMaPhong;
        this.mTenPhong = mTenPhong;
        this.mSoNguoi = mSoNguoi;
        mClientManager.addObserver(this);
        //mClientManager.GetRoomMember(mMaPhong);
    }

   
    private void closeWindow() {
    	mClientManager.deleteObserver(this);
    	mClientManager.closeWindow(mMaPhong);
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
		                mClientManager.SendFile(mMaPhong, data);
		            }
		        } catch (Exception exp) {
		            JOptionPane.showMessageDialog(fileJFrame, exp, "Error", JOptionPane.ERROR_MESSAGE);
		        }
			}
		});
        jScrollPane3 = new javax.swing.JScrollPane();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
            	closeWindow();
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
            	setTitle("ID: " + mMaPhong + " Name: " + mTenPhong + " So nguoi trong phong: " + mSoNguoi);
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

        jLabel1.setText("Thành viên room");
        
        btnSendGroup = new JButton("Gửi");
        btnSendGroup.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String mess = jTextField2.getText().trim();
                if(mess.length() ==0) {return;}
                mClientManager.SendMess(mMaPhong,mess);
                jTextField2.setText("");
        	}
        });
        jTextArea2 = new javax.swing.JTextArea();
        
                jTextArea2.setEditable(false);
                jTextArea2.setColumns(20);
                jTextArea2.setRows(5);
        
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
        				.addComponent(jLabel1)
        				.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
        					.addGroup(layout.createSequentialGroup()
        						.addComponent(btnSendGroup, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
        						.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        						.addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE))
        					.addGroup(layout.createSequentialGroup()
        						.addGroup(layout.createParallelGroup(Alignment.LEADING)
        							.addComponent(jTextArea2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        							.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE))
        						.addPreferredGap(ComponentPlacement.RELATED)
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
        					.addComponent(jLabel1)
        					.addGroup(layout.createParallelGroup(Alignment.LEADING)
        						.addGroup(layout.createSequentialGroup()
        							.addGap(29)
        							.addComponent(jScrollPane3, GroupLayout.PREFERRED_SIZE, 205, GroupLayout.PREFERRED_SIZE))
        						.addGroup(layout.createSequentialGroup()
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(jTextArea2, GroupLayout.PREFERRED_SIZE, 203, GroupLayout.PREFERRED_SIZE)
        							.addPreferredGap(ComponentPlacement.RELATED)
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea txtNoiDungChat;
    private javax.swing.JTextArea jTextArea2;
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
            JOptionPane.showMessageDialog(null, result.mContent, "Tháº¥t báº¡i", JOptionPane.ERROR_MESSAGE);
            return;
        }
        switch (result.mActionType)
        {
            case ActionType.SEND_MESSAGE:
            {
                String[] lines = result.mContent.split(";", -1);
                String sender = lines[0];
                String messContent = lines[1];
                //System.out.println(sender +" " + messContent +"\n");
                messContent = messContent.replaceAll("<br>", "\n");  //khi tin nháº¯n gá»­i Ä‘i Ä‘Ã£ thay kÃ½ tá»± xuá»‘ng dÃ²ng báº±ng <br> nÃªn khi nháº­n vá»� thÃ¬ thay ngÆ°á»£c láº¡i
                if(sender.equals(mClientManager.mNickname))
                    txtNoiDungChat.append("Me: " + messContent + "\n");
                else
                	txtNoiDungChat.append(sender + ": " + messContent + "\n");
                break;
            }
            case ActionType.GET_MESS:
        	{
        		if(result.mContent.length()>0)
        	    {
        	        String[] lines = result.mContent.split(";",-1);
        	        String[] rows = lines[1].split("<row>");
        	        if(mMaPhong == lines[0]) {
        	        for (int i = 0; i < rows.length; i++) //hÃ ng Ä‘áº§u lÃ  trá»‘ng
        	        {
        	            String[] cols = rows[i].split("<col>");
        	            
        	            String sender = cols[0];
        	            String messContent = cols[1];
        	            if(sender.equals(mClientManager.mNickname))
                            txtNoiDungChat.append("Me: " + messContent + "\n");
  
                        else
                        	txtNoiDungChat.append(sender + ": " + messContent + "\n");
        	            
        	        }
        	        }
        	    }
        		break;
        	}
            case ActionType.SEND_FILE:
            {
            	String[] lines = result.mContent.split(";", -1);
            	String sender = lines[0];
                String username = lines[1];
                
                if(username.equals(mClientManager.mNickname))
                    txtNoiDungChat.append("Me Send file: "+ result.file.getName() +"\n");
                else
                    txtNoiDungChat.append(sender + " send file " + result.file.getName() + "\n");
                //System.out.println(result.file.getName());
                mod.addElement(result.file);
                break;
            }
//            case ActionType.GET_ROOM_MEMBER:
//        	{
//        		if(result.mContent.length()>0)
//        	    {
//        	        //ds phÃ²ng cÃ³ dáº¡ng maphong<col>tenphong<col>songuoi<col><row>
//        	        //                 maphong<col>tenphong<col>songuoi<col><row>
//        	        String[] names = result.mContent.split(";");
//        	        for (int i = 0; i < names.length; i++) //hÃ ng Ä‘áº§u lÃ  trá»‘ng
//        	        {
//        	            jTextArea2.append(names[i] + "\n");
//        	            
//        	        }
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
//            case ActionType.NOTIFY_JUST_JOIN_ROOM:
//            {
//                String userJoin = result.mContent;
//                jTextArea1.append("<" + userJoin + "> vá»«a tham gia phÃ²ng\n" );
//                break;
//            }
        }
		
	}
}
