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
import model.Message;
import model.ResultCode;
import model.Room;
import model.User;

public class ServerManager extends Observable
{
    int mPort = 1106;
    ServerSocket mServerSocket;
    Thread mThreadAccept, mThreadProcess;
    //ArrayList<User> mListUser = new ArrayList<>();
    ArrayList<User> mListUserOnline = new ArrayList<>();
    ArrayList<Room> mListRoom = new ArrayList<>();
    ArrayList<User> mListUserWaitLogout = new ArrayList<>();
    UserDAO controlUser = new UserDAO();
    HashMap<String,User> mListUser = controlUser.selectAllUsers();
    
    DataInputStream mDataInputStream;
    DataOutputStream mDataOutputStream;
    RoomDAO dao = new RoomDAO();
    
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
            					ud.insertUser(uss);
            					mListUser.put(uss.getUsername(), uss);
            					mDataOutputStream.writeUTF("OK");
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
                        System.out.println(mListUserOnline);
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
                } else {
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
        notifyObservers(user.getLastName() + " vừa gửi file");
        user.getRoom(roomID).SendFileToAllUser(user.getLastName(), file, user.getUsername());
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
                    notifyObservers(user.getUsername() + " vừa đăng nhập thành công");
                
                break;
            }
            case ActionType.CREATE_ROOM:
            {
                String roomName = lines[1];  //query có dạng actionType;roomName
                Room room = GeneralRoom(roomName);
                mListRoom.add(room);
                user.addRoom(room.getIdRoom(), room);
                if(user.Send(actionType, ResultCode.OK, room.getIdRoom()))
                    room.AddUser(user);
                notifyObservers(user.getUsername() + " vừa tạo phòng " + roomName);
                break;
            }
            case ActionType.GET_LIST_ROOM:
            {
                List<Room> list = user.getListRoom();//query có dạng actionType;
                int size = list.size();
                int rowsPerBlock = 500;    //số phòng gửi về mỗi block
                if(size>0)
                {
                    String listRoom = "";
                    int start=0;
                    int end=0;
                    int numberBlock = (int)Math.floor(size/(double)rowsPerBlock);
                    for (int i = 0; i < numberBlock; i++) 
                    {
                        start = i*rowsPerBlock;
                        end = start + rowsPerBlock;
                        listRoom = "";
                        for (int j = start; j < end; j++) 
                        {
                            Room room = list.get(j);
                            listRoom+= room.getIdRoom() + "<col>" + room.getNameRoom() + "<col>" + room.CountUser() + "<col>" + "<row>";
                        }
                        System.out.print("Gửi lần thứ: " + i);
                        user.Send(actionType, ResultCode.OK, listRoom);
                    }
                    
                    listRoom = "";
                    for (int i = end; i < size; i++) //gửi nốt những phòng còn lại
                    {
                        Room room = list.get(i);
                        listRoom+= room.getIdRoom() + "<col>" + room.getNameRoom() + "<col>" + room.CountUser() + "<col>" + "<row>";
                    }
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
            	
                Room room = user.getRoom(lines[1]);//query có dạng actionType;
                List<Message> list = room.getListMess();
                int size = list.size();
                if(size>0)
                {
                    
                    String listMess = "";
                    for (int i = 0; i < size; i++) 
                    {
                        
                            Message mess = list.get(i);
                            listMess+= mess.getSender() + "<col>" + mess.getMess() + "<col>" + "<row>";
                        
                        
                    }
                    user.Send(actionType, ResultCode.OK,lines[1] + ";" + listMess);
                    
                    
                }else
                {
                    user.Send(actionType, ResultCode.OK, "");
                }
                notifyObservers(user.getLastName() + " vừa lấy list tin nhắn");
                break;
            }
//            case ActionType.GET_ROOM_MEMBER:
//            {
//            	
//                List<User> list = user.getRoom(lines[1]).getmListUser();//query có dạng actionType;
//                int size = list.size();
//   //số phòng gửi về mỗi block
//                if(size>0)
//                {
//                    /*Nếu có quá nhiều phòng(2000 chẳng hạn) thì 1 chuỗi string(1 lần gửi) sẽ không
//                    thể chứa hết được thông tin của tất cả phòng
//                    nên ta chia ra từng block gửi nhiều lần.
//                    Trong thực tế thì có thể sử dụng chức năng phân trang, nhưng trong project này
//                    không phân trang nên lựa chọn cách thức này
//                    */
//                    String listMess = "";
//                    for (int i = 0; i < 21; i++) 
//                    {
//                        
//                            String name = list.get(i).getUsername();
//                            listMess+= name + ";";
//                        
//                        
//                    }
//                    user.Send(actionType, ResultCode.OK, listMess);
//                    
//                    
//                }else
//                {
//                    user.Send(actionType, ResultCode.OK, "");
//                }
//                notifyObservers(user.getLastName() + " vừa lấy danh sách phòng");
//                break;
//            }
            case ActionType.JOIN_ROOM:
            {
                String maPhong = lines[1];   //query có dạng actionType;maPhong
                int size = mListRoom.size();
                boolean success = false;
                for (int i = 0; i < size; i++) 
                {
                    Room room = mListRoom.get(i);
                    if(room.getIdRoom().equals(maPhong))
                    {
                        room.AddUser(user);
                        user.addRoom(room.getIdRoom(),room);
                        String count = room.getmListUser().size()+"";
                        user.Send(actionType, ResultCode.OK, room.getNameRoom() + ";" + count);
                        notifyObservers(user.getUsername() + " vừa tham gia phòng " + room.getIdRoom());
                        user.getRoom(room.getIdRoom()).UpdateNumberUser();
                        user.getRoom(room.getIdRoom()).NotifyJustJoinRoom(user);
                        success = true;
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
            	System.out.println(lines[1] +" " + lines[2]);
                String contentMess = "";
                if(lines.length>=2)
                    contentMess = lines[2];   //query có dạng actionType;contentMess
                user.getRoom(lines[1]).SendToAllUser(user.getUsername(), contentMess);
                user.getRoom(lines[1]).addMess(user.getUsername(), contentMess);
                System.out.println(user.getUsername() + " " + contentMess + "\n");
                notifyObservers(user.getUsername() + " vừa gửi tin");
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
        }
    }
    
    
    //Tạo phòng mới
    Room GeneralRoom(String roomName)
    {
        Room room = new Room();
        room.setNameRoom(roomName);
        room.setCountPeople(1);
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
