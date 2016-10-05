package com.springframework.functionality;

import java.util.Map;

import com.springframework.model.Comment;
import com.springframework.model.Destination;
import com.springframework.model.User;

public class ProjectManager {

	private static ProjectManager instance = new ProjectManager(); // Singleton

	private ProjectManager() {
		// Loading all Managers and the cache
		
		// UsersManager
		UsersManager.getInstance(); // with visited destinations and followed users; no added destinations
		
	
		// DestinationsManager
		DestinationsManager.getInstance(); // no comment objects in each destination (*TODO add fillUserLikers, fillActivities, fillSights, fillHotels, fillRestaurants, fill pictures, fill videos)
		
		
		// CommentsManager
		CommentsManager.getInstance(); // all comments (*TODO add fillUserLikers)

		
		// adds all comments to destinations
		for (Map.Entry<String, Destination> destinationEntry : DestinationsManager.getInstance().getAllDestinations()
				.entrySet()) { // for
								// each
								// destination
								// in
								// cache
			for (Comment c : CommentsManager.getInstance().getAllComments()) { // for
																				// each
																				// comment
				// in cache
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

		
		// adds all destinations to authors (users fill added places)
		for (Map.Entry<String, Destination> destinationEntry : DestinationsManager.getInstance().getAllDestinations()
				.entrySet()) { // for each destination
			for (Map.Entry<String, User> userEntry : UsersManager.getInstance().getRegisterredUsers().entrySet()) { // for
																													// each
																													// user
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
