package dao.user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import Common.Common;
import model.User;

/**
 *
 * @author DELL
 */
public class UserDAO {

    private String jdbcURL = Common.jdbcUrl;
    private String jdbcUsername = Common.jdbcUsername;
    private String jdbcPassword = Common.jdbcPassword;
    private static final String INSERT_USER_SQL = "INSERT INTO user" + " (firstName,midName,lastName,birthDay,age,gender,isOnline,username,password, img) VALUES " +" (?, ?, ?, ?, ?, ?, ?,?,?, ?);";
    private static final String SELECT_USER_BY_ID = "select id,firstName,midName,lastName,birthDay,age,gender from user where id =  ?";
    private static final String SELECT_ALL_USER = "select * from user";
    private static final String DELETE_USER_SQL = "delete from user where id = ?;";
    private static final String UPDATE_USER_SQL = "update user set firstName = ?,midName = ?,lastName  =  ?,birthDay = ?,age = ?,gender = ? where  username =  ?";
    private static final String LOGIN = "SELECT id,firstName,midName,lastName,birthDay,age,gender,isOnline,img from user where username =? and password = ?";
    private static final String SELECT_USER_BY_UNAME = "select firstName,midName,lastName,birthDay,age,gender from user where username =  ?";
    //private static final String SELECT_FRIENDUSER_BY_ID = "select firstName,midName,lastName,birthDay,age,gender from user where id =  ?";
    private static final String SELECT_FRIENDUSER_BY_ID = "SELECT * FROM chat.userconnection inner join chat.user on user.id=userconnection.userID2 where userID1=?";
    
    private static final String SELECT_ID_BY_UNAME = "select id from user where username =  ?";
    private static final String SELECT_USER_BY_NAME = "select id,firstName,midName,lastName,birthDay,age,gender from user where lastName=? and username not like ?";
    private static final String INSERT_USERCONNECTION_SQL = "INSERT INTO userconnection" + " (userID1,userID2) VALUES " +" (?, ?);";
    private static final String SELECT_ID_FRIEND =" select userID2 from userconnection where userID1 =?";
    private static final String UPDATE_USER_ONLINE = "update user set isOnline = ? where username = ?";
    private static final String SELECT_isOnline_BY_UNAME = "select isOnline from user where username =  ?";
    private static final String SELECT_A_USER_BY_ID="Select * from user where id=?;";
    public UserDAO() {
    }

