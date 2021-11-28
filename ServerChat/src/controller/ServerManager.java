package controller;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import dao.room.RoomDAO;
import dao.user.UserDAO;
import model.ActionType;
import model.DataFile;
import model.MessageRoom;
import model.MessageRoom;
import model.ResultCode;
import model.Room;
import model.User;

public class ServerManager extends Observable
{
    int mPort = 1106;
    ServerSocket mServerSocket;
    Thread mThreadAccept, mThreadProcess;
    //ArrayList<User> mListUser = new ArrayList<>();
    ArrayList<User> mListFriend = new ArrayList<>();
    ArrayList<User> mListUserOnline = new ArrayList<>();
    ArrayList<Room> mListRoom = new ArrayList<>();
    ArrayList<User> mListUserWaitLogout = new ArrayList<>();
    UserDAO controlUser = new UserDAO();
    HashMap<String,User> mListUser = controlUser.selectAllUsers();
    
    DataInputStream mDataInputStream;
    DataOutputStream mDataOutputStream;
    RoomDAO roomDAO = new RoomDAO();
    
    public ServerManager(Observer obs)  //hàm khởi tạo khi chưa có socket
    {
        this.addObserver(obs);
    }
    public ServerManager(ServerSocket serverSocket, Observer obs)   //hàm khởi tạo khi đã có socket
    {
        this.addObserver(obs);
        mServerSocket = serverSocket;
    }
    
    public void Dispose() throws IOException
    {
        if(mThreadAccept!=null)
        {
            mThreadAccept.stop();
            mThreadProcess.stop();
            mServerSocket.close(); 
        }
    }
    
    public boolean StartServer() //khởi động server
            
    {
        try 
        {
            mServerSocket = new ServerSocket(mPort);
            StartThreadAccept();
            StartThreadProcess();
            notifyObservers("Khởi động server thành công");
            return true;
        } catch (IOException ex) 
        {
            notifyObservers("Không thể khởi động server");
            return false;
        }
    }
    void StartThreadAccept()   //bắt đầu luồng chấp nhận các socket kết nối đến
    {
        mThreadAccept = new Thread(new Runnable() 
        {
            @Override
            public void run() 
            {
                try 
                {
                    while(true)
                    {
                        Socket socket = mServerSocket.accept();
                        User newUser = new User(socket);
                        mDataInputStream = new DataInputStream(socket.getInputStream());
                        mDataOutputStream = new DataOutputStream(socket.getOutputStream());
                        String req = mDataInputStream.readUTF();
                        if(req.equals(ActionType.REGISTER) ) {
                        	try {
            					String Fname=mDataInputStream.readUTF();
            					String Mname=mDataInputStream.readUTF();
            					String Lname=mDataInputStream.readUTF();
            					String Birday=mDataInputStream.readUTF();
            					int Age=mDataInputStream.readInt();
            					String Uname=mDataInputStream.readUTF();
            					String Pass=mDataInputStream.readUTF();
            					String gender=mDataInputStream.readUTF();
            					User uss= new User(Fname,Mname,Lname,Birday,Age,gender,true,Uname,Pass);
            					//System.out.println(uss.toString());
            					UserDAO ud = new UserDAO();
            					ud.insertUser(uss);;
            					mDataOutputStream.writeUTF("OK");
            					mListUser = controlUser.selectAllUsers();
            				} catch (IOException | SQLException e) {
            					mDataOutputStream.writeUTF("ERROR");
            				} 
                        	continue;
                        }
                        String nickName = req;
                        String pass = mDataInputStream.readUTF();
                        
                        if(mListUser.containsKey(nickName)) {
                        	if(mListUser.get(nickName).getPassword().equals(pass)) {
                        		newUser.setUser(mListUser.get(nickName));
                        		newUser.setmTimeConnect(new Date());
                        		
                        		mDataOutputStream.writeUTF("OK");
                        	} else {
                            	mDataOutputStream.writeUTF("ERROR");
                            	socket.close();
                            	continue;
                            }
                        } else {
                        	mDataOutputStream.writeUTF("ERROR");
                        	socket.close();
                        	continue;
                        }
                        
                        mListUserOnline.add(newUser);
                        //System.out.println(mListUserOnline);
                    }
                }catch (IOException ex) {
                    notifyObservers("Lỗi kết nối");
                }
            }
        });
        mThreadAccept.start();
    }
    
