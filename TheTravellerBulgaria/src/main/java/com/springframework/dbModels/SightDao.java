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
import com.springframework.exceptions.InvalidDataException;
import com.springframework.exceptions.InvalidLocationException;
import com.springframework.model.Sight;

@Component
@Scope("Singleton")
public class SightDao {

	private SightDao() {
	}

	public Set<Sight> getAllSights() {
		
		Set<Sight> sights = new HashSet<>();
		Statement st = null;
		ResultSet rs = null;

		try {
			st = SpringContextProvider.getContext().getBean(DBManager.class).getConnection().createStatement();
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
			ps = SpringContextProvider.getContext().getBean(DBManager.class).getConnection().prepareStatement(
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