     public Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername,
                    jdbcPassword);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return connection;
    }

    public void insertUser(User user) throws SQLException {
        System.out.println(INSERT_USER_SQL);
        // try-with-resource statement will auto close the connection.
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL)) {
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getMidName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getBirthDay());
            preparedStatement.setInt(5, user.getAge());
            preparedStatement.setString(6, user.isGender());
            preparedStatement.setInt(7, 0);
            preparedStatement.setString(8, user.getUsername());
            preparedStatement.setString(9, user.getPassword());
            preparedStatement.setString(10, "/Image/10207-man-student-light-skin-tone-icon-32.png");
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public User selectUser(int id) {
        User user = null;
        // Step 1: Establishing a Connection
        try (Connection connection = getConnection();
                // Step 2:Create a statement using connection object
                PreparedStatement preparedStatement
                = connection.prepareStatement(SELECT_USER_BY_ID);) {
            preparedStatement.setInt(1, id);
            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            ResultSet rs = preparedStatement.executeQuery();
            // Step 4: Process the ResultSet object.
            while (rs.next()) {
                String firstName = rs.getString("firstName");
                String midName = rs.getString("midName");
                String lastName = rs.getString("lastName");
                String birthDay = rs.getString("birthDay");
                int age = rs.getInt("age");
                String gender = rs.getString("gender");
                user = new User(id, firstName, midName, lastName, birthDay, age, gender);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return user;
    }

    public HashMap<String, User> selectAllUsers() {
        // using try-with-resources to avoid closing resources (boiler plate code)
    	HashMap<String, User> users = new HashMap<>();
        // Step 1: Establishing a Connection
        try (Connection connection = getConnection();
                // Step 2:Create a statement using connection object
                PreparedStatement preparedStatement
                = connection.prepareStatement(SELECT_ALL_USER);) {
            //System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            ResultSet rs = preparedStatement.executeQuery();
            // Step 4: Process the ResultSet object.
            while (rs.next()) {
                int id = rs.getInt("id");
                String firstName = rs.getString("firstName");
                String midName = rs.getString("midName");
                String lastName = rs.getString("lastName");
                String birthDay = rs.getString("birthDay");
                int age = rs.getInt("age");
                String gender = rs.getString("gender");
                boolean isOnline = rs.getBoolean("isOnline");
                String userName = rs.getString("userName");
                String passWord = rs.getString("passWord");
                String img = rs.getString("img");
                
                users.put(userName, new User(id, firstName, midName, lastName, birthDay, age, gender, isOnline, userName, passWord));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return users;
    }
    public User selectUserbyuname(String uname) {
        User user = null;
        // Step 1: Establishing a Connection
        try (Connection connection = getConnection();
                // Step 2:Create a statement using connection object
                PreparedStatement preparedStatement
                = connection.prepareStatement(SELECT_USER_BY_UNAME);) {
            preparedStatement.setString(1, uname);
            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            ResultSet rs = preparedStatement.executeQuery();
            // Step 4: Process the ResultSet object.
            while (rs.next()) {
                String firstName = rs.getString("firstName");
                String midName = rs.getString("midName");
                String lastName = rs.getString("lastName");
                String birthDay = rs.getString("birthDay");
                int age = rs.getInt("age");
                String gender = rs.getString("gender");
                user = new User( firstName, midName, lastName, birthDay, age, gender);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return user;
    }
    public int selectIDbyuname(String uname) {
    	int id=0;
    	try (Connection connection = getConnection();
                
                PreparedStatement preparedStatement
                = connection.prepareStatement(SELECT_ID_BY_UNAME);) {
            preparedStatement.setString(1, uname);
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()) {
            	id=rs.getInt("id");
            }
    	}catch (SQLException e) {
                System.out.println(e);
            }
            return id;
    }
    public boolean getListFrinedOnline(String uname) {
    	boolean isOnline =false;
    	try (Connection connection = getConnection();
                
                PreparedStatement preparedStatement
                = connection.prepareStatement(SELECT_isOnline_BY_UNAME);) {
            preparedStatement.setString(1, uname);
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()) {
            	
            	isOnline = rs.getBoolean("isOnline");
            }
    	}catch (SQLException e) {
                System.out.println(e);
            }
            return isOnline;
    }
    public int selectIdFriend(int userID1) {
    	int id=0;
    	try (Connection connection = getConnection();
                
                PreparedStatement preparedStatement
                = connection.prepareStatement(SELECT_ID_FRIEND);) {
            preparedStatement.setInt(1, userID1);
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()) {
            	id=rs.getInt("id");
            }
    	}catch (SQLException e) {
                System.out.println(e);
            }
            return id;
    }
    public ArrayList<User> selectFriendbyId(int userID){
    	ArrayList<User> users = new ArrayList<>();
    	try (Connection connection = getConnection();
                
                PreparedStatement preparedStatement
                = connection.prepareStatement(SELECT_FRIENDUSER_BY_ID);) {       	
        		//userID2 = selectIdFriend()
    		preparedStatement.setInt(1, userID);
            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
            	int id= rs.getInt("userid2");
                String firstName = rs.getString("firstName");
                String midName = rs.getString("midName");
                String lastName = rs.getString("lastName");
                String birthDay = rs.getString("birthDay");
                int age = rs.getInt("age");
                String gender = rs.getString("gender");
                boolean isOnline = rs.getBoolean("isOnline");
                String username = rs.getString("username");
                String pass = rs.getString("password");
                users.add(new User(id,firstName, midName, lastName, birthDay, age, gender,isOnline, username, pass));
            	}
            }
            catch (SQLException e) {
            System.out.println(e);
        }
        return users;
    }
    
    public ArrayList<User> selectUsersbyName(String name,String uname) {
        // using try-with-resources to avoid closing resources (boiler plate code)
    	ArrayList<User> users = new ArrayList<>();
    	
        // Step 1: Establishing a Connection
        try (Connection connection = getConnection();
                // Step 2:Create a statement using connection object
        		
        		
                PreparedStatement preparedStatement
                = connection.prepareStatement(SELECT_USER_BY_NAME);) {       	
        		preparedStatement.setString(1, name);
        		preparedStatement.setString(2, uname);
            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            ResultSet rs = preparedStatement.executeQuery();
            // Step 4: Process the ResultSet object.
            while (rs.next()) {
                int id = rs.getInt("id");
                String firstName = rs.getString("firstName");
                String midName = rs.getString("midName");
                String lastName = rs.getString("lastName");
                String birthDay = rs.getString("birthDay");
                int age = rs.getInt("age");
                String gender = rs.getString("gender");
                
                users.add(new User(id,firstName, midName, lastName, birthDay, age, gender));
            	}
            }
            catch (SQLException e) {
            System.out.println(e);
        }
        return users;
    }
    public User Login(String username, String password) {
    	User user = new User();
    	try (Connection connection = getConnection();
    			PreparedStatement preparedStatement = connection.prepareStatement(LOGIN)){
    		preparedStatement.setString(1, username);
    		preparedStatement.setString(2, password);
    		ResultSet rs = preparedStatement.executeQuery();
    		
    		if(rs.wasNull() == false) {
    			while(rs.next()) {
        			int id = rs.getInt("id");
                    String firstName = rs.getString("firstName");
                    String midName = rs.getString("midName");
                    String lastName = rs.getString("lastName");
                    String birthDay = rs.getString("birthDay");
                    int age = rs.getInt("age");
                    String gender = rs.getString("gender");
                    boolean isOnline = rs.getBoolean("isOnline");
                    String img = rs.getString("img");
                    user.setId(id);
                    user.setFirstName(firstName);
                    user.setMidName(midName);
                    user.setLastName(lastName);
                    user.setBirthDay(birthDay);
                    user.setAge(age);
                    user.setGender(gender);
                    
        		}
    		}
    		else return null;
    		
    		
    	}catch (Exception e) {
			System.out.println(e);
		}
    	return user;
    }
    public boolean insertUserconnection(int userID1,String uname) throws SQLException {
    	boolean rowUpdated=false;
        try (Connection connection = getConnection(); 
                PreparedStatement statement
                = connection.prepareStatement(INSERT_USERCONNECTION_SQL);
                PreparedStatement statement1
                = connection.prepareStatement(INSERT_USERCONNECTION_SQL)) {
            int userID2= selectIDbyuname(uname);
            statement.setInt(1, userID2);
            statement.setInt(2, userID1);
            
           rowUpdated = statement.executeUpdate() > 0;
           statement1.setInt(1, userID1);
           statement1.setInt(2, userID2);
           statement1.executeUpdate();
        } catch (Exception e){
         System.out.println(e);
      }
     return rowUpdated;  
    }

    public boolean deleteUser(int id) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = getConnection(); PreparedStatement statement
                = connection.prepareStatement(DELETE_USER_SQL);) {
            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    public boolean updateUser(User user) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = getConnection(); 
                PreparedStatement statement
                = connection.prepareStatement(UPDATE_USER_SQL);) {
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getMidName());
            statement.setString(3, user.getLastName());
            statement.setString(4, user.getBirthDay());
            statement.setInt(5, user.getAge());
            statement.setString(6, user.isGender());
            //statement.setInt(7, user.getId());
            statement.setString(7, user.getUsername());
            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }
    public void updateOnline (User user) throws SQLException {
    	boolean rUpdated;
    	try (Connection connection = getConnection(); 
                PreparedStatement statement
                = connection.prepareStatement(UPDATE_USER_ONLINE);) {
    		statement.setBoolean(1, user.isOnline());
    		statement.setString(2, user.getUsername());
    	 statement.executeUpdate();
    	}
    	//return rUpdated;
    }
    public User selectAllInfoAUserByID(int id) {
    	User user = null;
        // Step 1: Establishing a Connection
        try (Connection connection = getConnection();
                // Step 2:Create a statement using connection object
                PreparedStatement preparedStatement
                = connection.prepareStatement(SELECT_A_USER_BY_ID);) {
            preparedStatement.setInt(1, id);
            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            ResultSet rs = preparedStatement.executeQuery();
            // Step 4: Process the ResultSet object.
             if(rs.next()) {
                String firstName = rs.getString("firstName");
                String midName = rs.getString("midName");
                String lastName = rs.getString("lastName");
                String birthDay = rs.getString("birthDay");
                int age = rs.getInt("age");
                String gender = rs.getString("gender");
                boolean isOnline = rs.getBoolean("isOnline");
                String username = rs.getString("username");
                String pass = rs.getString("password");
                user = new User(id,firstName, midName, lastName, birthDay, age, gender,isOnline, username, pass);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return user;
    }
}
