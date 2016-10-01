package dbModels;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;

import exceptions.CannotConnectToDBException;
import models.Activity;
import models.Destination;
import models.Destination.Category;
import models.PlaceToEat;
import models.PlaceToSleep;
import models.Sight;
import models.User;

public class DestinationDAO {

	private static DestinationDAO instance; // Singleton

	private DestinationDAO() {

	}

	public static synchronized DestinationDAO getInstance() {
		if (instance == null) {
			instance = new DestinationDAO();
		}
		return instance;
	}

	public Set<Destination> getAllDestinations() {
		Set<Destination> destinations = new HashSet<Destination>();
		Statement statement = null;
		ResultSet result = null;
		try {
			try {
				statement = DBManager.getInstance().getConnection().createStatement();
				String selectAllDestinationsFromDB = "SELECT name, description, longitude, lattitude, picture FROM destinations;";
				result = statement.executeQuery(selectAllDestinationsFromDB);
				while (result.next()) {
					Destination dest = new Destination(result.getString("name"),
							Double.parseDouble(result.getString("lattitude")),
							Double.parseDouble(result.getString("longitude")), result.getString("description"),
							result.getString("main_picture"), result.getString("author_email"),
							Destination.Category.valueOf(result.getString("category")),
							result.getInt("number_of_likes"), result.getInt("number_of_dislikes"));
					destinations.add(dest);
				}
			} catch (CannotConnectToDBException e) {
				// TODO handle exception - write to log and userFriendly screen
				e.getMessage();
				return destinations;
			}
		} catch (SQLException e) {
			// TODO write in the log
			return destinations;
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
		return destinations;
	}

	public synchronized boolean saveDestinationToDB(User u, Destination destination) {
		String insertDestinationInfoIntoDB = "INSERT INTO destinations (name, description, longitude, lattitude, picture) VALUES (?, ?, ?, ?, ?);";
		String insertIntoVisitedDestinations = "INSERT INTO visited_destinations (destination_name, user_email) VALUES (?, ?);";
		PreparedStatement statement = null;
		PreparedStatement statement2 = null;
		try {
			DBManager.getInstance().getConnection().setAutoCommit(false);
			statement = DBManager.getInstance().getConnection().prepareStatement(insertDestinationInfoIntoDB);
			statement.setString(1, destination.getName());
			statement.setString(2, destination.getDescription());
			statement.setDouble(3, destination.getLocation().getLongitude());
			statement.setDouble(4, destination.getLocation().getLattitude());
			statement.setString(5, destination.getMainPicture());
			statement.executeUpdate();

			statement2 = DBManager.getInstance().getConnection().prepareStatement(insertIntoVisitedDestinations);
			statement2.setString(1, destination.getName());
			statement2.setString(2, u.getEmail());
			statement2.executeUpdate();
			DBManager.getInstance().getConnection().commit();
			return true;
		} catch (SQLException e) {
			try {
				DBManager.getInstance().getConnection().rollback();
			} catch (SQLException | CannotConnectToDBException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
			return false;
		} catch (CannotConnectToDBException e) {
			// TODO handle exception - write to log and userFriendly screen
			e.getMessage();
			return false;
		} finally {
			try {
				DBManager.getInstance().getConnection().setAutoCommit(true);
				if (statement != null) {
					statement.close();
				}
				if (statement2 != null) {
					statement2.close();
				}
			} catch (SQLException | CannotConnectToDBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public synchronized boolean updateDestinationInDB(String name, String description, double longitude,
			double lattitude, String mainPicture, ConcurrentSkipListSet<PlaceToSleep> placesToSleep,
			ConcurrentSkipListSet<PlaceToEat> placesToEat, Category category, CopyOnWriteArrayList<String> pictures,
			CopyOnWriteArrayList<String> videos, ConcurrentSkipListSet<Activity> activities,
			ConcurrentSkipListSet<Sight> sights) {
		PreparedStatement prepStatement = null;
		String updateDestinationStatement = "UPDATE destinations SET description=?, longitude=?, lattitude=?, picture=?  WHERE name=?;";
		try {
			// TODO update only current fields

			prepStatement = DBManager.getInstance().getConnection().prepareStatement(updateDestinationStatement);
			prepStatement.setString(1, description);
			prepStatement.setDouble(2, longitude);
			prepStatement.setDouble(3, lattitude);
			prepStatement.setString(4, mainPicture);
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

	public void removeDestination(String destinationName) {
		// TODO remove from 2 tables

	}

	public boolean addLike(String userEmail, String destinationName) {
		// TODO update like (userLiker and number Of likes; +/- userDisliker and
		// number of dislikes)
		return false;
	}

	public boolean removeLike(String userEmail, String destinationName) {
		// TODO update dislike (userDisliker and number Of dislikes; +/-
		// userLiker and number of likes)
		return false;
	}

}
