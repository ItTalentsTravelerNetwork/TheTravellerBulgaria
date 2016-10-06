package com.springframework.dbModels;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.springframework.SpringContextProvider;
import com.springframework.exceptions.CannotConnectToDBException;
import com.springframework.model.PlaceToSleep;

@Component
@Scope("Singleton")
public class HotelDao {

	private HotelDao() {
	}


	public Set<PlaceToSleep> getAllHotels() {
		Set<PlaceToSleep> hotels = new HashSet<>();
		Statement st = null;
		ResultSet rs = null;

		try {
			st = SpringContextProvider.getContext().getBean(DBManager.class).getConnection().createStatement();
			rs = st.executeQuery(
					"SELECT name, lattitude, longitude, description, picture, author_rating, type, contact, price, place_name from places_to_sleep");
			while (rs.next()) {
				PlaceToSleep hotel = new PlaceToSleep(rs.getString("name"), rs.getDouble("lattitude"),
						rs.getDouble("longitude"), rs.getString("description"), rs.getString("picture"),
						rs.getDouble("author_rating"), PlaceToSleep.Type.valueOf(rs.getString("type")),
						rs.getString("contact"), rs.getDouble("price"), rs.getString("place_name"));
				if (hotel != null) {
					hotels.add(hotel);
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
		return hotels;
	}

	public synchronized void saveHotelInDB(PlaceToSleep hotel) throws CloneNotSupportedException {
		PreparedStatement ps = null;
		try {
			ps = SpringContextProvider.getContext().getBean(DBManager.class).getConnection().prepareStatement(
					"INSERT INTO places_to_sleep(name, lattitude, longitude, description, picture, author_rating, type, contact, price, place_name) VALUES(?,?,?,?,?,?,?,?,?,?);");
			ps.setString(1, hotel.getName());
			ps.setDouble(2, hotel.getLocation().getLattitude());
			ps.setDouble(3, hotel.getLocation().getLongitude());
			ps.setString(4, hotel.getDescription());
			ps.setString(5, hotel.getPicture());
			ps.setDouble(6, hotel.getAuthorRating());
			ps.setString(7, hotel.getType().name());
			ps.setString(8, hotel.getContact());
			ps.setDouble(9, hotel.getPrice());
			ps.setString(10, hotel.getDestinationName());

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
