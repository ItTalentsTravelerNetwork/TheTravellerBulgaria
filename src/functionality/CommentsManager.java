package functionality;

import java.time.format.DateTimeFormatter;
import java.util.concurrent.CopyOnWriteArrayList;

import dbModels.CommentDao;
import models.Comment;

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

}
