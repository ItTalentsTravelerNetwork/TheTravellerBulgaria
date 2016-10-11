package com.springframework.functionality;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.springframework.dbModels.CommentDao;
import com.springframework.model.Comment;

public class CommentsManager {
	private static final DateTimeFormatter DATE_AND_TIME_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	private static CommentsManager instance; // Singleton
	private CopyOnWriteArrayList<Comment> allComments; // all cached comments

	private CommentsManager() {
		allComments = new CopyOnWriteArrayList<>();

		for (Comment c : CommentDao.getInstance().getAllComments()) { // adds
			// all
			// comments
			// from
			// DB to

			// collection
			allComments.add(c);
		}
		fillUserLikersToComments(); // fills all users who like the comments
		fillUserDislikersToComments(); // fills all users who dislike the comments
	} 

	public static synchronized CommentsManager getInstance() {
		if (instance == null) {
			instance = new CommentsManager();
		}
		return instance;
	}

	public synchronized void saveComment(Comment comment) {
		allComments.add(comment); // adds the new
									// comment to
									// the
									// collection
		CommentDao.getInstance().saveCommentToDB(comment); // saves
															// comment
		// to
		// DB

	}

	public synchronized void deleteComment(Comment comment) {
		if (comment != null && allComments.contains(comment)) {
			allComments.remove(comment);
			CommentDao.getInstance().deleteComment(comment);
		}
	}

	public synchronized CopyOnWriteArrayList<Comment> getAllComments() {
		CopyOnWriteArrayList<Comment> copy = new CopyOnWriteArrayList<>();
		copy.addAll(allComments);
		return copy;
	}

	public synchronized boolean likeComment(long commentId, String userEmail) {
		Comment comment = getCommentById(commentId);
		int result = comment.like(userEmail);
		if (result==1) {
			CommentDao.getInstance().addLike(commentId, userEmail);
			return true;
		}
		else {
			if (result==2) {
				CommentDao.getInstance().addLike(commentId, userEmail);
				CommentDao.getInstance().removeDislike(commentId, userEmail);
				return true;
			}
		}	
		return false; // incorrect data or already liked
	}

	public synchronized boolean unlikeComment(long commentId, String userEmail) {
		Comment comment = getCommentById(commentId);
		if (comment.unlike(userEmail)) {
			CommentDao.getInstance().removeLike(commentId, userEmail);
			return true;
		}
		return false; // incorrect data or not liked
	}
	
	public synchronized boolean dislikeComment(long commentId, String userEmail) {
		Comment comment = getCommentById(commentId);
		int result = comment.dislike(userEmail);
		if (result==1) {
			CommentDao.getInstance().addDislike(commentId, userEmail);
			return true;
		}
		else {
			if (result==2) {
				CommentDao.getInstance().addDislike(commentId, userEmail);
				CommentDao.getInstance().removeLike(commentId, userEmail);
				return true;
			}
		}
		return false; // incorrect data or already disliked
	}

	public synchronized boolean undislikeComment(long commentId, String userEmail) {
		Comment comment = getCommentById(commentId);
		if (comment.undislike(userEmail)) {
			CommentDao.getInstance().removeDislike(commentId, userEmail);
			return true;
		}
		return false; // incorrect data or not disliked
	}

	public synchronized void deleteUserComments(String userEmail) {
		ArrayList<Comment> commentsToRemove = new ArrayList<>();
		for (Comment comment : allComments) {
			if (comment.getAuthorEmail().equals(userEmail)) {
				commentsToRemove.add(comment);
			}
		}
		for (Comment comment : commentsToRemove) {
			this.allComments.remove(comment);
		}
	}

	private synchronized void fillUserLikersToComments() {
		if (CommentDao.getInstance().getAllCommentUserLikersFromDB() != null) {
			for (Map.Entry<Long, CopyOnWriteArrayList<String>> entry : CommentDao.getInstance()
					.getAllCommentUserLikersFromDB().entrySet()) { // for
																	// each
																	// (comment
																	// id->list
																	// of
																	// user
																	// likers)
				if (getCommentById(entry.getKey()) != null) { // if there is
																// such a
																// comment in
																// cache
					getCommentById(entry.getKey()).setUserLikers(entry.getValue()); // add
																					// user
																					// likers
																					// to
																					// comment
				}
			}
		}
	}
	
	private synchronized void fillUserDislikersToComments() {
		if (CommentDao.getInstance().getAllCommentUserDislikersFromDB() != null) {
			for (Map.Entry<Long, CopyOnWriteArrayList<String>> entry : CommentDao.getInstance()
					.getAllCommentUserDislikersFromDB().entrySet()) { // for
																	// each
																	// (comment
																	// id->list
																	// of
																	// user
																	// dislikers)
				if (getCommentById(entry.getKey()) != null) { // if there is
																// such a
																// comment in
																// cache
					getCommentById(entry.getKey()).setUserDislikers(entry.getValue()); // add
																					// user
																					// dislikers
																					// to
																					// comment
				}
			}
		}
	}

	public synchronized Comment getCommentById(long commentId) {
		for (Comment comment : allComments) {
			if (comment.getId() == commentId) {
				return comment;
			}
		}
		return null;
	}
	
	public synchronized String showCommentStatus(long commentId, String userEmail) {
		if (allComments.contains(getCommentById(commentId))) {
			Comment comment = getCommentById(commentId);
			if (comment.getUserLikers().contains(userEmail)) {
				return "Comment already liked!";
			}
			else {
				if (comment.getUserDislikers().contains(userEmail)) {
					return "Comment already disliked!";
				}
				else {
					return "Comment not liked nor disliked!";
				}
			}
		}
		return "No such comment!";
	}

}
