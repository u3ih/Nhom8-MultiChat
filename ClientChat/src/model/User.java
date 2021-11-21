package model;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Date;
import java.util.logging.Logger;
import java.util.logging.Level;


public class User {
    protected int id;
    protected String firstName;
    protected String midName;
    protected String lastName;
    protected String birthDay;
    protected int age;
    protected  String gender;
    protected  boolean isOnline;
    protected String username;
    protected String password;
    
    private Socket mSocket;
    private BufferedReader mBufferReader;
    private DataOutputStream mDataOutputStream;
    private Room mRoom;
    private Date mTimeConnect; //thời gian kết nối đến server

    public User() {
    }
    
    public User(String lastName) {
    	this.lastName = lastName;
    }

    public User(String firstName, String midName, String lastName, String birthDay, int age, String gender, boolean isOnline, String username, String password) {
        this.firstName = firstName;
        this.midName = midName;
        this.lastName = lastName;
        this.birthDay = birthDay;
        this.age = age;
        this.gender = gender;
        this.isOnline = isOnline;
        this.username = username;
        this.password = password;
    }

    public User(int id, String firstName, String midName, String lastName, String birthDay, int age, String gender, boolean isOnline, String username, String password) {
        this.id = id;
        this.firstName = firstName;
        this.midName = midName;
        this.lastName = lastName;
        this.birthDay = birthDay;
        this.age = age;
        this.gender = gender;
        this.isOnline = isOnline;
        this.username = username;
        this.password = password;
    }

    public User(String firstName, String midName, String lastName, String birthDay, int age, String gender) {
        this.firstName = firstName;
        this.midName = midName;
        this.lastName = lastName;
        this.birthDay = birthDay;
        this.age = age;
        this.gender = gender;
    }

    public User(int id, String firstName, String midName, String lastName, String birthDay, int age, String gender) {
        this.id = id;
        this.firstName = firstName;
        this.midName = midName;
        this.lastName = lastName;
        this.birthDay = birthDay;
        this.age = age;
        this.gender = gender;
    }

	public User(Socket socket) throws IOException
    {
        mSocket = socket;
        mBufferReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream(), "UTF8"));
        mDataOutputStream = new DataOutputStream(mSocket.getOutputStream());  //user nhận bằng DataInputStream nên gửi về bằng DataOutputStream
    }
    
    public String Read() throws IOException
    {
        if(mBufferReader.ready())
        {
            return mBufferReader.readLine();
        }
        return null;
    }
    
    public boolean Ready() throws IOException
    {
        return mBufferReader.ready();
    }

    public Boolean Send(String actionType, String resultCode, String content)
    {
        try 
        {
            mDataOutputStream.writeUTF(actionType + ";" + resultCode + ";" + content);
            return true;
        } catch (IOException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
//    public Boolean IsOnline()
//    {
//        return Send(ActionType.CHECK_ONLINE, ResultCode.OK, "");
//    }
}