    void StartThreadProcess()  //bắt đầu luồng chính xử lý các request, kiểm soát user
    {
        mThreadProcess = new Thread(new Runnable() 
        {
            @Override
            public void run() 
            {
                try 
                {
                    while(true)
                    {
                        CheckRequest();
                        CheckTimeConnect();
                        
                        if(mListUserWaitLogout.size()>0)  //remove các user trong hàng đợi
                            RemoveUserLoggedOut();
                        
                        Thread.sleep(0);  //cần thiết để server có thể nhận được bufferReader
                    }
                    
                }catch (IOException ex) {
                    notifyObservers("Lỗi kết nối");
                } catch (InterruptedException ex) {
                    Logger.getLogger(ServerManager.class.getName()).log(Level.SEVERE, null, ex);
                } 
            }
        });
        mThreadProcess.start();
    }
    
    @Override
    public void notifyObservers(Object arg) {
        super.setChanged();
        super.notifyObservers(arg);
    }
    
    void CheckRequest() throws IOException
    {
    	int size = mListUserOnline.size();
        for(int i=0; i<size; i++)
        {
            User user = mListUserOnline.get(i);
            //server cần nhận thông tin liên tục của nhiều người, nên ko thể sử dụng DataInputStream để đợi nhận yêu cầu của một người
            //mà cần chạy liên tục để nhận yêu cầu của bất cứ ai ngay lập tức
            String request = user.Read();
            DataFile file = new DataFile();
            if(request!=null) {
            	String[] lines = request.split(";",-1);
                String actionType = lines[0];
                String roomID = lines[1];
                System.out.println(actionType + " " + roomID);
                if(actionType.equals(ActionType.SEND_FILE)) {
                	file = user.ReadFile();
                	System.out.println("file: " + file.getName());
                	ProcessSendFile(user, roomID, file);
                } else if (actionType.equals(ActionType.SEND_FILE_FRIEND)) {
                	file = user.ReadFile();
                	System.out.println("file: " + file.getName());
                	ProcessSendFileFriend(user, roomID, file);
                }	else if(actionType.equals(ActionType.CALL_ONLINE)) {
                	String uname=lines[1];
                	for(int j=0;j<size;j++) {
                		if(j!=i) {
                			User usersk=mListUserOnline.get(j);
                			int uid=controlUser.selectIDbyuname(uname);
                			User user1 = controlUser.selectAllInfoAUserByID(uid);
                			user1.setOnline(false);
        					String res=user1.getFirstName()+";"+user1.getMidName()+";"+user1.getLastName()+";"+user1.getBirthDay()+";"+Integer.toString(user1.getAge())+";"+user1.getGender()+";"+user1.isOnline()+";"+user1.getId();
                    		usersk.Send(actionType, ResultCode.OK, res);
                    		notifyObservers(res);
                		}
                	}
                }
                else if(actionType.equals(ActionType.CALL_OFFLINE)) {
                	String uname=lines[1];
                	for(int j=0;j<size;j++) {
                		if(j!=i) {
                			User usersk=mListUserOnline.get(j);
                			int uid=controlUser.selectIDbyuname(uname);
                			User user1 = controlUser.selectAllInfoAUserByID(uid);
                			user1.setOnline(true);
        					String res=user1.getFirstName()+";"+user1.getMidName()+";"+user1.getLastName()+";"+user1.getBirthDay()+";"+Integer.toString(user1.getAge())+";"+user1.getGender()+";"+user1.isOnline()+";"+user1.getId();
                    		usersk.Send(actionType, ResultCode.OK, res);
                    		notifyObservers(res);
                		}
                	}
                }
                else 
                {
                	ProcessRequest(user, request);
                }
            }
                
        }
    }
    
    void CheckTimeConnect()
    {
        Date now = new Date();
        int size = mListUserOnline.size();
        long second = 0;
        for(int i=0; i<size; i++)
        {
            User user = mListUserOnline.get(i);
            if(user.isOnline() == false)
            {
                second = (now.getTime() - user.getmTimeConnect().getTime()) / 1000;
                if(second>10) //thường thì kết nối đến tương ứng vs đăng nhập, nhưng nếu ko đăng nhập sau 10s thì xóa
                {
                    mListUserWaitLogout.add(user);
                }
            }
        }
    }
    
