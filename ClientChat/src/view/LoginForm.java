package view;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;

import controller.ClientManager;
import model.Result;
import model.ResultCode;
import model.ResultObj;
import model.User;
import model.ActionType;

import java.awt.event.ActionEvent;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;

/**
 *
 * @author ADMIN
 */
public class LoginForm extends javax.swing.JFrame implements Observer {

    /**
     * Creates new form loginForm
     */
	ClientManager mClientManager;
	
    public LoginForm() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        txtNickName = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
        loginBtn = new javax.swing.JButton();
        registerBtn = new javax.swing.JButton();
        registerBtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(51, 51, 255));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel3.setText("Password");

        loginBtn.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        loginBtn.setText("Đăng nhập");
        loginBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLoginPerformed(evt);
            }
        });

        registerBtn.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        registerBtn.setText("Đăng ký");
        registerBtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		btnRegisterActionPerformed(e);
        	}
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Chat App");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel2.setText("Username");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(Alignment.TRAILING, layout.createSequentialGroup()
        			.addContainerGap(271, Short.MAX_VALUE)
        			.addComponent(registerBtn, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
        			.addGap(78))
        		.addGroup(layout.createSequentialGroup()
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(layout.createSequentialGroup()
        					.addGap(181)
        					.addComponent(jLabel1))
        				.addGroup(layout.createSequentialGroup()
        					.addGap(10)
        					.addComponent(jSeparator1, GroupLayout.PREFERRED_SIZE, 416, GroupLayout.PREFERRED_SIZE))
        				.addGroup(layout.createSequentialGroup()
        					.addGap(27)
        					.addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
        					.addGap(4)
        					.addComponent(txtNickName, GroupLayout.PREFERRED_SIZE, 305, GroupLayout.PREFERRED_SIZE))
        				.addGroup(layout.createSequentialGroup()
        					.addGap(27)
        					.addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
        					.addGap(4)
        					.addGroup(layout.createParallelGroup(Alignment.LEADING)
        						.addComponent(loginBtn)
        						.addComponent(txtPassword, GroupLayout.PREFERRED_SIZE, 305, GroupLayout.PREFERRED_SIZE))))
        			.addContainerGap(23, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addGap(23)
        			.addComponent(jLabel1)
        			.addGap(6)
        			.addComponent(jSeparator1, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
        			.addGap(27)
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
        				.addGroup(layout.createSequentialGroup()
        					.addGap(1)
        					.addComponent(txtNickName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
        			.addGap(34)
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(layout.createSequentialGroup()
        					.addGap(3)
        					.addComponent(jLabel3))
        				.addComponent(txtPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addGap(38)
        			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(registerBtn, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
        				.addComponent(loginBtn))
        			.addContainerGap(24, Short.MAX_VALUE))
        );
        getContentPane().setLayout(layout);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    @Override
    public void update(Observable o, Object arg) {
    	loginBtn.setEnabled(true);
    	Result result = (Result)arg;
        if(result.mResultCode.equals(ResultCode.ERROR))
        {
            JOptionPane.showMessageDialog(null, result.mContent, "Thất bại", JOptionPane.ERROR_MESSAGE);
        }else if(result.mActionType.equals(ActionType.LOGIN))
        {
            mClientManager.deleteObserver(this);   //xóa obs login đi để tránh login nhận thông báo trong khi ko hoạt động;
            ListForm listForm = new ListForm(mClientManager, this);
            listForm.setVisible(true);
            this.setVisible(false);
        }
    }
    private void jButtonLoginPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    	String nickName = txtNickName.getText().trim();
    	String pass = String.valueOf(txtPassword.getPassword());
        if(nickName.length()==0 || pass.length() == 0)
        {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập đủ thông tin", "Chưa nhập thông tin", JOptionPane.WARNING_MESSAGE);
            txtNickName.requestFocus();
            txtPassword.requestFocus();
            return;
        }
        
        
    	if(mClientManager!=null)
        {
            mClientManager.Dispose();
        }
        mClientManager = new ClientManager(this);
        if(mClientManager.StartConnect(nickName, pass))
        {
            try 
            {
            	loginBtn.setEnabled(false);
                mClientManager.Login(nickName);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(LoginForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */

    private void btnRegisterActionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
    	RegisterForm mRegis=new RegisterForm();
    	mRegis.setVisible(true);
    	mRegis.pack();
    	mRegis.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	mRegis.setLocationRelativeTo(null);
        this.dispose();
	}

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LoginForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LoginForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LoginForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton loginBtn;
    private javax.swing.JButton registerBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField txtNickName;
    // End of variables declaration//GEN-END:variables
}
