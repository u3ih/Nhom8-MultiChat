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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    private ObjectOutputStream mObjectOutputStream;
    private ObjectInputStream mObjectInputStream;
    private HashMap<String, Room> mRoom = new HashMap<String,Room>();
    private List<Room> listRoom = new ArrayList<Room>();
    private List<User> listUser = new ArrayList<User>();
    private Date mTimeConnect; //thá»�i gian káº¿t ná»‘i Ä‘áº¿n server

    public User() {
    }
    
    public void setUser(User user) {
    	this.id = user.id;
    	this.firstName = user.firstName;
        this.midName = user.midName;
        this.lastName = user.lastName;
        this.birthDay = user.birthDay;
        this.age = user.age;
        this.gender = user.gender;
        this.username = user.username;
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
    public User(String firstName, String midName, String lastName, String birthDay, int age, String gender, boolean isOnline) {
        this.firstName = firstName;
        this.midName = midName;
        this.lastName = lastName;
        this.birthDay = birthDay;
        this.age = age;
        this.gender = gender;
        this.isOnline = isOnline;
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
    public User(int id, String firstName, String midName, String lastName, String birthDay, int age, String gender,boolean isOnline) {
        this.id = id;
        this.firstName = firstName;
        this.midName = midName;
        this.lastName = lastName;
        this.birthDay = birthDay;
        this.age = age;
        this.gender = gender;
        this.isOnline = isOnline;
    }
    public User(String uname) {
    	this.username= uname;
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
	public String getGender() {
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

	public HashMap<String,Room> getmRoom() {
		return mRoom;
	}

	public void setmRoom(HashMap<String,Room> mRoom) {
		this.mRoom = mRoom;
	}
	
	public void addRoom(String idRoom,Room room) {
		mRoom.put(idRoom, room);
		listRoom.add(room);
	}
	
	public Room getRoom(String idRoom) {
		Room room = mRoom.get(idRoom);
		return room;
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
        mDataOutputStream = new DataOutputStream(mSocket.getOutputStream());  //user nháº­n báº±ng DataInputStream nÃªn gá»­i vá»� báº±ng DataOutputStream
        mObjectOutputStream = new ObjectOutputStream(mSocket.getOutputStream());
        mObjectInputStream = new ObjectInputStream(mSocket.getInputStream());
    }
    
    public String Read() throws IOException
    {
        if(mBufferReader.ready())
        {
            return mBufferReader.readLine();
        }
        return null;
    }
    
    public DataFile ReadFile() throws IOException
    {
    	try {
			return (DataFile) mObjectInputStream.readObject();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
        return null;
    }
    
    public boolean Ready() throws IOException
    {
        return mBufferReader.ready();
    }

    public Boolean Send(String actionType, String resultCode, String content)
    {
    	System.out.println(content);
        try 
        {
            mDataOutputStream.writeUTF(actionType + ";" + resultCode + ";" + content);
            return true;
        } catch (IOException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public Boolean SendFile(String actionType, String resultCode, String content, DataFile file)
    {
        try 
        {
            mDataOutputStream.writeUTF(actionType + ";" + resultCode + ";" + content);
            mObjectOutputStream.writeObject(file);
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

public List<Room> getListRoom() {
	return listRoom;
}

public void setListRoom(List<Room> listRoom) {
	this.listRoom = listRoom;
}
public List<User> getListUser() {
	return listUser;
}

public void setListUser(List<User> listUser) {
	this.listUser = listUser;
}

}
