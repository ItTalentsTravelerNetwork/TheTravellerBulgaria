package com.springframework.dbModels;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;

import com.springframework.exceptions.CannotConnectToDBException;
import com.springframework.exceptions.InvalidDataException;
import com.springframework.exceptions.InvalidLocationException;
import com.springframework.model.Activity;
import com.springframework.model.Destination;
import com.springframework.model.Destination.Category;
import com.springframework.model.PlaceToEat;
import com.springframework.model.PlaceToSleep;
import com.springframework.model.Sight;
import com.springframework.model.User;


public class DestinationDao {

	private ConcurrentHashMap<String, ArrayList<String>> destinationPictures;
	
	private static DestinationDao instance;
	
	public static DestinationDao getInstance() {
		if (instance == null) {
			instance = new DestinationDao();
		}
		return instance;
	}

	private DestinationDao() {
		this.destinationPictures = this.getAllDestPictures();
	}

	private ConcurrentHashMap<String, ArrayList<String>> getAllDestPictures() {

		ConcurrentHashMap<String, ArrayList<String>> allPics = new ConcurrentHashMap<>();
		Statement statement = null;
		ResultSet result = null;
		try {
			statement = DBManager.getInstance().getConnection().createStatement();
			result = statement.executeQuery("SELECT picture, destination_name from destination_pictures;");
			if (result != null) {
				while (result.next()) {
					String name = result.getString("destination_name");
					String pic = result.getString("picture");
					if (!allPics.containsKey(name)) {
						allPics.put(result.getString("destination_name"), new ArrayList<String>());
					}
					allPics.get(name).add(pic);
				}
			}

		} catch (SQLException | CannotConnectToDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return allPics;
	}

	public Set<Destination> getAllDestinations() {
		Set<Destination> destinations = new HashSet<Destination>();
		Statement statement = null;
		ResultSet result = null;
		try {
			try {
				statement = DBManager.getInstance().getConnection().createStatement();
				String selectAllDestinationsFromDB = "SELECT name, description, lattitude, longitude, main_picture, author_email, category, number_of_likes, number_of_dislikes FROM destinations;";
				result = statement.executeQuery(selectAllDestinationsFromDB);
				while (result.next()) {
					Destination dest = new Destination(result.getString("name"), result.getString("description"),
							Double.parseDouble(result.getString("lattitude")),
							Double.parseDouble(result.getString("longitude")), result.getString("main_picture"),
							result.getString("author_email"),
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
		String insertDestinationInfoIntoDB = "INSERT INTO destinations (name, description, lattitude, longitude, main_picture, author_email, category, number_of_likes, number_of_dislikes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
		String insertIntoVisitedDestinations = "INSERT INTO visited_destinations (destination_name, user_email) VALUES (?, ?);";
		PreparedStatement statement = null;
		PreparedStatement statement2 = null;
		try {
			DBManager.getInstance().getConnection().setAutoCommit(false);
			statement = DBManager.getInstance().getConnection()
					.prepareStatement(insertDestinationInfoIntoDB);
			statement.setString(1, destination.getName());
			statement.setString(2, destination.getDescription());
			statement.setDouble(3, destination.getLocation().getLattitude());
			statement.setDouble(4, destination.getLocation().getLongitude());
			statement.setString(5, destination.getMainPicture());
			statement.setString(6, destination.getAuthorEmail());
			statement.setString(7, destination.getCategory().toString());
			statement.setInt(8, destination.getNumberOfLikes());
			statement.setInt(9, destination.getNumberOfDislikes());
			statement.executeUpdate();

			statement2 = DBManager.getInstance().getConnection()
					.prepareStatement(insertIntoVisitedDestinations);
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

	public synchronized boolean updateDestinationInDB(String name, String description, double lattitude,
			double longitude, String mainPicture, ConcurrentSkipListSet<PlaceToSleep> placesToSleep,
			ConcurrentSkipListSet<PlaceToEat> placesToEat, Category category, CopyOnWriteArrayList<String> pictures,
			CopyOnWriteArrayList<String> videos, ConcurrentSkipListSet<Activity> activities,
			ConcurrentSkipListSet<Sight> sights) {
		PreparedStatement prepStatement = null;
		String updateDestinationStatement = "UPDATE destinations SET description=?, longitude=?, lattitude=?, picture=?  WHERE name=?;";
		try {
			// TODO update only current fields

			prepStatement = DBManager.getInstance().getConnection()
					.prepareStatement(updateDestinationStatement);
			prepStatement.setString(1, description);
			prepStatement.setDouble(2, longitude);
			prepStatement.setDouble(3, lattitude);
			prepStatement.setString(4, mainPicture);
			prepStatement.setString(5, name);
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

	public ConcurrentHashMap<String, ArrayList<String>> getDestinationPictures() {
		ConcurrentHashMap<String, ArrayList<String>> newCol = new ConcurrentHashMap<>();
		newCol.putAll(destinationPictures);
		return newCol;
	}

	public void removeDestination(String destinationName) {
		// TODO remove from 2 tables

	}

	public boolean addLike(String userEmail, String destinationName) {
		// TODO update like (userLiker and number Of likes; +/- userDisliker and
		// number of dislikes)
		try {
			PreparedStatement ps = DBManager.getInstance().getConnection()
					.prepareStatement("INSERT INTO destination_likes(user_email, destination_name) VALUES (?,?);");
			ps.setString(1, userEmail);
			ps.setString(2, destinationName);
			ps.executeUpdate();
		} catch (SQLException | CannotConnectToDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean addDislike(String userEmail, String destinationName) {
		try {
			PreparedStatement ps = DBManager.getInstance().getConnection()
					.prepareStatement("INSERT INTO destination_dislikes(user_email, destination_name) VALUES (?,?);");
			ps.setString(1, userEmail);
			ps.setString(2, destinationName);
			ps.executeUpdate();
		} catch (SQLException | CannotConnectToDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean removeLike(String userEmail, String destinationName) {
		// TODO update dislike (userDisliker and number Of dislikes; +/-
		// userLiker and number of likes)
		try {
			PreparedStatement ps = DBManager.getInstance().getConnection()
					.prepareStatement("DELETE FROM destination_likes WHERE user_email=? AND destination_name=?;");
			ps.setString(1, userEmail);
			ps.setString(2, destinationName);
			ps.executeUpdate();
		} catch (SQLException | CannotConnectToDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean removeDislike(String userEmail, String destinationName) {
		// TODO update dislike (userDisliker and number Of dislikes; +/-
		// userLiker and number of likes)
		try {
			PreparedStatement ps = DBManager.getInstance().getConnection()
					.prepareStatement("DELETE FROM destination_dislikes WHERE user_email=? AND destination_name=?;");
			ps.setString(1, userEmail);
			ps.setString(2, destinationName);
			ps.executeUpdate();
		} catch (SQLException | CannotConnectToDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public ConcurrentHashMap<String, ArrayList<String>> getLikes() {
		ConcurrentHashMap<String, ArrayList<String>> userLikes = new ConcurrentHashMap<String, ArrayList<String>>();
		Statement st = null;
		ResultSet rs = null;
		try {
			st = DBManager.getInstance().getConnection().createStatement();
			rs = st.executeQuery("SELECT user_email, destination_name from destination_likes;");
			while (rs.next()) {
				if (!userLikes.containsKey(rs.getString("destination_name"))) {
					userLikes.put(rs.getString("destination_name"), new ArrayList<String>());
				}
				userLikes.get(rs.getString("destination_name")).add("user_email");
			}
		} catch (SQLException | CannotConnectToDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				st.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return userLikes;
	}

	public ConcurrentHashMap<String, ArrayList<String>> getDisLikes() {
		ConcurrentHashMap<String, ArrayList<String>> userDisLikes = new ConcurrentHashMap<String, ArrayList<String>>();
		Statement st = null;
		ResultSet rs = null;
		try {
			st = DBManager.getInstance().getConnection().createStatement();
			rs = st.executeQuery("SELECT user_email, destination_name from destination_dislikes;");
			while (rs.next()) {
				if (!userDisLikes.containsKey(rs.getString("destination_name"))) {
					userDisLikes.put(rs.getString("destination_name"), new ArrayList<String>());
				}
				userDisLikes.get(rs.getString("destination_name")).add("user_email");
			}
		} catch (SQLException | CannotConnectToDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				st.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return userDisLikes;
	}

	public ConcurrentHashMap<String, ArrayList<String>> getVideos() {
		ConcurrentHashMap<String, ArrayList<String>> videos = new ConcurrentHashMap<String, ArrayList<String>>();
		Statement st = null;
		ResultSet rs = null;
		try {
			st = DBManager.getInstance().getConnection().createStatement();
			rs = st.executeQuery("SELECT video, destination_name from destination_videos;");
			while (rs.next()) {
				if (!videos.containsKey(rs.getString("destination_name"))) {
					videos.put(rs.getString("destination_name"), new ArrayList<String>());
				}
				videos.get(rs.getString("destination_name")).add("video");
			}
		} catch (SQLException | CannotConnectToDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				st.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return videos;
	}

	public void addPicture(String destName, String pic) {
		try {
			PreparedStatement ps = DBManager.getInstance().getConnection()
					.prepareStatement("INSERT INTO destination_pictures(destination_name, picture) VALUES(?, ?);");
			ps.setString(1, destName);
			ps.setString(2, pic);
			ps.executeUpdate();
		} catch (SQLException | CannotConnectToDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void addVideo(String destName, String video) {
		try {
			PreparedStatement ps = DBManager.getInstance().getConnection()
					.prepareStatement("INSERT INTO destination_videos(video, destination_name) VALUES(?, ?);");
			ps.setString(1, video);
			ps.setString(2, destName);
			ps.executeUpdate();
		} catch (SQLException | CannotConnectToDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized ConcurrentHashMap<String, CopyOnWriteArrayList<String>> getAllDestinationLikers() {
		ConcurrentHashMap<String, CopyOnWriteArrayList<String>> allDestinationLikers = new ConcurrentHashMap<>(); // destination
																													// name->
																													// user
																													// likers
		String selectAllDestinationLikersFromDB = "SELECT user_email, destination_name FROM destination_likes ORDER BY destination_name;";
		Statement statement = null;
		ResultSet result = null;
		try {
			statement = DBManager.getInstance().getConnection().createStatement();
			result = statement.executeQuery(selectAllDestinationLikersFromDB);
			while (result.next()) {
				if (!(allDestinationLikers.containsKey(result.getString("destination_name")))) {
					allDestinationLikers.put(result.getString("destination_name"), new CopyOnWriteArrayList<>());
				}
				allDestinationLikers.get(result.getString("destination_name")).add(result.getString("user_email"));
			}
		} catch (SQLException e) {
			// TODO handle exception
			e.printStackTrace();
		} catch (CannotConnectToDBException e) {
			// TODO handle exception
			e.printStackTrace();
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
		return allDestinationLikers;
	}

	public synchronized ConcurrentHashMap<String, CopyOnWriteArrayList<String>> getAllDestinationDislikers() {
		ConcurrentHashMap<String, CopyOnWriteArrayList<String>> allDestinationDislikers = new ConcurrentHashMap<>(); // destination
																														// name->
																														// user
																														// dislikers
		String selectAllDestinationDislikersFromDB = "SELECT user_email, destination_name FROM destination_dislikes ORDER BY destination_name;";
		Statement statement = null;
		ResultSet result = null;
		try {
			statement = DBManager.getInstance().getConnection().createStatement();
			result = statement.executeQuery(selectAllDestinationDislikersFromDB);
			while (result.next()) {
				if (!(allDestinationDislikers.containsKey(result.getString("destination_name")))) {
					allDestinationDislikers.put(result.getString("destination_name"), new CopyOnWriteArrayList<>());
				}
				allDestinationDislikers.get(result.getString("destination_name")).add(result.getString("user_email"));
			}
		} catch (SQLException e) {
			// TODO handle exception
			e.printStackTrace();
		} catch (CannotConnectToDBException e) {
			// TODO handle exception
			e.printStackTrace();
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
		return allDestinationDislikers;
	}

	public synchronized ConcurrentHashMap<String, ConcurrentSkipListSet<Activity>> getAllDestinationActivities()
			throws InvalidDataException, InvalidLocationException {
		ConcurrentHashMap<String, ConcurrentSkipListSet<Activity>> allDestinationActivities = new ConcurrentHashMap<>(); // destination
																															// name
																															// ->
																															// activities
		String selectAllDestinationActivitiesFromDB = "SELECT name, lattitude, longitude, description, picture, author_rating, price, place_name FROM activities ORDER BY place_name;";
		Statement statement = null;
		ResultSet result = null;
		try {
			statement = DBManager.getInstance().getConnection().createStatement();
			result = statement.executeQuery(selectAllDestinationActivitiesFromDB);
			while (result.next()) {
				if (!(allDestinationActivities.containsKey(result.getString("place_name")))) {
					allDestinationActivities.put(result.getString("place_name"), new ConcurrentSkipListSet<>());
				}
				Activity activity = new Activity(result.getString("name"), result.getDouble("lattitude"),
						result.getDouble("longitude"), result.getString("description"), result.getString("picture"),
						result.getDouble("author_rating"), result.getDouble("price"), result.getString("place_name"));
				allDestinationActivities.get(result.getString("place_name")).add(activity);
			}
		} catch (SQLException e) {
			// TODO handle exception
			e.printStackTrace();
		} catch (CannotConnectToDBException e) {
			// TODO handle exception
			e.printStackTrace();
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
		return allDestinationActivities;
	}

	public synchronized ConcurrentHashMap<String, ConcurrentSkipListSet<Sight>> getAllDestinationSights()
			throws InvalidDataException, InvalidLocationException {
		ConcurrentHashMap<String, ConcurrentSkipListSet<Sight>> allDestinationSights = new ConcurrentHashMap<>(); // destination
																													// name
																													// ->
																													// sights
		String selectAllDestinationSightsFromDB = "SELECT name, lattitude, longitude, description, picture, author_rating, place_name FROM sights ORDER BY place_name;";
		Statement statement = null;
		ResultSet result = null;
		try {
			statement = DBManager.getInstance().getConnection().createStatement();
			result = statement.executeQuery(selectAllDestinationSightsFromDB);
			while (result.next()) {
				if (!(allDestinationSights.containsKey(result.getString("place_name")))) {
					allDestinationSights.put(result.getString("place_name"), new ConcurrentSkipListSet<>());
				}
				Sight sight = new Sight(result.getString("name"), result.getDouble("lattitude"),
						result.getDouble("longitude"), result.getString("description"), result.getString("picture"),
						result.getDouble("author_rating"), result.getString("place_name"));
				allDestinationSights.get(result.getString("place_name")).add(sight);
			}
		} catch (SQLException e) {
			// TODO handle exception
			e.printStackTrace();
		} catch (CannotConnectToDBException e) {
			// TODO handle exception
			e.printStackTrace();
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
		return allDestinationSights;
	}

}
