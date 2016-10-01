package dbModels;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import exceptions.CannotConnectToDBException;
import models.PlaceToEat;

public class PlaceToEatDao {
	private static PlaceToEatDao instance;

	private PlaceToEatDao() {
	}

	public static PlaceToEatDao getInstance() {
		if (instance == null) {
			instance = new PlaceToEatDao();
		}
		return instance;
	}

	public Set<PlaceToEat> getAllPlacesToEat() {
		Set<PlaceToEat> resturants = new HashSet<>();
		Statement st = null;
		ResultSet rs = null;

		try {
			st = DBManager.getInstance().getConnection().createStatement();
			rs = st.executeQuery("SELECT * from places_to_eat");
			while (rs.next()) {
				PlaceToEat pte = new PlaceToEat(rs.getString(2), rs.getDouble(3), rs.getDouble(4), rs.getString(5),
						rs.getString(6), rs.getDouble(7), rs.getString(8));
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

	public synchronized void saveActivityToDb(PlaceToEat placeToEat) throws CloneNotSupportedException {
		PreparedStatement ps = null;
		try {
			ps = DBManager.getInstance().getConnection().prepareStatement(
					"INSERT INTO places_to_sleep(name, lattitude, longitude, description, picture, author_rating, place_name) VALUES(?,?,?,?,?,?,?);");
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
