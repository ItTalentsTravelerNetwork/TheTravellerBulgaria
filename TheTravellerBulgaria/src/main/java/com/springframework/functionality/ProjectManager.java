package com.springframework.functionality;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.springframework.SpringContextProvider;
import com.springframework.model.Comment;
import com.springframework.model.Destination;
import com.springframework.model.User;

@Component
public class ProjectManager {

	private static ProjectManager instance = new ProjectManager(); // Singleton

	public ProjectManager() {
		// Loading all Managers and the cache

		// UsersManager
		SpringContextProvider.context.getBean(UsersManager.class); // with
																	// visited
																	// destinations
																	// and
																	// followed
																	// users;
																	// no
																	// added
																	// destinations

		// DestinationsManager
		SpringContextProvider.context.getBean(DestinationsManager.class); // no
																			// comment
																			// objects
																			// in
																			// each
																			// destination

		// CommentsManager
		SpringContextProvider.context.getBean(CommentsManager.class); // all
																		// comments
																		// +
																		// all
																		// user
																		// likers

		// adding all comments to destinations
		for (Map.Entry<String, Destination> destinationEntry : SpringContextProvider.context
				.getBean(DestinationsManager.class).getAllDestinations().entrySet()) { // for
																						// each
																						// destination
																						// in
																						// cache
			for (Comment c : SpringContextProvider.context.getBean(CommentsManager.class).getAllComments()) { // for
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
		for (Map.Entry<String, Destination> destinationEntry : SpringContextProvider.context
				.getBean(DestinationsManager.class).getAllDestinations().entrySet()) { // for
																						// each
																						// destination
			for (Map.Entry<String, User> userEntry : SpringContextProvider.context.getBean(UsersManager.class)
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
