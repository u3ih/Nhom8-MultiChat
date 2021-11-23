/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private String idRoom;
    private String nameRoom;
    private int countPeople;
    private List<MessageRoom> listMess = new ArrayList<>();
    
    private ArrayList<User> mListUser = new ArrayList<>();
    
    private String lastMess;
    
    public Room(String idRoom, String nameRoom, int countPeople, String lastMess) {
    	this.idRoom = idRoom;
    	this.nameRoom = nameRoom;
    	this.countPeople = countPeople;
    	this.lastMess = lastMess;
    }
    
    public Room(String idRoom, String nameRoom, int countPeople, ArrayList<User> mlistUser) {
    	this.idRoom = idRoom;
    	this.nameRoom = nameRoom;
    	this.countPeople = countPeople;
    	this.mListUser = mlistUser;
    }
    
    public Room(String idRoom, String nameRoom, int countPeople) {
    	this.nameRoom = nameRoom;
    	this.countPeople = countPeople;
    	this.idRoom = idRoom;
    }


	public String getIdRoom() {
		return idRoom;
	}

	public void setIdRoom(String idRoom) {
		this.idRoom = idRoom;
	}

	public String getNameRoom() {
		return nameRoom;
	}

	public void setNameRoom(String nameRoom) {
		this.nameRoom = nameRoom;
	}

	public int getCountPeople() {
		return countPeople;
	}

	public void setCountPeople(int countPeople) {
		this.countPeople = countPeople;
	}

	public ArrayList<User> getmListUser() {
		return mListUser;
	}

	public void setmListUser(ArrayList<User> mListUser) {
		this.mListUser = mListUser;
	}

	public void AddUser(User user)
    {
        mListUser.add(user);
    }
    
    public void RemoveUser(User user)
    {
        mListUser.remove(user);
    }
    
    public int CountUser()
    {
        return mListUser.size();
    }

	public String getLastMess() {
		return lastMess;
	}

	public void setLastMess(String lastMess) {
		this.lastMess = lastMess;
	}
    
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return idRoom + " " + countPeople + " " + nameRoom + " " + lastMess;
	}
//    public void SendToAllUser(String sender, String content)
//    {
//        int size = mListUser.size();
//        for (int i = 0; i < size; i++) 
//        {
//            User user = mListUser.get(i);
//            if(user.Send(ActionType.SEND_MESSAGE, ResultCode.OK, sender + ";" + content)==false)
//            {
//                NotifyJustLeaveRoom(user);
//            }
//        }
//    }
//    
//    public void UpdateNumberUser()
//    {
//        int size = mListUser.size();
//        for (int i = 0; i < size; i++) 
//        {
//            User user = mListUser.get(i);
//            if(user.Send(ActionType.UPDATE_NUMBER_USER, ResultCode.OK, size + "")==false)
//            {
//                NotifyJustLeaveRoom(user);
//            }
//        }
//    }
//    
//    public void NotifyJustJoinRoom(User userJoin)
//    {
//        int size = mListUser.size();
//        for (int i = 0; i < size; i++) 
//        {
//            User user = mListUser.get(i);
//            if(user!=userJoin)
//            {
//                user.Send(ActionType.NOTIFY_JUST_JOIN_ROOM, ResultCode.OK, userJoin.getLastName());
//            }
//        }
//    }
//    public void NotifyJustLeaveRoom(User userLeave)
//    {
//        int size = mListUser.size();
//        for (int i = 0; i < size; i++) 
//        {
//            User user = mListUser.get(i);
//            if(user!=userLeave)
//            {
//                user.Send(ActionType.NOTIFY_JUST_LEAVE_ROOM, ResultCode.OK, userLeave.getLastName());
//            }
//        }
//    }
}

