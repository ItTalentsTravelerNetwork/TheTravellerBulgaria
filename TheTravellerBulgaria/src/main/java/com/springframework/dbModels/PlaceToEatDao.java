package com.springframework.dbModels;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import com.springframework.exceptions.CannotConnectToDBException;
import com.springframework.model.PlaceToEat;


public class PlaceToEatDao {
	
	private static PlaceToEatDao instance;
	
	public static PlaceToEatDao getInstance() {
		if (instance == null) {
			instance = new PlaceToEatDao();
		}
		return instance;
	}

	private PlaceToEatDao() {
	}

	public Set<PlaceToEat> getAllPlacesToEat() {
		Set<PlaceToEat> resturants = new HashSet<>();
		Statement st = null;
		ResultSet rs = null;

		try {
			st = DBManager.getInstance().getConnection().createStatement();
			rs = st.executeQuery(
					"SELECT name, lattitude, longitude, description, picture, author_rating, place_name from places_to_eat");
			while (rs.next()) {
				PlaceToEat pte = new PlaceToEat(rs.getString("name"), rs.getDouble("lattitude"),
						rs.getDouble("longitude"), rs.getString("description"), rs.getString("picture"),
						rs.getDouble("author_rating"), rs.getString("place_name"));
				if (pte != null) {
					resturants.add(pte);
				}
			}
		} catch (SQLException | CannotConnectToDBException e) {
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

		return resturants;
	}

	public synchronized void savePlaceToEatInDB(PlaceToEat placeToEat) throws CloneNotSupportedException {
		PreparedStatement ps = null;
		try {
			ps = DBManager.getInstance().getConnection().prepareStatement(
					"INSERT INTO places_to_eat(name, lattitude, longitude, description, picture, author_rating, place_name) VALUES(?,?,?,?,?,?,?);");
			ps.setString(1, placeToEat.getName());
			ps.setDouble(2, placeToEat.getLocation().getLattitude());
			ps.setDouble(3, placeToEat.getLocation().getLongitude());
			ps.setString(4, placeToEat.getDescription());
			ps.setString(5, placeToEat.getPicture());
			ps.setDouble(6, placeToEat.getAuthorRating());
			ps.setString(7, placeToEat.getDestinationName());

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
