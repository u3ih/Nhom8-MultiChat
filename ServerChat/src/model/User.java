/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DELL
 */
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
    private DataInputStream mDataInputStream;
    private Room mRoom;
    private Date mTimeConnect; //thời gian kết nối đến server

    public User() {
    }
    
    public void setUser(User user) {
    	this.firstName = user.firstName;
        this.midName = user.midName;
        this.lastName = user.lastName;
        this.birthDay = user.birthDay;
        this.age = user.age;
        this.gender = user.gender;
        this.isOnline = user.isOnline;
        this.username = user.username;
        this.password = user.password;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMidName() {
		return midName;
	}

	public void setMidName(String midName) {
		this.midName = midName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String isGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Socket getmSocket() {
		return mSocket;
	}

	public void setmSocket(Socket mSocket) {
		this.mSocket = mSocket;
	}

	public BufferedReader getmBufferReader() {
		return mBufferReader;
	}

	public void setmBufferReader(BufferedReader mBufferReader) {
		this.mBufferReader = mBufferReader;
	}

	public DataOutputStream getmDataOutputStream() {
		return mDataOutputStream;
	}

	public void setmDataOutputStream(DataOutputStream mDataOutputStream) {
		this.mDataOutputStream = mDataOutputStream;
	}

	public DataInputStream getmDataInputStream() {
		return mDataInputStream;
	}

	public void setmDataInputStream(DataInputStream mDataInputStream) {
		this.mDataInputStream = mDataInputStream;
	}

	public Room getmRoom() {
		return mRoom;
	}

	public void setmRoom(Room mRoom) {
		this.mRoom = mRoom;
	}

	public Date getmTimeConnect() {
		return mTimeConnect;
	}

	public void setmTimeConnect(Date mTimeConnect) {
		this.mTimeConnect = mTimeConnect;
	}

	public User(Socket socket) throws IOException
    {
        mSocket = socket;
        mBufferReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream(), "UTF8"));
        mDataInputStream = new DataInputStream(mSocket.getInputStream());
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
    
    public Boolean IsOnline()
    {
        return Send(ActionType.CHECK_ONLINE, ResultCode.OK, "");
    }

	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", midName=" + midName + ", lastName=" + lastName
				+ ", birthDay=" + birthDay + ", age=" + age + ", gender=" + gender + ", isOnline=" + isOnline
				+ ", username=" + username + ", password=" + password + ", mTimeConnect=" + mTimeConnect + "]";
	}
   public String getFullName() {
	   return this.firstName +" "+ this.midName + " " + this.lastName; 
   }
}
