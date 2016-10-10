package com.springframework.functionality;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;

import com.springframework.dbModels.ActivityDao;
import com.springframework.dbModels.DestinationDao;
import com.springframework.dbModels.HotelDao;
import com.springframework.dbModels.PlaceToEatDao;
import com.springframework.dbModels.SightDao;
import com.springframework.exceptions.InvalidCoordinatesException;
import com.springframework.model.Activity;
import com.springframework.model.Comment;
import com.springframework.model.Destination;
import com.springframework.model.Destination.Category;
import com.springframework.model.Location;
import com.springframework.model.PlaceToEat;
import com.springframework.model.PlaceToSleep;
import com.springframework.model.Sight;
import com.springframework.model.User;

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

		allDestinations = new ConcurrentHashMap<>();
		allDestinationsAndAuthors = new ConcurrentHashMap<>();
		Set<Destination> tempAllDest = DestinationDao.getInstance().getAllDestinations();
		for (Destination d : tempAllDest) { // adds
											// all
											// destinations
											// form
											// DB
											// to
											// collection
			allDestinations.put(d.getName(), d);
		}
		for (Destination destination : allDestinations.values()) {
			this.allDestinationsAndAuthors.put(destination.getName(), destination.getAuthorEmail());
		}
		this.fillDestinationsWithPlaces();
		this.fillDestinationsWithPics();
		this.fillDestinationsWithLikesAndDislikes();
		this.fillDestinationsWithVideos();
	}

	public static synchronized DestinationsManager getInstance() {
		if (instance == null) {
			instance = new DestinationsManager();
		}
		return instance;
	}

	private void fillDestinationsWithPics() {
		for (Entry<String, ArrayList<String>> destPics : DestinationDao.getInstance().getDestinationPictures()
				.entrySet()) {
			String destName = destPics.getKey();
			for (String pic : destPics.getValue()) {
				this.allDestinations.get(destName).addPicture(pic);
			}
		}

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
			String mainPicture, String category) throws InvalidCoordinatesException {
		if (UsersManager.getInstance().validateUser(user.getEmail(), user.getPassword())
				&& !chechDestinationInCache(name)) { // if
														// the
														// user
														// exists
														// in
														// the
														// collection
														// and
														// no
														// such
														// destination
														// yet
			Category destCategory = Destination.Category.valueOf(category);
			Destination destination = new Destination(name, description, lattitude, longitude, mainPicture,
					user.getEmail(), destCategory, 0, 0);
			allDestinations.put(name, destination); // adds the new destination
			allDestinationsAndAuthors.put(name, user.getEmail()); // to the
																	// collection
			UsersManager.getInstance().addDestinationToUser(user.getEmail(), name);
			DestinationDao.getInstance().saveDestinationToDB(user, destination); // saves
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
					.removePlace(destinationName); // removes
													// place
													// from
													// user's
													// added
													// places
			DestinationDao.getInstance().removeDestination(destinationName);
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
			ConcurrentSkipListSet<PlaceToEat> placesToEat, String category, CopyOnWriteArrayList<String> pictures,
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
			Category destCategory = Destination.Category.valueOf(category);
			destination.setCategory(destCategory);
			destination.setPictures(pictures);
			destination.setVideos(videos);
			destination.setActivities(activities);
			destination.setSights(sights);
			boolean updateInDB = DestinationDao.getInstance().updateDestinationInDB(name, description, longitude,
					lattitude, mainPicture, placesToSleep, placesToEat, destCategory, pictures, videos, activities,
					sights); // updates
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
				if (DestinationDao.getInstance().addLike(userEmail, destinationName)) {
					// if the DB is updated
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
				if (DestinationDao.getInstance().removeLike(userEmail, destinationName)) {
					// if the DB is updated
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

	private void fillDestinationsWithPlaces() {
		Set<Activity> activities = ActivityDao.getInstance().getAllActivities();
		Set<PlaceToSleep> placesToSleep = HotelDao.getInstance().getAllHotels();
		Set<PlaceToEat> placesToEat = PlaceToEatDao.getInstance().getAllPlacesToEat();
		Set<Sight> sights = SightDao.getInstance().getAllSights();
		for (Sight sight : sights) {
			String destName = sight.getDestinationName();
			allDestinations.get(destName).addSight(sight);
		}
		for (Activity activity : activities) {
			String destName = activity.getDestinationName();
			allDestinations.get(destName).addActivity(activity);
		}
		for (PlaceToSleep placeToSleep : placesToSleep) {
			String destName = placeToSleep.getDestinationName();
			allDestinations.get(destName).addPlaceToSleep(placeToSleep);
		}
		for (PlaceToEat placeToEat : placesToEat) {
			String destName = placeToEat.getDestinationName();
			allDestinations.get(destName).addPlaceToEat(placeToEat);
		}
	}

	private void fillDestinationsWithLikesAndDislikes() {
		for (Entry<String, ArrayList<String>> entry : DestinationDao.getInstance().getLikes().entrySet()) {
			for (String email : entry.getValue()) {
				this.allDestinations.get(entry.getKey()).like(email);
			}
		}
		for (Entry<String, ArrayList<String>> entry : DestinationDao.getInstance().getDisLikes().entrySet()) {
			for (String email : entry.getValue()) {
				this.allDestinations.get(entry.getKey()).dislike(email);
			}
		}
	}

	private void fillDestinationsWithVideos() {
		for (Entry<String, ArrayList<String>> entry : DestinationDao.getInstance().getVideos().entrySet()) {
			for (String video : entry.getValue()) {
				this.allDestinations.get(entry.getKey()).addVideo(video);
			}
		}
	}

	public void addActivity(String destName, Activity a) throws CloneNotSupportedException {
		this.allDestinations.get(destName).addActivity(a);
		ActivityDao.getInstance().saveActivityToDb(a);
	}

	public void addSight(String destName, Sight sight) throws CloneNotSupportedException {
		this.allDestinations.get(destName).addSight(sight);
		SightDao.getInstance().saveSightToDB(sight);
	}

	public void addHotel(String destName, PlaceToSleep p) throws CloneNotSupportedException {
		this.allDestinations.get(destName).addPlaceToSleep(p);
		HotelDao.getInstance().saveHotelInDB(p);
	}

	public void addPlaceToEat(String destName, PlaceToEat e) throws CloneNotSupportedException {
		this.allDestinations.get(destName).addPlaceToEat(e);
		PlaceToEatDao.getInstance().savePlaceToEatInDB(e);
	}

	public void addPicture(String destName, String pic) {
		this.allDestinations.get(destName).addPicture(pic);
		DestinationDao.getInstance().addPicture(destName, pic);
	}

	public void addVideo(String destName, String video) {
		this.allDestinations.get(destName).addVideo(video);
		DestinationDao.getInstance().addVideo(destName, video);
	}

	public void addLike(String userEmail, String destinationName) {
		Destination dest = this.allDestinations.get(destinationName);
		if (dest.getUserLikers().contains(userEmail)) {
			return;
		}
		if (dest.getUserDislikers().contains(userEmail)) {
			dest.getUserDislikers().remove(userEmail);
			DestinationDao.getInstance().removeDislike(userEmail, destinationName);
		}
		dest.like(userEmail);
		DestinationDao.getInstance().addLike(userEmail, destinationName);
	}

	public void addDislike(String userEmail, String destinationName) {
		Destination dest = this.allDestinations.get(destinationName);
		if (dest.getUserDislikers().contains(userEmail)) {
			return;
		}
		if (dest.getUserLikers().contains(userEmail)) {
			dest.getUserLikers().remove(userEmail);
			DestinationDao.getInstance().removeLike(userEmail, destinationName);
		}
		dest.dislike(userEmail);
		DestinationDao.getInstance().addDislike(userEmail, destinationName);
	}

	public void removeLike(String userEmail, String destinationName) {
		Destination dest = this.allDestinations.get(destinationName);
		if (!dest.getUserLikers().contains(userEmail)) {
			return;
		}
		dest.unlike(userEmail);
		DestinationDao.getInstance().removeLike(userEmail, destinationName);
	}

	public void removeDisike(String userEmail, String destinationName) {
		Destination dest = this.allDestinations.get(destinationName);
		if (!dest.getUserDislikers().contains(userEmail)) {
			return;
		}
		dest.removeDislike(userEmail);
		DestinationDao.getInstance().removeDislike(userEmail, destinationName);
	}

	public boolean deleteAllUserData(User user) {
		String userEmail = user.getEmail();
		ArrayList<Destination> destinationsToRemove = new ArrayList<>();
		for (Entry<String, String> authorDestination : this.allDestinationsAndAuthors.entrySet()) {
			if (authorDestination.getValue().equals(userEmail)) {
				destinationsToRemove.add(this.allDestinations.get(authorDestination.getKey()));
			}
		}
		for (Destination dest : destinationsToRemove) {
			this.allDestinations.remove(dest.getName());
			this.allDestinationsAndAuthors.remove(dest.getName());

		}

		for (Destination dest : this.allDestinations.values()) {

			for (Comment comment : dest.getComments()) {
				if (comment.getAuthorEmail().equals(userEmail)) {
					dest.removeComment(comment);
				} else {
					for (String email : comment.getUserLikers()) {
						if (email.equals(userEmail)) {
							comment.unlike(userEmail);
						}
					}
				}
			}
			for (String email : dest.getUserLikers()) {
				if (email.equals(userEmail)) {
					dest.unlike(userEmail);
				}
			}
			for (String email : dest.getUserDislikers()) {
				if (email.equals(userEmail)) {
					dest.removeDislike(userEmail);
				}
			}
		}
		CommentsManager.getInstance().deleteUserComments(userEmail);
		return true;
	}

}
