package com.springframework.functionality;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
		CommentDao.getInstance().saveCommentToDB(comment); // saves comment
															// to
															// DB
		DestinationsManager.getInstance().getDestinationFromCache(comment.getPlaceName()).addComment(comment);
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

	public synchronized void likeComment(long commentId, String userEmail) {
		for (Comment comment : allComments) {
			if (comment.getId() == commentId) {
				if (!comment.getUserLikers().contains(comment)) {
					comment.like(userEmail);
					CommentDao.getInstance().addLike(commentId, userEmail);
					return;
				}
			}
		}
	}

	public synchronized void removeLikeComment(long commentId, String userEmail) {
		for (Comment comment : allComments) {
			if (comment.getId() == commentId) {
				if (comment.getUserLikers().contains(comment)) {
					comment.unlike(userEmail);
					CommentDao.getInstance().removeLike(commentId, userEmail);
					return;
				}
			}
		}
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

}
