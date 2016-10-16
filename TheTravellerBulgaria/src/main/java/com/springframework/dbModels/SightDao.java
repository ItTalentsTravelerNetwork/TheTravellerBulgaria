package com.springframework.dbModels;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import com.springframework.exceptions.CannotConnectToDBException;
import com.springframework.exceptions.InvalidDataException;
import com.springframework.exceptions.InvalidLocationException;
import com.springframework.model.Sight;

public class SightDao {

	private static SightDao instance;

	public static SightDao getInstance() {
		if (instance == null) {
			instance = new SightDao();
		}
		return instance;
	}

	private SightDao() {
	}

	public synchronized Set<Sight> getAllSights() {

		Set<Sight> sights = new HashSet<>();
		Statement st = null;
		ResultSet rs = null;

		try {
			st = DBManager.getInstance().getConnection().createStatement();
			rs = st.executeQuery(
					"SELECT name, lattitude, longitude, description, picture, author_rating, place_name from sights");
			while (rs.next()) {
				Sight sight = new Sight(rs.getString("name"), rs.getDouble("lattitude"), rs.getDouble("longitude"),
						rs.getString("description"), rs.getString("picture"), rs.getDouble("author_rating"),
						rs.getString("place_name"));
				sights.add(sight);
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

		return sights;
	}

	public synchronized void saveSightToDB(Sight sight) throws CloneNotSupportedException {
		PreparedStatement ps = null;
		try {
			ps = DBManager.getInstance().getConnection().prepareStatement(
					"INSERT INTO sights(name, lattitude, longitude, description, picture, author_rating, place_name) VALUES(?,?,?,?,?,?,?);");
			ps.setString(1, sight.getName());
			ps.setDouble(2, sight.getLocation().getLattitude());
			ps.setDouble(3, sight.getLocation().getLongitude());
			ps.setString(4, sight.getDescription());
			ps.setString(5, sight.getPicture());
			ps.setDouble(6, sight.getAuthorRating());
			ps.setString(7, sight.getDestinationName());

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
