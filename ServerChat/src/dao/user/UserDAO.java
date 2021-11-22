package dao.user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private static final String UPDATE_USER_SQL = "update user set firstName = ?,midName = ?,lastName  =  ?,birthDay = ?,age = ?,gender = ? where  id =  ?;";
    private static final String LOGIN = "SELECT id,firstName,midName,lastName,birthDay,age,gender,isOnline,img from user where username =? and password = ?";
   
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
            statement.setInt(7, user.getId());
            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }
}
