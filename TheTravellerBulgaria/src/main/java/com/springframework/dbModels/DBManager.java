package com.springframework.dbModels;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Component;

import com.springframework.exceptions.CannotConnectToDBException;


public class DBManager {

	private static final String DB_IP = "localhost";
	private static final String DB_PORT = "3306";
	private static final String DB_NAME = "traveler_db";
	private static final String DB_USERNAME = "root";
	private static final String DB_PASSWORD = "123456";
	private static final String commonURL = "jdbc:mysql://" + DB_IP + ":" + DB_PORT + "/";
	private static final String URL = "jdbc:mysql://" + DB_IP + ":" + DB_PORT + "/" + DB_NAME;

	private Connection connection;
	
	private static DBManager instance;
	
	public static DBManager getInstance() {
		if (instance == null) {
			instance = new DBManager();
		}
		return instance;
	}

	private DBManager() {
		try {
			Class.forName("com.mysql.jdbc.Driver"); // DriverManager
			setConnection(DriverManager.getConnection(commonURL, DB_USERNAME, DB_PASSWORD));
			if (!checkDBExists(DB_NAME)) { // checks if the DB schema exists
				makeDBAndTables(); // creates the DB schema and the tables in it
									// if the schema does not exist
			}
			setConnection(DriverManager.getConnection(URL, DB_USERNAME, DB_PASSWORD)); // establishing
																						// a
																						// connection
		} catch (ClassNotFoundException e) {
			// TODO handle exception
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	Connection getConnection() throws CannotConnectToDBException {
		if (connection == null) {
			System.out.println("getting DBManager conncetion instance = not null!");
			throw new CannotConnectToDBException();
		}
		return connection;
	}

	private boolean checkDBExists(String dbName) {
		ResultSet resultSet = null;
		try {
			resultSet = this.getConnection().getMetaData().getCatalogs(); // retrieving
																			// the
																			// db
																			// schemas'
																			// names

			while (resultSet.next()) {
				String databaseName = resultSet.getString(1);
				if (databaseName.equals(dbName)) {
					return true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CannotConnectToDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

	private void makeDBAndTables() {
		Statement st = null;
		try {
			this.getConnection().setAutoCommit(false); // stop auto committing
			String createSchema = "CREATE DATABASE traveler_db;";
			String useSchema = "USE traveler_db;";

			String createUsersTable = "CREATE TABLE users (\r\n" + "email VARCHAR(64) NOT NULL PRIMARY KEY,\r\n"
					+ "password VARCHAR(64) NOT NULL,\r\n" + "first_name VARCHAR(64) NOT NULL,\r\n"
					+ "last_name VARCHAR(64) NOT NULL,\r\n" + "description VARCHAR(500) NOT NULL,\r\n"
					+ "profile_picture VARCHAR(100) NOT NULL,\r\n" + "rating DOUBLE NOT NULL,\r\n"
					+ "times_liked INT NOT NULL\r\n" + ") ENGINE=InnoDB;\r\n" + "";

			String createDestinationsTable = "CREATE TABLE destinations (\r\n"
					+ "name VARCHAR(64) NOT NULL PRIMARY KEY,\r\n" + "description VARCHAR(500) NOT NULL,\r\n"
					+ "lattitude DOUBLE NOT NULL,\r\n" + "longitude DOUBLE NOT NULL,\r\n"
					+ "main_picture VARCHAR(100) NOT NULL, \r\n" + "author_email VARCHAR(64) NOT NULL,\r\n"
					+ "CONSTRAINT FK_author_email FOREIGN KEY (author_email)\r\n" + "REFERENCES users (email)\r\n"
					+ "ON DELETE CASCADE,\r\n" + "category VARCHAR(50) NOT NULL,\r\n"
					+ "number_of_likes INT NOT NULL,\r\n" + "number_of_dislikes INT NOT NULL,\r\n"
					+ "date_and_time VARCHAR(20) NOT NULL\r\n"
					+ ") ENGINE=InnoDB;\r\n" + "";

			String createVisitedDestinationsTable = "CREATE TABLE visited_destinations (\r\n"
					+ "destination_name VARCHAR(64) NOT NULL,\r\n"
					+ "CONSTRAINT FK_destination_name FOREIGN KEY (destination_name)\r\n"
					+ "REFERENCES destinations (name)\r\n" + "ON DELETE CASCADE,\r\n"
					+ "user_email VARCHAR(64) NOT NULL,\r\n" + "CONSTRAINT FK_user_email FOREIGN KEY (user_email)\r\n"
					+ "REFERENCES users (email)\r\n" + "ON DELETE CASCADE,\r\n"
					+ "date_and_time VARCHAR(20) NOT NULL, \r\n"
					+ "PRIMARY KEY (destination_name, user_email)\r\n" + ") ENGINE=InnoDB;\r\n" + "";

			String createCommentsTable = "CREATE TABLE comments (\r\n"
					+ "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,\r\n" + "author_email VARCHAR(64) NOT NULL,\r\n"
					+ "CONSTRAINT FK_userauthor_email FOREIGN KEY (author_email)\r\n" + "REFERENCES users (email)\r\n"
					+ "ON DELETE CASCADE,\r\n" + "place_name VARCHAR(64) NOT NULL,\r\n"
					+ "CONSTRAINT FK_place_name FOREIGN KEY (place_name)\r\n" + "REFERENCES destinations (name)\r\n"
					+ "ON DELETE CASCADE,\r\n" + "text VARCHAR(500) NOT NULL,\r\n" + "number_of_likes INT NOT NULL,\r\n"
					+ "number_of_dislikes INT NOT NULL,\r\n"
					+ "date_and_time VARCHAR(20) NOT NULL,\r\n" + "video VARCHAR(100)\r\n" + ") ENGINE=InnoDB;\r\n"
					+ "";

			String createCommentLikesTable = "CREATE TABLE comment_likes (\r\n"
					+ "commenter_email VARCHAR(64) NOT NULL,\r\n" + "comment_id INT NOT NULL,\r\n"
					+ "CONSTRAINT FK_commenter_email FOREIGN KEY (commenter_email)\r\n" + "REFERENCES users (email)\r\n"
					+ "ON DELETE CASCADE,\r\n" + "CONSTRAINT FK_comment_id FOREIGN KEY (comment_id)\r\n"
					+ "REFERENCES comments (id)\r\n" + "ON DELETE CASCADE,\r\n"
					+ "PRIMARY KEY (commenter_email, comment_id)\r\n" + ") ENGINE=InnoDB;\r\n" + "";

			String createCommentDislikesTable = "CREATE TABLE comment_dislikes (\r\n"
					+ "commenter_email VARCHAR(64) NOT NULL,\r\n" + "comment_id INT NOT NULL,\r\n"
					+ "CONSTRAINT FK_usercommenter_email FOREIGN KEY (commenter_email)\r\n"
					+ "REFERENCES users (email)\r\n" + "ON DELETE CASCADE,\r\n"
					+ "CONSTRAINT FK_comment_id_number FOREIGN KEY (comment_id)\r\n" + "REFERENCES comments (id)\r\n"
					+ "ON DELETE CASCADE,\r\n" + "PRIMARY KEY (commenter_email, comment_id)\r\n"
					+ ") ENGINE=InnoDB;\r\n" + "";

			String createDestinationLikesTable = "CREATE TABLE destination_likes (\r\n"
					+ "user_email VARCHAR(64) NOT NULL,\r\n" + "destination_name VARCHAR(64) NOT NULL,\r\n"
					+ "CONSTRAINT FK_userliker_email FOREIGN KEY (user_email)\r\n" + "REFERENCES users (email)\r\n"
					+ "ON DELETE CASCADE,\r\n"
					+ "CONSTRAINT FK_liked_destination_name FOREIGN KEY (destination_name)\r\n"
					+ "REFERENCES destinations (name)\r\n" + "ON DELETE CASCADE,\r\n"
					+ "PRIMARY KEY (user_email, destination_name)\r\n" + ") ENGINE=InnoDB;\r\n" + "";

			String createDestinationDislikesTable = "CREATE TABLE destination_dislikes (\r\n"
					+ "user_email VARCHAR(64) NOT NULL,\r\n" + "destination_name VARCHAR(64) NOT NULL,\r\n"
					+ "CONSTRAINT FK_userdisliker_email FOREIGN KEY (user_email)\r\n" + "REFERENCES users (email)\r\n"
					+ "ON DELETE CASCADE,\r\n"
					+ "CONSTRAINT FK_disliked_destination_name FOREIGN KEY (destination_name)\r\n"
					+ "REFERENCES destinations (name)\r\n" + "ON DELETE CASCADE,\r\n"
					+ "PRIMARY KEY (user_email, destination_name)\r\n" + ") ENGINE=InnoDB;\r\n" + "";

			String createFollowersTable = "CREATE TABLE followers (\r\n" + "follower_email VARCHAR(64) NOT NULL,\r\n"
					+ "followed_email VARCHAR(64) NOT NULL,\r\n"
					+ "CONSTRAINT FK_follower_email FOREIGN KEY (follower_email)\r\n" + "REFERENCES users (email)\r\n"
					+ "ON DELETE CASCADE,\r\n" + "CONSTRAINT FK_followed_email FOREIGN KEY (followed_email)\r\n"
					+ "REFERENCES users (email)\r\n" + "ON DELETE CASCADE,\r\n"
					+ "PRIMARY KEY (follower_email, followed_email)\r\n" + ") ENGINE=InnoDB;\r\n" + "";

			String createDestinationsVideosTable = "CREATE TABLE destination_videos (\r\n"
					+ "video VARCHAR(100) NOT NULL,\r\n" + "destination_name VARCHAR(64) NOT NULL,\r\n"
					+ "CONSTRAINT FK_destination_with_video_name FOREIGN KEY (destination_name)\r\n"
					+ "REFERENCES destinations (name)\r\n" + "ON DELETE CASCADE,\r\n" + "PRIMARY KEY (video)\r\n"
					+ ") ENGINE=InnoDB;\r\n" + "";

			String createDestinationsPicturesTable = "CREATE TABLE destination_pictures (\r\n"
					+ "picture VARCHAR(100) NOT NULL,\r\n" + "destination_name VARCHAR(64) NOT NULL,\r\n"
					+ "CONSTRAINT FK_destination_with_picture_name FOREIGN KEY (destination_name)\r\n"
					+ "REFERENCES destinations (name)\r\n" + "ON DELETE CASCADE,\r\n" + "PRIMARY KEY (picture)\r\n"
					+ ") ENGINE=InnoDB;\r\n" + "";

			String createPlacesToEatTable = "CREATE TABLE places_to_eat (\r\n"
					+ "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,\r\n" + "name VARCHAR(64) NOT NULL,\r\n"
					+ "lattitude DOUBLE NOT NULL,\r\n" + "longitude DOUBLE NOT NULL,\r\n"
					+ "description VARCHAR(500) NOT NULL,\r\n" + "picture VARCHAR(100) NOT NULL, \r\n"
					+ "author_rating DOUBLE NOT NULL,\r\n" + "place_name VARCHAR(64) NOT NULL,\r\n"
					+ "CONSTRAINT FK_added_place_name FOREIGN KEY (place_name)\r\n"
					+ "REFERENCES destinations (name)\r\n" + "ON DELETE CASCADE\r\n" + ") ENGINE=InnoDB;\r\n" + "";

			String createActivitiesTable = "CREATE TABLE activities (\r\n"
					+ "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,\r\n" + "name VARCHAR(64) NOT NULL,\r\n"
					+ "lattitude DOUBLE NOT NULL,\r\n" + "longitude DOUBLE NOT NULL,\r\n"
					+ "description VARCHAR(500) NOT NULL,\r\n" + "picture VARCHAR(100) NOT NULL, \r\n"
					+ "author_rating DOUBLE NOT NULL,\r\n" + "price DOUBLE NOT NULL,\r\n"
					+ "place_name VARCHAR(64) NOT NULL,\r\n"
					+ "CONSTRAINT FK_added_user_place_name FOREIGN KEY (place_name)\r\n"
					+ "REFERENCES destinations (name)\r\n" + "ON DELETE CASCADE\r\n" + ") ENGINE=InnoDB;\r\n" + "";

			String createSightsTable = "CREATE TABLE sights (\r\n" + "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,\r\n"
					+ "name VARCHAR(64) NOT NULL,\r\n" + "lattitude DOUBLE NOT NULL,\r\n"
					+ "longitude DOUBLE NOT NULL,\r\n" + "description VARCHAR(500) NOT NULL,\r\n"
					+ "picture VARCHAR(100) NOT NULL, \r\n" + "author_rating DOUBLE NOT NULL,\r\n"
					+ "place_name VARCHAR(64) NOT NULL,\r\n"
					+ "CONSTRAINT FK_user_added_place_name FOREIGN KEY (place_name)\r\n"
					+ "REFERENCES destinations (name)\r\n" + "ON DELETE CASCADE\r\n" + ") ENGINE=InnoDB;\r\n" + "";

			String createPlacesToSleepTable = "CREATE TABLE places_to_sleep (\r\n"
					+ "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,\r\n" + "name VARCHAR(64) NOT NULL,\r\n"
					+ "lattitude DOUBLE NOT NULL,\r\n" + "longitude DOUBLE NOT NULL,\r\n"
					+ "description VARCHAR(500) NOT NULL,\r\n" + "picture VARCHAR(100) NOT NULL, \r\n"
					+ "author_rating DOUBLE NOT NULL,\r\n" + "type VARCHAR(50) NOT NULL,\r\n"
					+ "contact VARCHAR(50) NOT NULL,\r\n" + "price DOUBLE NOT NULL,\r\n"
					+ "place_name VARCHAR(64) NOT NULL,\r\n"
					+ "CONSTRAINT FK_current_place_name FOREIGN KEY (place_name)\r\n"
					+ "REFERENCES destinations (name)\r\n" + "ON DELETE CASCADE\r\n" + ") ENGINE=InnoDB;\r\n" + "";

			st = this.getConnection().createStatement();
			st.executeUpdate(createSchema);
			st.executeUpdate(useSchema);
			st.executeUpdate(createUsersTable);
			st.executeUpdate(createDestinationsTable);
			st.executeUpdate(createVisitedDestinationsTable);
			st.executeUpdate(createCommentsTable);
			st.executeUpdate(createCommentLikesTable);
			st.executeUpdate(createCommentDislikesTable);
			st.executeUpdate(createDestinationLikesTable);
			st.executeUpdate(createDestinationDislikesTable);
			st.executeUpdate(createFollowersTable);
			st.executeUpdate(createDestinationsVideosTable);
			st.executeUpdate(createDestinationsPicturesTable);
			st.executeUpdate(createPlacesToEatTable);
			st.executeUpdate(createActivitiesTable);
			st.executeUpdate(createSightsTable);
			st.executeUpdate(createPlacesToSleepTable);
			this.getConnection().commit(); // commit the two statements
		} catch (CannotConnectToDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			try {
				this.getConnection().rollback(); // if the commit fails
			} catch (SQLException | CannotConnectToDBException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			try {
				this.getConnection().setAutoCommit(true);
				if (st != null) {
					st.close();
				}
			} catch (SQLException | CannotConnectToDBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void setConnection(Connection con) {
		this.connection = con;
	}

}