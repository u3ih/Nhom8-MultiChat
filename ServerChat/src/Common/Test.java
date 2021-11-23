package Common;

import java.util.List;

import dao.room.RoomDAO;
import model.ActionType;
import model.ResultCode;
import model.Room;

public class Test {
	public static void main(String[] args) {
		RoomDAO roomDAO = new RoomDAO();
		 for(Room room:roomDAO.getRoomByUserID(1)) {
			 System.out.println();
		 }
	}
}
