
package controller;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.*;


@SuppressWarnings("deprecation")
public class ClientManager extends Observable{
    
    String serverName = "localhost";
    int port = 1106;
    Socket mSocket;
    BufferedWriter mBufferWriter;
//    ObjectInputStream objectInputStream;
//    ObjectOutputStream objectOutputStream;
    DataInputStream mDataInputStream;
    DataOutputStream mDataOutputStream;
    ObjectOutputStream mObjectOutputStream;
    ObjectInputStream mObjectInputStream;
    Thread mThread;
    public String mNickname;
    @SuppressWarnings("deprecation")
	public ClientManager(Observer obs)  //hÃ m khá»Ÿi táº¡o khi chÆ°a cÃ³ socket
    {
        this.addObserver(obs);
    }
    public ClientManager(Socket socket, Observer obs)   //hÃ m khá»Ÿi táº¡o khi Ä‘Ã£ cÃ³ socket
    {
        this.addObserver(obs);
        mSocket = socket;
    }
    public void Dispose()
    {
        if(mSocket!=null)
        {
            try 
            {
                mSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(ClientManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if(mThread!=null)
            mThread.stop();
    }
    private void getConnect() {
    	try {
			mSocket = new Socket(serverName, port);
			mDataInputStream = new DataInputStream(mSocket.getInputStream());
	        mDataOutputStream = new DataOutputStream(mSocket.getOutputStream());
	        mObjectOutputStream = new ObjectOutputStream(mSocket.getOutputStream());
	        mObjectInputStream = new ObjectInputStream(mSocket.getInputStream());
	        mBufferWriter = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream(), "UTF8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        
    }
    public boolean StartConnect(String nickName, String pass)
    {
        try 
        {
        	getConnect();
            mDataOutputStream.writeUTF(nickName);
            mDataOutputStream.writeUTF(pass);
            String res = mDataInputStream.readUTF();
            if(res.equals("ERROR")) {
            	Result result = new Result("", ResultCode.ERROR, "Sai tÃ i khoáº£n hoáº·c máº­t kháº©u");
                notifyObservers(result);
                return false;
            }
            StartThreadWaitResult();
            return true;
        } catch (IOException ex) 
        {
            Result result = new Result("", ResultCode.ERROR, "KhÃ´ng thá»ƒ káº¿t ná»‘i Ä‘áº¿n server");
            notifyObservers(result);
            return false;
        }
    }
    
    void StartThreadWaitResult()
    {
        mThread = new Thread(new Runnable() 
        {
            @Override
            public void run() 
            {
                try 
                {
                    while(true)
                    {
                    	String[] lines = mDataInputStream.readUTF().split(";", -1);  //limit = -1 sáº½ ko loáº¡i bá»� pháº§n tá»­ cuá»‘i náº¿u bá»‹ rá»—ng vÃ  Ä‘áº£m báº£o Ä‘á»§ 3 pháº§n tá»­ Ä‘á»ƒ ko bá»‹ lá»—i
                        Result result;
                        if(lines.length==3) //náº¿u chá»‰ gá»“m 3 pháº§n tá»©c lÃ  chá»‰ gá»“m actionType;ResultCode;Content
                        {
                            result = new Result(lines[0], lines[1], lines[2]);
                        }else  //náº¿u nhiá»�u hÆ¡n tá»©c lÃ  pháº§n content Ä‘áº±ng sau cÃ³ ;
                        {
                            String content = "";
                            for (int i = 2; i < lines.length; i++)   //nÃªn ná»‘i láº¡i pháº§n content
                            {
                                content += lines[i] + ";";
                            }
                            if(lines[0].equals(ActionType.SEND_FILE)) {
                            	DataFile file = (DataFile) mObjectInputStream.readObject();
                            	result = new Result(lines[0], lines[1], content, file);
                            } else {
                            	result = new Result(lines[0], lines[1], content);
                            }
                        }
                        notifyObservers(result);
                           
                    }
                }catch (IOException | ClassNotFoundException ex) {
                   
                } 
            }
        });
        mThread.start();
    }
    
    @Override
    public void notifyObservers(Object arg) {
        super.setChanged();
        super.notifyObservers(arg);
    }
 
    public void SendMess(String maPhong, String mess)
    {
        mess = mess.replaceAll("\\n", "<br>");
        String line = ActionType.SEND_MESSAGE+ ";" + maPhong + ";" + mess;
        try 
        {
            mBufferWriter.write(line + "\n");
            mBufferWriter.flush();
        } catch (IOException ex) {
            Result result = new Result("", ResultCode.ERROR, "KhÃ´ng thá»ƒ káº¿t ná»‘i tá»›i server");
            notifyObservers(result);
        }
    }
    
    public void SendFile(String maPhong, DataFile file)
    {
        String line = ActionType.SEND_FILE + ";" + maPhong + "; " ;
        try 
        {
        	mBufferWriter.write(line + "\n");
            mBufferWriter.flush();
            mObjectOutputStream.writeObject(file);
            mObjectOutputStream.flush();
        } catch (IOException ex) {
            Result result = new Result("", ResultCode.ERROR, "Không thể kết nối tới server");
            notifyObservers(result);
        }
    }
    
    public void Login(String nickName) throws UnsupportedEncodingException  //vÃ¬ lÃ m Ä‘Æ¡n giáº£n nÃªn chá»‰ cáº§n Ä‘Äƒng nháº­p vá»›i há»� tÃªn
    {
        String line = ActionType.LOGIN + ";" + nickName;
        mNickname = nickName;
        try
        {
            mBufferWriter.write(line + "\n");
            mBufferWriter.flush();
        } catch (IOException ex) {
            Result result = new Result("", ResultCode.ERROR, "KhÃ´ng thá»ƒ káº¿t ná»‘i tá»›i server");
            notifyObservers(result);
        }
    }

    public boolean Register(String Fname,String Mname,String Lname,String Birday,int Age,String Uname,String Pass,String gender) {
        try
        {
        	getConnect();
            
        	mDataOutputStream.writeUTF(ActionType.REGISTER);
        	mDataOutputStream.writeUTF(Fname);
        	mDataOutputStream.writeUTF(Mname);
        	mDataOutputStream.writeUTF( Lname);
        	mDataOutputStream.writeUTF(Birday);
        	mDataOutputStream.writeInt(Age);
        	mDataOutputStream.writeUTF(Uname);
        	mDataOutputStream.writeUTF(Pass);
        	mDataOutputStream.writeUTF(gender);
        	
        	String res = mDataInputStream.readUTF();
            if(res.equals("ERROR")) {
            	Result result = new Result("", ResultCode.ERROR, "Lỗi đăng ký");
                notifyObservers(result);
                return false;
            } else if(res.equals("OK")) {
            	Result result = new Result("", ResultCode.OK, "Đăng ký thành công");
                notifyObservers(result);
                return true;
            }
        } catch (IOException ex) {
            Result result = new Result("", ResultCode.ERROR, "Không thể kết nối tới server");
            notifyObservers(result);
        }
		return false;
	}
    
    public void getMess(String roomID) {
    	String line = ActionType.GET_MESS + ";" + roomID;
        try
        {
            mBufferWriter.write(line + "\n");
            mBufferWriter.flush();
        } catch (IOException ex) {
            Result result = new Result("", ResultCode.ERROR, "KhÃ´ng thá»ƒ káº¿t ná»‘i tá»›i server");
            notifyObservers(result);
        }
    }

    public boolean isSuccessReg(){
    	String s="OK";
    	String result;
		try {
			result = mDataInputStream.readUTF();
			return s.equals(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return false;
    }
    
    public void GetListRoom()
    {
        String line = ActionType.GET_LIST_ROOM + ";";
        try
        {
            mBufferWriter.write(line + "\n");
            mBufferWriter.flush();
        } catch (IOException ex) {
            Result result = new Result("", ResultCode.ERROR, "Káº¿t ná»‘i tá»›i server cÃ³ lá»—i");
            notifyObservers(result);
        }
    }
    
    public void GetRoomMember(String roomID) {
    	String line = ActionType.GET_ROOM_MEMBER + ";" + roomID;
    	try {
    		mBufferWriter.write(line + "\n");
            mBufferWriter.flush();
		} catch (Exception e) {
			Result result = new Result("", ResultCode.ERROR, "Káº¿t ná»‘i tá»›i server cÃ³ lá»—i");
            notifyObservers(result);
		}
    }
    
    public void CreateRoom(String roomName)
    {
        String line = ActionType.CREATE_ROOM + ";" + roomName;
        try
        {
            mBufferWriter.write(line + "\n");
            mBufferWriter.flush();
        } catch (IOException ex) {
            Result result = new Result("", ResultCode.ERROR, "Káº¿t ná»‘i tá»›i server cÃ³ lá»—i");
            notifyObservers(result);
        }
    }
    
    public void JoinRoom(String maPhong)
    {
        String line = ActionType.JOIN_ROOM + ";" + maPhong;
        try
        {
            mBufferWriter.write(line + "\n");
            mBufferWriter.flush();
        } catch (IOException ex) {
            Result result = new Result("", ResultCode.ERROR, "Káº¿t ná»‘i tá»›i server cÃ³ lá»—i");
            notifyObservers(result);
        }
    }
    public void LeaveRoom()
    {
        String line = ActionType.LEAVE_ROOM + ";null";
        try
        {
            mBufferWriter.write(line + "\n");
            mBufferWriter.flush();
        } catch (IOException ex) {
            Result result = new Result("", ResultCode.ERROR, "Káº¿t ná»‘i tá»›i server cÃ³ lá»—i");
            notifyObservers(result);
        }
    }
    public void closeWindow(String roomID) {
    	notifyObservers(new Result(ActionType.Close_WINDOW_CHAT,ResultCode.OK,roomID));
    }
    public void Logout()
    {
        String line = ActionType.LOGOUT + ";null";
        try
        {
            mBufferWriter.write(line + "\n");
            mBufferWriter.flush();
        } catch (IOException ex) {
            Result result = new Result("", ResultCode.ERROR, "Káº¿t ná»‘i tá»›i server cÃ³ lá»—i");
            notifyObservers(result);
        }
    }
    
    
}
