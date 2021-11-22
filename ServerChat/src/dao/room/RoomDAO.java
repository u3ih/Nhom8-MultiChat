/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.room;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.MessageRoom;

/**
 *
 * @author asus
 */
public class RoomDAO {
    private String jdbcUrl = "jdbc:mysql://localhost:3306/chat?useSSL=false";
    private String jdbcUsername = "root";
    private String jdbcPassword = "Hungblo123";
    
    private static final String CREATE_ROOM = "INSERT INTO room " + 
            "(id,numberOfUser,roomname) VALUES " + "(?,?,?)";
    private static final String ADD_ROOM_CONNECTION = "INSERT INTO roomConnection (roomID, userID)" +
            "VALUES (?, ?)";
    private static final String DELETE_ROOM_CONNECTION = "DELETE FROM roomConnection where roomID = ? AND userID = ?";
    private static final String SELECT_ROOM_MEMBER_BY_ROMID = "SELECT userID,username from roomConnection where roomID = ?";
    private static final String SELECT_ROOM_BY_NAME = "SELECT * FROM room WHERE roomName = ?";
    private static final String SELECT_ROOM_BY_ID = "SELECT * FROM room WHERE id = ?";
    private static final String UPDATE_ROOM_NAME = "UPDATE room SET roomName = ? Where id = ?";
    private static final String UPDATE_ROOM_MEMBER_COUNT = "UPDATE room SET numberOfUser = ? Where id = ?";
    private static final String GET_MESS_BY_ROOMID = "select username,content "
            +"from messageroom inner join user " + "on messageroom.userid = user.id "
            +"where roomid = ? order by messageroom.id ASC limit 20";
    private static final String GET_NEWEST_MESS = "select username,content "
            +"from messageroom inner join user " + "on messageroom.userid = user.id "
            +"where roomid = ? order by messageroom.id desc limit 1";
    private static final String INSERT_MESS = "INSERT INTO messageroom (roomID, userID, content) values (?,?,?);";

    public RoomDAO() {
    }
    
    private Connection getConnection(){
        Connection c = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
            
        } catch (Exception e) {
            System.out.println(e);
        }
        return c;
    }
    
    public int InsertMess(String roomID, int userID, String content) {
    	int s = 0;
        try (Connection c = getConnection();
                PreparedStatement prepare = c.prepareStatement(INSERT_MESS)) {
            
            prepare.setString(1, roomID);
            prepare.setString(3, content);
            prepare.setInt(2, userID);
            s = prepare.executeUpdate();
            c.close();
            prepare.close();
            
        } catch (Exception e) {
        }
        return s;
    }
    
    public List<MessageRoom> getMessByRoomID(String roomID){
        List<MessageRoom> list = new ArrayList<>();
            
        try (Connection c = getConnection();
                PreparedStatement prepare = c.prepareStatement(GET_MESS_BY_ROOMID)) {
            
            prepare.setString(1, roomID);
            ResultSet rs = prepare.executeQuery();
            while(rs.next()){
                list.add(new MessageRoom(rs.getString("userName"),rs.getString("content")));
            }
            c.close();
            prepare.close();
            
        } catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }
    
    public MessageRoom getNewestMess(String roomID){
        MessageRoom mess = null;
            
        try (Connection c = getConnection();
                PreparedStatement prepare = c.prepareStatement(GET_MESS_BY_ROOMID)) {
            
            prepare.setString(1, roomID);
            ResultSet rs = prepare.executeQuery();
            rs.next();
             mess = new MessageRoom(rs.getString("userName"),rs.getString("content"));
             c.close();
             prepare.close();
            
        } catch (Exception e) {
            System.out.println(e);
        }
        return mess;
    }
    
    public int createRoom(String id,int numberOfUser, String name){
        int status = 0;
        try (Connection c = getConnection();
                PreparedStatement prepare = c.prepareStatement(CREATE_ROOM)) {
            
            prepare.setString(1, id);
            prepare.setInt(2,numberOfUser);
            prepare.setString(3, name);
            status = prepare.executeUpdate();
            c.close();
            prepare.close();
            
        } catch (Exception e) {
        }
        return status;
    }
    
    public int addRoomConnection(String roomID, int userID){
        int status = 0;
        try(Connection c = getConnection();
                PreparedStatement prepare = c.prepareStatement(ADD_ROOM_CONNECTION)){
            
            prepare.setString(1,roomID);
            prepare.setInt(2,userID);
            status = prepare.executeUpdate();
            c.close();
            prepare.close();
            
        } catch (Exception e){
            System.out.println(e);
        }
        return status;
    }
    
    public int deleteRoomConnection(String roomID, int userID){
        int status = 0;
        try(Connection c = getConnection();
                PreparedStatement prepare = c.prepareStatement(DELETE_ROOM_CONNECTION)){
            
            prepare.setString(1, roomID);
            prepare.setInt(2,userID);
            status = prepare.executeUpdate();
            c.close();
            prepare.close();
            
        }catch (Exception e){
            System.out.println(e);
        }
        return status;
    }
    
    public int changeRoomName(String roomID, String name){
        int status = 0;
        try(Connection c = getConnection();
                PreparedStatement prepare = c.prepareStatement(UPDATE_ROOM_NAME)){
            
            prepare.setString(2, roomID);
            prepare.setString(1, name);
            status = prepare.executeUpdate();
            c.close();
            prepare.close();
            
        }catch(Exception e){
            System.out.println(e);
        }
        return status;
    }
    
    public int UpdateMemberCount(String roomID, int count){
        int status = 0;
        try(Connection c = getConnection();
                PreparedStatement prepare = c.prepareStatement(UPDATE_ROOM_MEMBER_COUNT)){
            
            prepare.setString(2, roomID);
            prepare.setInt(1, count);
            status = prepare.executeUpdate();
            c.close();
            prepare.close();
            
        }catch(Exception e){
            System.out.println(e);
        }
        return status;
    }
    
    public List<Integer> getRoomMember(int roomID){
        
        List<Integer> list = new ArrayList<>();
        
        try (Connection c = getConnection();
                PreparedStatement prepare = c.prepareStatement(SELECT_ROOM_MEMBER_BY_ROMID)) {
            
            prepare.setInt(1, roomID);
            ResultSet rs = prepare.executeQuery();
            while(rs.next()){
                list.add(rs.getInt("userID"));
            }
            c.close();
            prepare.close();
            
        } catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }
}
