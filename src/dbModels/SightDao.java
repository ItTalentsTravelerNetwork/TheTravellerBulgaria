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
import models.Sight;

public class SightDao {
	private static SightDao instance;

	private SightDao() {
	}

	public static SightDao getInstance() {
		if (instance == null) {
			instance = new SightDao();
		}
		return instance;
	}

	public Set<Sight> getAllSights() {
		Set<Sight> sights = new HashSet<>();
		Statement st = null;
		ResultSet rs = null;

		try {
			st = DBManager.getInstance().getConnection().createStatement();
			rs = st.executeQuery("SELECT * from sights");
			while (rs.next()) {
				Sight sight = new Sight(rs.getString(2), rs.getDouble(3), rs.getDouble(4), rs.getString(5),
						rs.getString(6), rs.getDouble(7), rs.getString(8));
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

	public synchronized void saveActivityToDb(Sight sight) throws CloneNotSupportedException {
		PreparedStatement ps = null;
		try {
			ps = DBManager.getInstance().getConnection().prepareStatement(
					"INSERT INTO places_to_sleep(name, lattitude, longitude, description, picture, author_rating, place_name) VALUES(?,?,?,?,?,?,?);");
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
