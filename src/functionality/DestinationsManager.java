package functionality;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;

import dbModels.DestinationDAO;
import exceptions.InvalidCoordinatesException;
import models.Activity;
import models.Destination;
import models.Destination.Category;
import models.Location;
import models.PlaceToEat;
import models.PlaceToSleep;
import models.Sight;
import models.User;

public class DestinationsManager {

	private static DestinationsManager instance; // Singleton
	private ConcurrentHashMap<String, Destination> allDestinations; // destination
																	// name and
																	// destination
	private ConcurrentHashMap<String, String> allDestinationsAndAuthors; // destination
																			// name
																			// and
																			// author
																			// email

	private DestinationsManager() {

		allDestinationsAndAuthors = DestinationDAO.getInstance().getAllDestinationsAndAuthors(); // adds
																									// destinations
																									// and
																									// authors
																									// to
																									// collection
		allDestinations = new ConcurrentHashMap<>();
		Set<Destination> tempAllDest = DestinationDAO.getInstance().getAllDestinations();
		for (Destination d : tempAllDest) { // adds
											// all
											// destinations
											// form
											// DB
											// to
											// collection
			allDestinations.put(d.getName(), d);
		}

	}

	public static synchronized DestinationsManager getInstance() {
		if (instance == null) {
			instance = new DestinationsManager();
		}
		return instance;
	}

	public boolean chechDestinationInCache(String name) { // validation of input
		if (allDestinations.containsKey(name) && allDestinationsAndAuthors.containsKey(name)) { // destination
																								// exists
																								// already
			return true;
		}
		return false;
	}

	public boolean addDestination(User user, String name, String description, double lattitude, double longitude,
			String mainPicture, ConcurrentSkipListSet<PlaceToSleep> placesToSleep,
			ConcurrentSkipListSet<PlaceToEat> placesToEat, Category category, CopyOnWriteArrayList<String> pictures,
			CopyOnWriteArrayList<String> videos, ConcurrentSkipListSet<Activity> activities,
			ConcurrentSkipListSet<Sight> sights) throws InvalidCoordinatesException {
		if (UsersManager.getInstance().validateUser(user.getEmail(), user.getPassword())
				&& !chechDestinationInCache(name)) { // if the user exists in
														// the collection and no
														// such destination yet
			Destination destination = new Destination(name, description, new Location(lattitude, longitude),
					mainPicture, user.getEmail(), new CopyOnWriteArrayList<>(), placesToSleep, placesToEat, category,
					pictures, videos, 0, new CopyOnWriteArrayList<>(), 0, new CopyOnWriteArrayList<>(), activities,
					sights);
			allDestinations.put(name, destination); // adds the new destination
			allDestinationsAndAuthors.put(name, user.getEmail()); // to the
																	// collection
			user.addPlace(name);
			DestinationDAO.getInstance().saveDestinationToDB(user, destination); // saves
																					// destination
																					// to
																					// DB
			return true;
		}
		return false; // no such user
	}

	public boolean removeDestination(String destinationName) {
		if (chechDestinationInCache(destinationName)) { // if there is such
														// destination
			allDestinations.remove(destinationName);
			allDestinationsAndAuthors.remove(destinationName);
			UsersManager.getInstance().getUserFromCache(allDestinationsAndAuthors.get(destinationName))
					.removePlace(destinationName); // removes place from user's
													// added places
			DestinationDAO.getInstance().removeDestination(destinationName);
			return true;
		}
		return false;
	}

	public Destination getDestinationFromCache(String destinationName) {
		if (!allDestinations.containsKey(destinationName)) {
			return null; // no such destination
		}
		return allDestinations.get(destinationName); // returns the destination
	}

	public boolean updateDestinationInfo(String name, String description, double longitude, double lattitude,
			String mainPicture, ConcurrentSkipListSet<PlaceToSleep> placesToSleep,
			ConcurrentSkipListSet<PlaceToEat> placesToEat, Category category, CopyOnWriteArrayList<String> pictures,
			CopyOnWriteArrayList<String> videos, ConcurrentSkipListSet<Activity> activities,
			ConcurrentSkipListSet<Sight> sights) throws InvalidCoordinatesException {
		if (allDestinations.containsKey(name)) { // destination exists
			Destination destination = allDestinations.get(name); // takes the
																	// destination
																	// and
																	// updates
																	// its
																	// fields
			destination.setDescription(description);
			destination.setLocation(new Location(lattitude, longitude));
			destination.setMainPicture(mainPicture);
			destination.setPlacesToSleep(placesToSleep);
			destination.setPlacesToEat(placesToEat);
			destination.setCategory(category);
			destination.setPictures(pictures);
			destination.setVideos(videos);
			destination.setActivities(activities);
			destination.setSights(sights);
			boolean updateInDB = DestinationDAO.getInstance().updateDestinationInDB(name, description, longitude,
					lattitude, mainPicture, placesToSleep, placesToEat, category, pictures, videos, activities, sights); // updates
			// the
			// DB
			// destination
			if (updateInDB) { // if DB is updated
				return true;
			}
		}
		return false;
	}

	public boolean like(String userEmail, String destinationName) {
		if (allDestinations.containsKey(destinationName)
				&& UsersManager.getInstance().getUserFromCache(userEmail) != null) {
			if (allDestinations.get(destinationName).like(userEmail)) { // if
																		// destination
																		// updated
				if (DestinationDAO.getInstance().addLike(userEmail, destinationName)) { // if
																						// DB
																						// updated
					return true;
				}
			}
		}
		return false;
	}

	public boolean dislike(String userEmail, String destinationName) {
		if (allDestinations.containsKey(destinationName)
				&& UsersManager.getInstance().getUserFromCache(userEmail) != null) {
			if (allDestinations.get(destinationName).dislike(userEmail)) { // if
																			// destination
																			// updated
				if (DestinationDAO.getInstance().removeLike(userEmail, destinationName)) { // if
																							// DB
																							// updated
					return true;
				}
			}
		}
		return false;
	}

	public String getDestinationAuthor(String destinationName) {
		return allDestinationsAndAuthors.get(destinationName);
	}

	public ConcurrentHashMap<String, String> getAllDestinationsAndAuthors() {
		ConcurrentHashMap<String, String> copy = new ConcurrentHashMap<>();
		copy.putAll(allDestinationsAndAuthors);
		return copy;
	}

	public ConcurrentHashMap<String, Destination> getAllDestinations() {
		ConcurrentHashMap<String, Destination> copy = new ConcurrentHashMap<>();
		copy.putAll(allDestinations);
		return copy;
	}

}
