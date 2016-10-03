package com.springframework.dbModels;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.springframework.exceptions.CannotConnectToDBException;
import com.springframework.exceptions.InvalidDataException;
import com.springframework.model.Comment;

public class CommentDao {

	private static CommentDao instance; // Singleton

	private CommentDao() {
	}

	public static synchronized CommentDao getInstance() {
		if (instance == null) {
			instance = new CommentDao();
		}
		return instance;
	}

	public synchronized ArrayList<Comment> getAllComments() {
		System.out.println("Getting all comments from DB!!!!");
		ArrayList<Comment> comments = new ArrayList<>();
		Statement statement = null;
		ResultSet result = null;
		try {
			try {
				statement = DBManager.getInstance().getConnection().createStatement();
				String selectAllCommentsFromDB = "SELECT id, author_email, place_name, text, number_of_likes, date_and_time, video FROM comments;";
				result = statement.executeQuery(selectAllCommentsFromDB);
				while (result.next()) {
					try {
						Comment comment = new Comment(result.getLong("id"), result.getString("author_email"),
								result.getString("place_name"), result.getString("text"),
								result.getInt("number_of_likes"), result.getString("date_and_time"),
								result.getString("video"));
						comments.add(comment);
					} catch (InvalidDataException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.out.println("All comments returned from DB.");
				// TODO add destinations to each user (form DB)
			} catch (CannotConnectToDBException e) {
				// TODO handle exception - write to log and userFriendly screen
				e.getMessage();
				System.out.println("NO comments returned!!!!!");
				return comments;
			}
		} catch (SQLException e) {
			// TODO write in the log
			System.out.println("NO comments returned!!!!!");
			return comments;
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
		return comments;
	}

	public synchronized boolean saveCommentToDB(Comment comment) {
		String insertCommentIntoDB = "INSERT INTO comments (author_email, place_name, text, number_of_likes, date_and_time, video) VALUES (?, ?, ?, ?, ?, ?);";
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			statement = DBManager.getInstance().getConnection().prepareStatement(insertCommentIntoDB,
					Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, comment.getAuthorEmail());
			statement.setString(2, comment.getPlaceName());
			statement.setString(3, comment.getText());
			statement.setInt(4, comment.getNumberOfLikes());
			statement.setString(5, comment.getDateAndTimeToString());
			statement.setString(6, comment.getVideo());
			statement.executeUpdate(); // add comment to DB
			result = statement.getGeneratedKeys(); // get comment id
			result.next();
			comment.setId(result.getLong(1)); // add id to comment
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (CannotConnectToDBException e) {
			// TODO handle exception - write to log and userFriendly screen
			e.getMessage();
			return false;
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
		return true;
	}

	public synchronized void addLike(long commentId, String userEmail) {
		try {
			PreparedStatement ps = DBManager.getInstance().getConnection()
					.prepareStatement("INSERT INTO comment_likes(commenter_email, comment_id) VALUES (?,?)");
			ps.setString(1, userEmail);
			ps.setLong(2, commentId);
			ps.executeUpdate();
		} catch (SQLException | CannotConnectToDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public synchronized void removeLike(long commentId, String userEmail) {
		try {
			PreparedStatement ps = DBManager.getInstance().getConnection()
					.prepareStatement("DELETE FROM comment_likes WHERE commenter_email=? AND comment_id=?");
			ps.setString(1, userEmail);
			ps.setLong(2, commentId);
			ps.executeUpdate();
		} catch (SQLException | CannotConnectToDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void deleteComment(Comment comment) {
		try {
			PreparedStatement ps = DBManager.getInstance().getConnection()
					.prepareStatement("DELETE FROM comments WHERE comment_id=?");
			ps.setLong(1, comment.getId());
			ps.executeUpdate();
		} catch (SQLException | CannotConnectToDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
