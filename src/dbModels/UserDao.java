package dbModels;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import exceptions.CannotConnectToDBException;
import models.User;

public class UserDao {

	private static UserDao instance; // Singleton

	private UserDao() {
	}

	public static synchronized UserDao getInstance() {
		if (instance == null) {
			instance = new UserDao();
		}
		return instance;
	}

	public Set<User> getAllUsers() {
		System.out.println("Getting all users from DB!!!!");
		Set<User> users = new HashSet<User>();
		Statement statement = null;
		ResultSet result = null;
		try {
			try {
				statement = DBManager.getInstance().getConnection().createStatement();
				String selectAllUsersFromDB = "SELECT email, password, first_name, last_name, description, profile_picture, rating, times_liked FROM users;";
				result = statement.executeQuery(selectAllUsersFromDB);
				while (result.next()) {
					User user = new User(result.getString("email"), result.getString("password"),
							result.getString("first_name"), result.getString("last_name"),
							result.getString("description"), result.getString("profile_picture"),
							result.getDouble("rating"), result.getInt("times_liked")); // creating
																						// a
																						// new
																						// user
																						// with
																						// info
																						// from
																						// DB
					users.add(user); // add user to allUsers cache
				}
				System.out.println("All users returned from DB.");
				// TODO add destinations to each user (form DB)
			} catch (CannotConnectToDBException e) {
				// TODO handle exception - write to log and userFriendly screen
				e.getMessage();
				System.out.println("NO users returned!!!!!");
				return users;
			}
		} catch (SQLException e) {
			// TODO write in the log
			System.out.println("NO users returned!!!!!");
			return users;
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (result != null) {
					result.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return users;
	}

	public synchronized boolean saveUserToDB(User user) {
		// save user
		String insertUserInfoIntoDB = "INSERT INTO users (email, password, first_name, last_name, description, profile_picture, rating, times_liked) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
		PreparedStatement statement = null;
		try {
			statement = DBManager.getInstance().getConnection().prepareStatement(insertUserInfoIntoDB);
			statement.setString(1, user.getEmail());
			statement.setString(2, user.getPassword());
			statement.setString(3, user.getFirstName());
			statement.setString(4, user.getLastName());
			statement.setString(5, user.getDescription());
			statement.setString(6, user.getProfilePicture());
			statement.setDouble(7, user.getRating());
			statement.setInt(8, user.getTimesLiked());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (CannotConnectToDBException e) {
			// TODO handle exception - write to log and userFriendly screen
			e.getMessage();
			return false;
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}

	public synchronized boolean updateUserInDB(String email, String password, String firstName, String lastName,
			String description, String profilePicture) {
		// The user updates all their fields except email (primary key)
		PreparedStatement prepStatement = null;
		String updateUserStatement = "UPDATE users SET password=?, first_name=?, last_name=?, description=?, profile_picture=? WHERE email=?;";
		try {
			prepStatement = DBManager.getInstance().getConnection().prepareStatement(updateUserStatement);
			prepStatement.setString(1, password);
			prepStatement.setString(2, firstName);
			prepStatement.setString(3, lastName);
			prepStatement.setString(4, description);
			prepStatement.setString(5, profilePicture);
			prepStatement.setString(6, email);
			prepStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (CannotConnectToDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			if (prepStatement != null) {
				try {
					prepStatement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public synchronized boolean removeUserFromDB(User user) {
		// TODO implement
	}

	public boolean addToFollowedUsers(User user, String followedUserEmail) {
		// adds the data into followers DB table
		String insertFollowedUserIntoDB = "INSERT INTO followers (follower_email, followed_email) VALUES (?, ?);";
		PreparedStatement statement = null;
		try {
			statement = DBManager.getInstance().getConnection().prepareStatement(insertFollowedUserIntoDB);
			statement.setString(1, user.getEmail());
			statement.setString(2, followedUserEmail);
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			// TODO handle exception
			e.printStackTrace();
		} catch (CannotConnectToDBException e) {
			// TODO handle exception
			e.printStackTrace();
		}
		return false;
	}

	public boolean removeFromFollowedUsers(User user, String followedUserEmail) {
		// delete the following relationship between the two users
		String deleteFollowedUserFromDB = "DELETE FROM followers where follower_email=? AND followed_email=?;";
		PreparedStatement statement = null;
		try {
			statement = DBManager.getInstance().getConnection().prepareStatement(deleteFollowedUserFromDB);
			statement.setString(1, user.getEmail());
			statement.setString(2, followedUserEmail);
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			// TODO handle exception
			e.printStackTrace();
		} catch (CannotConnectToDBException e) {
			// TODO handle exception
			e.printStackTrace();
		}
		return false;
	}

	private void displaySqlErrors(SQLException e) {
		System.out.println("SQLException: " + e.getMessage());
		System.out.println("SQLState: " + e.getSQLState());
		System.out.println("Vendor error: " + e.getErrorCode());
	}
}