    void RemoveUserLoggedOut()
    {
        int size = mListUserWaitLogout.size();
        for(int i=0; i<size; i++)
        {
            User user = mListUserWaitLogout.get(i);
            mListUserOnline.remove(user);
        }
        mListUserWaitLogout.clear();
    }
    
    void ProcessSendFile(User user, String roomID, DataFile file) {     
        //notifyObservers(user.getLastName() + " vừa gửi file");
        user.getRoom(roomID).SendFileToAllUser(user.getLastName(), file, user.getUsername());
    }
    void ProcessSendFileFriend(User user, String friendUsername, DataFile file) {     
        //notifyObservers(user.getLastName() + " vừa gửi file");
    	User userFriend = new User();
    	boolean check = false;
    	for(User u: mListUserOnline) {
    		if(u.getUsername().equals(friendUsername)) {
    			userFriend = u;
    			check = true;
    		}
    	}
        user.SendFile(ActionType.SEND_FILE, ResultCode.OK, + userFriend.getId() + ";" +user.getUsername() +";", file);
        if(check) userFriend.SendFile(ActionType.SEND_FILE, ResultCode.OK, userFriend.getId() + ";" +user.getUsername() +";", file);
    }
    
    void ProcessRequest(User user, String request)
    {
        String[] lines = request.split(";",-1);
        String actionType = lines[0];
        switch (actionType)
        {
            case ActionType.LOGIN:
            {
                    user.setOnline(true);
                    user.Send(actionType, ResultCode.OK, "OK");
                    try {
                    	controlUser.updateOnline(user);
                    }catch (Exception e) {
						// TODO: handle exception
					}
                    
                    notifyObservers(user.getUsername() + " vừa đăng nhập thành công");
                
                break;
            }
            case ActionType.CREATE_ROOM:
            {
            	String roomName = lines[1];  //query cÄ‚Â³ dÃ¡ÂºÂ¡ng actionType;roomName
                Room room = GeneralRoom(roomName);
                mListRoom.add(room);
                user.addRoom(room.getIdRoom(), room);
                if(user.Send(actionType, ResultCode.OK, room.getIdRoom()))
                    room.AddUser(user);
                roomDAO.createRoom(room.getIdRoom(),1, room.getNameRoom());
                roomDAO.addRoomConnection(room.getIdRoom(), user.getId());
                notifyObservers(user.getUsername() + " vừa tạo phòng " + roomName);
                break;
            }
            case ActionType.GET_LIST_ROOM:
            {
                List<Room> list = roomDAO.getRoomByUserID(user.getId());
                
                int size = list.size();
                //System.out.println(size);
                if(size>0)
                {
//                	for(Room room:list) {
//                        System.out.println(room.toString());    
//                       }
                	String listRoom = "";
                	for(Room room:list) {
                     listRoom += room.getIdRoom() + "<col>" + room.getNameRoom() + "<col>" 
                            + room.getCountPeople() + "<col>" + room.getLastMess() + "<col>" + "<row>";    
                    }
                    //System.out.print(listRoom);
                    user.Send(actionType, ResultCode.OK, listRoom);
                }else
                {
                    user.Send(actionType, ResultCode.OK, "");
                }
                notifyObservers(user.getUsername() + " vừa lấy danh sách phòng");
                break;
            }
            case ActionType.GET_MESS:
            {
            	
                List<MessageRoom> list = roomDAO.getMessByRoomID(lines[1]);
                for(MessageRoom mess:list) {
                	System.out.println(mess.getSender() + " " + mess.getMess());
                }
                int size = list.size();
                if(size>0)
                {
                    
                    String listMess = "";
                    for (int i = 0; i < size; i++) 
                    {
                        
                            MessageRoom mess = list.get(i);
                            listMess+= mess.getSender() + "<col>" + mess.getMess() + "<col>" + "<row>";
                        
                        
                    }
                    user.Send(actionType, ResultCode.OK,lines[1] + ";" + listMess);
                    
                    
                }else
                {
                    user.Send(actionType, ResultCode.OK, "");
                }
                notifyObservers(user.getLastName() + " Vừa lấy danh sách tin nhắn ");
                break;
            }
            case ActionType.GET_ROOM_MEMBER:
            {
            	
                List<String> list = roomDAO.getRoomMember(lines[1]);//query cÃ³ dáº¡ng actionType;
                //System.out.println(list.size() + "\n");
                int size = list.size();
                if(size>0)
                {
                    String listMess = "";
                    for (int i = 0; i < size; i++) 
                    {
                        String name = list.get(i);
                        listMess+= name + ";";
                        
                    }
                    user.Send(actionType, ResultCode.OK,lines[1] + ";" + listMess);
                    
                    
                }else
                {
                    user.Send(actionType, ResultCode.OK, "");
                }
                notifyObservers(user.getLastName() + " vừa lấy danh sách thành viên");
                break;
            }
            case ActionType.GET_ROOM_BY_ID:
            {
            	
                Room room = roomDAO.getRoomInfo(lines[1]);//query cÃ³ dáº¡ng actionType;
                //System.out.println(list.size() + "\n");
                if(room != null)
                {
                	String listRoom = room.getIdRoom() + "<col>" + room.getNameRoom() + "<col>" 
                            + room.getCountPeople() + "<col>" + room.getLastMess() + "<col>" + "<row>";    

                    user.Send(actionType, ResultCode.OK, listRoom);
                }else
                {
                    user.Send(actionType, ResultCode.OK, "");
                }
                notifyObservers(user.getLastName() + " vừa lấy danh sách thành viên");
                break;
            }
            case ActionType.JOIN_ROOM:
            {
                String maPhong = lines[1];   //query cÃ³ dáº¡ng actionType;maPhong
                int size = mListRoom.size();
                boolean success = false;
                for (int i = 0; i < size; i++) 
                {
                    Room room = mListRoom.get(i);
                    if(room.getIdRoom().equals(maPhong))
                    {
                    	if(user.getmRoom().containsKey(room.getIdRoom())) {
                    		user.Send(actionType, ResultCode.ERROR, "Bạn đã ở trong phòng");
                    		return;
                    	}
                    	else {
                    	roomDAO.addRoomConnection(room.getIdRoom(), user.getId());
                        room.AddUser(user);
                        roomDAO.UpdateMemberCount(room.getIdRoom(), room.getCountPeople());
                        user.addRoom(room.getIdRoom(),room);
                        String count = room.getmListUser().size()+"";
                        user.Send(actionType, ResultCode.OK, room.getNameRoom() + ";" + count);
                        notifyObservers(user.getUsername() + " vá»«a tham gia phÃ²ng " + room.getIdRoom());
                        //user.getRoom(room.getIdRoom()).UpdateNumberUser();
                        user.getRoom(room.getIdRoom()).NotifyJustJoinRoom(user);
                        success = true;
                    	}
                    }
                }
                if(success==false)
                {
                    user.Send(actionType, ResultCode.ERROR, "Không tìm thấy phòng");
                    notifyObservers(user.getUsername() + " không thể tham gia phòng " + maPhong);
                }
                
                break;
            }
            case ActionType.SEND_MESSAGE:
            {
            	
                String contentMess = "";
                if(lines.length>=2)
                    contentMess = lines[2];   //query cÃ³ dáº¡ng actionType;contentMess
                user.getRoom(lines[1]).SendToAllUser(lines[1]+";"+user.getUsername(), contentMess);
                roomDAO.InsertMess(user.getRoom(lines[1]).getIdRoom(), user.getId(), contentMess);
                roomDAO.UpdateLastMess(user.getRoom(lines[1]).getIdRoom(),user.getUsername()+": "+ contentMess);
                notifyObservers(user.getUsername() + " vừa gửi tin");
                break;
            }
            
            case ActionType.SEND_MESSAGE_FRIEND:
            {
            	User userFriend = new User();
            	boolean check = false;
            	for(User u: mListUserOnline) {
            		if(u.getUsername().equals(lines[1])) {
            			userFriend = u;
            			check = true;
            		}
            	}
                String contentMess = "";
                if(lines.length>=2)
                    contentMess = lines[2];   //query cÃ³ dáº¡ng actionType;contentMess
                user.Send(ActionType.SEND_MESSAGE, ResultCode.OK, + userFriend.getId() + ";" +user.getUsername() +";"+ contentMess);
                if(check) userFriend.Send(ActionType.SEND_MESSAGE, ResultCode.OK, userFriend.getId() + ";" +user.getUsername() +";"+  contentMess);
                //roomDAO.InsertMess(user.getRoom(lines[1]).getIdRoom(), user.getId(), contentMess);
                //roomDAO.UpdateLastMess(user.getRoom(lines[1]).getIdRoom(),user.getUsername()+": "+ contentMess);
                //notifyObservers(user.getUsername() + " vừa gửi tin");
                break;
            }
            
            case ActionType.LEAVE_ROOM:    //query có dạng actionType;
            {
                Room room = user.getRoom("gf");
                room.RemoveUser(user);
                if(room.CountUser()>0)
                {
                    room.NotifyJustLeaveRoom(user);
                    room.UpdateNumberUser();
                }
                else
                    mListRoom.remove(room);
                user.setmRoom(null);
                notifyObservers(user.getUsername() + " vừa rời phòng");
                break;
            }
            case ActionType.LOGOUT:    //query có dạng actionType;
            {
                Room room = user.getRoom("gfd");
                user.setOnline(false);
                try {
                	controlUser.updateOnline(user);
                }catch (Exception e) {
					// TODO: handle exception
				}
                if(room!=null)
                {
                    room.RemoveUser(user);
                    if(room.CountUser()>0)
                    {
                        room.NotifyJustLeaveRoom(user);
                        room.UpdateNumberUser();
                    }
                    else
                        mListRoom.remove(room);
                }
                //vì mListUser luôn được sử dụng bởi thread đợi kết quả, nên can thiệp vào giữa chừng rất dễ gây lỗi
                //Nên ta sử dụng một list hàng đợi, thread đợi kết quả sẽ xóa các user này khi duyệt đợi kết quả xong
                mListUserWaitLogout.add(user); 
                notifyObservers(user.getUsername() + " vừa đăng xuất");
                break;

            }
            case ActionType.Get_User_Info:
            {
            	String uname = lines[1];
            	User u= new User();
            	u=controlUser.selectUserbyuname(uname);
            	String name=u.getFullName();
            	String birthday=u.getBirthDay();
            	String age =Integer.toString(u.getAge());
            	String gender=u.isGender();
            	String res=name+"+"+birthday+"+"+age+"+"+gender;
            	user.Send(actionType, ResultCode.OK, res);
            	notifyObservers(res + " vừa lay thong tin ");
            	break;
            }
            case ActionType.Set_User_Info:
            {
            	String infos = lines[1];
            	String info[] = infos.split("\\+");
            	String name[]=info[1].split("\\s",3);
            	User u= new User();
            	u.setUsername(info[0]);
            	u.setFirstName(name[0]);
            	u.setMidName(name[1]);
            	u.setLastName(name[2]);
            	u.setBirthDay(info[2]);
            	u.setAge(Integer.parseInt(info[3]));
            	u.setGender(info[4]);
            	boolean check = false;
				try {
					check = controlUser.updateUser(u);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	if(check) {
            		user.Send(actionType, ResultCode.OK, "Cập nhât thành công");
            	}
            	else	user.Send(actionType, ResultCode.ERROR, "Cập nhật thất bại");
            	notifyObservers(u.getLastName()+" thay đổi thông tin ");
            	break;
            }
            case ActionType.Get_User_Info_byName:
            {
            	String name = lines[1];
            	String uname= lines[2];
            	ArrayList<User> users=controlUser.selectUsersbyName(name,uname);
            	String res = "";
            	for(int i=0;i<users.size();i++) {
            		String id =Integer.toString(users.get(i).getId());
            		String fullname = users.get(i).getFullName();
            		String birthday = users.get(i).getBirthDay();
            		String age = Integer.toString(users.get(i).getAge());
            		String gender = users.get(i).isGender();
            		res+=id+"<cols>"+fullname+"<cols>"+birthday+"<cols>"+age+"<cols>"+gender+"<row>";	
            	}
            	user.Send(actionType, ResultCode.OK, res);
            	break;
            }
            case ActionType.Ket_Ban:
            { 
            	String id = lines[1];
            	int a= Integer.parseInt(id);
            	String uname = lines[2];
            	int uid=controlUser.selectIDbyuname(uname);
            	ArrayList<User> listFriendID=controlUser.selectFriendbyId(uid);
            	boolean test = true;
            	for(int i=0;i<listFriendID.size();i++) {
            		if(a == listFriendID.get(i).getId())	test=false;
            	}
            	if(test == false) {
            		String s="error";
            		user.Send(actionType, ResultCode.OK, s);
            	}
            	else {
            		boolean check = false;
				try {
					check=controlUser.insertUserconnection(a,uname);
					
				} catch (SQLException e) {
					 //TODO Auto-generated 
					e.printStackTrace();
				}
				if(check) {
					User user1 = controlUser.selectAllInfoAUserByID(a);
					String res=user1.getFirstName()+";"+user1.getMidName()+";"+user1.getLastName()+";"+user1.getBirthDay()+";"+Integer.toString(user1.getAge())+";"+user1.getGender()+";"+user1.isOnline()+";"+user1.getId() +";"+user1.getUsername() +";"+user1.getPassword();
            		user.Send(actionType, ResultCode.OK, res);
            		mListFriend.add(user1);
            	}
            	else	user.Send(actionType, ResultCode.ERROR, "thất bại");
            	}
            	
            	notifyObservers(uname+" ket ban ");
            	break;
            }
            case ActionType.GET_LIST_FRIEND:
            {
            	//String id = lines[1];
            	String uname =lines[1];
            	int id = controlUser.selectIDbyuname(uname);
//            	//int a= Integer.parseInt(id);
            	//List<User> list = user.getId();//query có dạng actionType;
            	List<User> list = controlUser.selectFriendbyId(id);
//               
            	//List<Room> list = roomDAO.getRoomByUserID(user.getId());//query cÃ³ dáº¡ng actionType;
                int size = list.size();       
                if(size>0)
                {
//                	for(User u:list) {
//                        System.out.println(u.toString());    
//                       }
                	String listFriend = "";
                	for(User u:list) {
                     listFriend += u.getFirstName() +"<col>" +u.getMidName()+"<col>" +u.getLastName()+"<col>" 
                	+u.getBirthDay()+"<col>" +u.getAge()+"<col>" +u.getGender()+"<col>"+u.isOnline()+"<col>"+u.getId()+"<col>"+u.getUsername()+"<col>"+u.getPassword()+"<row>";    
                    }
                    //System.out.print(listFriend);
                    user.Send(actionType, ResultCode.OK, listFriend);
                }else
                {
                    user.Send(actionType, ResultCode.OK, "");
                }
                notifyObservers(user.getUsername() + " vừa lấy danh sách friend");
                break;
            }
        }
    }
    
    
    //Tạo phòng mới
    Room GeneralRoom(String roomName)
    {
        Room room = new Room();
        room.setNameRoom(roomName);
        room.setIdRoom(GeneralMaPhong());
        return room;
    }
    
    int maxChar = 3;
    //Tạo ngẫu nhiên một mã phòng
    String GeneralMaPhong()
    {
        int countRandom = 0;
        //int maxChar = 3;
        String maPhong = "";
        do
        {
            if(countRandom>50) //nếu đã ramdom 50 lần mà vẫn bị trùng thì tăng max char lên để tránh tốn thời gian random
                maxChar++;
            
            maPhong = RandomString(maxChar);
            countRandom++;  //tăn)g số lần đềm random
        }while(CheckMaPhong(maPhong)==false);
        return maPhong;
        
    }
    
    //Kiểm tra mã phòng đã tồn tại hay chưa, true là chưa, false là rồi
    boolean CheckMaPhong(String maPhong)
    {
        int size = mListRoom.size();
        for (int i = 0; i < size; i++) 
        {
            Room room = mListRoom.get(i);
            if(room.getIdRoom().equals(maPhong))
                return false;
        }
        return true;
    }
    
    //Random ngẫu nhiên một chuỗi
    String RandomString(int length)
    {
        String data = "1234567890qwertyuiopasdfghjklzxcvbnm";
        int sizeData = data.length();
        String result = "";
        Random rd = new Random();
        for (int i = 0; i < length; i++) 
        {
            result += data.charAt(rd.nextInt(sizeData));
        }
        return result;
    }
    
    public int CountUser()
    {
        return mListUserOnline.size();
    }
    public int CountRoom()
    {
        return mListRoom.size();
    }
}
