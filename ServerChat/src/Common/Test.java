package Common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;

import dao.room.RoomDAO;
import model.ActionType;
import model.ResultCode;
import model.Room;

public class Test {
	public static void main(String[] args) {
		RoomDAO roomDAO = new RoomDAO();
		HashMap<String,Room> listRoom = roomDAO.getAllRoom();
		for(Map.Entry<String, Room> entry : listRoom.entrySet()) {
			 Room r = entry.getValue();
			 System.out.println(r.toString());
		}
		
	}	
}
