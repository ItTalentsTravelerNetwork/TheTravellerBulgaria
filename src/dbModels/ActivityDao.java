package dbModels;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import exceptions.CannotConnectToDBException;
import exceptions.InvalidDataException;
import exceptions.InvalidLocationException;
import models.Activity;

public class ActivityDao {

	private static ActivityDao instance;

	private ActivityDao() {
	}

	public static ActivityDao getInstance() {
		if (instance == null) {
			instance = new ActivityDao();
		}
		return instance;
	}

	public Set<Activity> getAllActivities() {
		Set<Activity> activities = new HashSet<>();
		Statement st = null;
		ResultSet rs = null;

		try {
			st = DBManager.getInstance().getConnection().createStatement();
			rs = st.executeQuery("SELECT * from activities");
			while (rs.next()) {
				Activity act = new Activity(rs.getString(2), rs.getDouble(8), rs.getDouble(3), rs.getDouble(4),
						rs.getString(5), rs.getString(6), rs.getDouble(7), rs.getString(9));
				if (act != null) {
					activities.add(act);
				}
			}
		} catch (SQLException | CannotConnectToDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				st.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return activities;
	}

	public synchronized void saveActivityToDb(Activity activity) throws CloneNotSupportedException {
		PreparedStatement ps = null;
		try {
			ps = DBManager.getInstance().getConnection().prepareStatement(
					"INSERT INTO activities(name, lattitude, longitude, description, picture, author_rating, price place_name) VALUES(?,?,?,?,?,?,?,?);");
			ps.setString(1, activity.getName());
			ps.setDouble(2, activity.getLocation().getLattitude());
			ps.setDouble(3, activity.getLocation().getLongitude());
			ps.setString(4, activity.getDescription());
			ps.setString(5, activity.getPicture());
			ps.setDouble(6, activity.getAuthorRating());
			ps.setDouble(7, activity.getPrice());
			ps.setString(8, activity.getDestinationName());
			ps.executeUpdate();

		} catch (SQLException | CannotConnectToDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
