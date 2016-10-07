package com.springframework.functionality;

import java.util.Map;

import com.springframework.dbModels.DBManager;
import com.springframework.model.Comment;
import com.springframework.model.Destination;
import com.springframework.model.User;


public class ProjectManager {
	
	private static ProjectManager instance;

	public ProjectManager() {
		// Loading all Managers and the cache
		DBManager.getInstance();
		// UsersManager
		UsersManager.getInstance(); // with
																	// visited
																	// destinations
																	// and
																	// followed
																	// users;
																	// no
																	// added
																	// destinations

		// DestinationsManager
		DestinationsManager.getInstance(); // no
																			// comment
																			// objects
																			// in
																			// each
																			// destination

		// CommentsManager
		CommentsManager.getInstance(); // all
																		// comments
																		// +
																		// all
																		// user
																		// likers

		// adding all comments to destinations
		for (Map.Entry<String, Destination> destinationEntry : DestinationsManager.getInstance().getAllDestinations().entrySet()) { // for
																						// each
																						// destination
																						// in
																						// cache
			for (Comment c : CommentsManager.getInstance().getAllComments()) { // for
																												// each
																												// comment
																												// in
																												// cache
				if (c.getPlaceName().equals(destinationEntry.getKey())) { // if
																			// name
																			// of
																			// comments'
																			// place
																			// equals
																			// name
																			// of
																			// destination
																			// from
																			// cache
					destinationEntry.getValue().addComment(c); // add
																// comment
																// to
																// destination's
																// cache
				}
			}
		}

		// adding all destinations to authors (filling added places to users)
		for (Map.Entry<String, Destination> destinationEntry : DestinationsManager.getInstance().getAllDestinations().entrySet()) { // for
																						// each
																						// destination
			for (Map.Entry<String, User> userEntry : UsersManager.getInstance()
					.getRegisterredUsers().entrySet()) { // for each user
				if (destinationEntry.getValue().getAuthorEmail().equals(userEntry.getKey())) { // if
																								// destination
																								// author's
																								// email
																								// equals
																								// user's
																								// email
					userEntry.getValue().addPlace(destinationEntry.getKey()); // add
																				// destination
																				// name
																				// to
																				// user's
																				// added
																				// places
				}
			}
		}
	}
	
	public static synchronized ProjectManager getInstance() {
		if (instance == null) {
			instance = new ProjectManager();
		}
		return instance;
	}

}
