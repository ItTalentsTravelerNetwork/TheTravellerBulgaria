package com.springframework.dbModels;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.springframework.SpringContextProvider;
import com.springframework.exceptions.CannotConnectToDBException;
import com.springframework.exceptions.InvalidDataException;
import com.springframework.exceptions.InvalidLocationException;
import com.springframework.model.Activity;

@Component
public class ActivityDao {

	public ActivityDao() {
	}

	public Set<Activity> getAllActivities() {
		Set<Activity> activities = new HashSet<>();
		Statement st = null;
		ResultSet rs = null;

		try {
			st = SpringContextProvider.context.getBean(DBManager.class).getConnection().createStatement();
			rs = st.executeQuery(
					"SELECT name, lattitude, longitude, description, picture, author_rating, price, place_name from activities");
			while (rs.next()) {
				Activity act = new Activity(rs.getString("name"), rs.getDouble("lattitude"), rs.getDouble("longitude"),
						rs.getString("description"), rs.getString("picture"), rs.getDouble("author_rating"),
						rs.getDouble("price"), rs.getString("place_name"));
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
			ps = SpringContextProvider.context.getBean(DBManager.class).getConnection().prepareStatement(
					"INSERT INTO activities(name, lattitude, longitude, description, picture, author_rating, price, place_name) VALUES(?,?,?,?,?,?,?,?);");
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
