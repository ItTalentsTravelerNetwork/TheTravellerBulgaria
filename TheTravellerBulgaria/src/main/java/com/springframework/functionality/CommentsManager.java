package com.springframework.functionality;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.springframework.SpringContextProvider;
import com.springframework.dbModels.CommentDao;

import com.springframework.model.Comment;

@Component
@Scope("Singleton")
public class CommentsManager {
	private static final DateTimeFormatter DATE_AND_TIME_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	private static CommentsManager instance; // Singleton
	private CopyOnWriteArrayList<Comment> allComments; // all cached comments

	private CommentsManager() {
		allComments = new CopyOnWriteArrayList<>();
		for (Comment c : SpringContextProvider.getContext().getBean(CommentDao.class).getAllComments()) { // adds
																		// all
																		// comments
																		// from
																		// DB to
																		// collection
			allComments.add(c);
		}
		fillUserLikersToComments(); // fills all users who like the comments
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
		SpringContextProvider.getContext().getBean(CommentDao.class).saveCommentToDB(comment); // saves comment
															// to
															// DB
		SpringContextProvider.getContext().getBean(DestinationsManager.class).getDestinationFromCache(comment.getPlaceName()).addComment(comment);
	}

	public synchronized void deleteComment(Comment comment) {
		if (comment != null && allComments.contains(comment)) {
			allComments.remove(comment);
			SpringContextProvider.getContext().getBean(CommentDao.class).deleteComment(comment);
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
					SpringContextProvider.getContext().getBean(CommentDao.class).addLike(commentId, userEmail);
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
					SpringContextProvider.getContext().getBean(CommentDao.class).removeLike(commentId, userEmail);
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
	
	private synchronized void fillUserLikersToComments() {
		if (SpringContextProvider.getContext().getBean(CommentDao.class).getAllCommentUserLikersFromDB() != null) {
			for (Map.Entry<Long, CopyOnWriteArrayList<String>> entry : SpringContextProvider.getContext()
					.getBean(CommentDao.class).getAllCommentUserLikersFromDB().entrySet()) { // for each 
																							//(comment id->list of user likers)
				if (getCommentById(entry.getKey())!=null) { // if there is such a comment in cache
					getCommentById(entry.getKey()).setUserLikers(entry.getValue()); // add user likers to comment
				}																		
			}
		}
	}
	
	public synchronized Comment getCommentById(long commentId) {
		for (Comment comment: allComments) {
			if (comment.getId()==commentId) {
				return comment;
			}
		}
		return null;
	}

}
