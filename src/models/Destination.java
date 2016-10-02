package models;

import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;

import functionality.UsersManager;

public class Destination {
	public enum Category {
		MOUNTAINS, BEACHES, CAVES, RIVERS, FORESTS, WATERFALLS, LAKES, CITIES, RURAL_AREAS, CAMPSITES
	};

	private String name;
	private String description;
	private Location location;
	private String mainPicture;
	private String authorEmail;
	private CopyOnWriteArrayList<Comment> comments;
	private ConcurrentSkipListSet<PlaceToSleep> placesToSleep;
	private ConcurrentSkipListSet<PlaceToEat> placesToEat;
	private Category category;
	private CopyOnWriteArrayList<String> pictures;
	private CopyOnWriteArrayList<String> videos;
	private int numberOfLikes;
	private CopyOnWriteArrayList<String> userLikers;
	private int numberOfDislikes;
	private CopyOnWriteArrayList<String> userDislikers;
	private ConcurrentSkipListSet<Activity> activities;
	private ConcurrentSkipListSet<Sight> sights;

	public Destination(String name, Double latitude, Double longitude, String description, String mainPicture,
			String authorEmail, Category category, int numberOfLikes, int numberOfDislikes) {

		setName(name);
		setDescription(description);
		setLocation(latitude, longitude);
		setMainPicture(mainPicture);
		setAuthorEmail(authorEmail);
		setCategory(category);
		setNumberOfLikes(numberOfLikes);
		setNumberOfDislikes(numberOfDislikes);
		this.placesToEat = new ConcurrentSkipListSet<>();
		this.comments = new CopyOnWriteArrayList<>();
		this.activities = new ConcurrentSkipListSet<>();
		this.sights = new ConcurrentSkipListSet<>();
		this.placesToSleep = new ConcurrentSkipListSet<>();
		this.userLikers = new CopyOnWriteArrayList<>();
		this.userDislikers = new CopyOnWriteArrayList<>();
		this.pictures = new CopyOnWriteArrayList<>();
		this.videos = new CopyOnWriteArrayList<>();
	}

	private void setLocation(Double latitude, Double longitude) {
		this.location = new Location(latitude, longitude);
	}

	public synchronized void addComment(Comment comment) {
		if (comment != null)
			this.comments.add(comment);
	}

	public synchronized void removeComment(Comment comment) {
		if (comment != null && comments.contains(comment))
			this.comments.remove(comment);
	}

	public synchronized void addPlaceToSleep(PlaceToSleep place) {
		if (place != null)
			this.placesToSleep.add(place);
	}

	public synchronized void removePlaceToSleep(PlaceToSleep place) {
		if (place != null && placesToSleep.contains(place))
			this.placesToSleep.remove(place);
	}

	public synchronized void addPlaceToEat(PlaceToEat place) {
		if (place != null)
			this.placesToEat.add(place);
	}

	public synchronized void removePlaceToEat(PlaceToEat place) {
		if (place != null && placesToEat.contains(place))
			this.placesToEat.remove(place);
	}

	public synchronized void addActivity(Activity activity) {
		if (activity != null)
			this.activities.add(activity);
	}

	public synchronized void removeActivity(Activity activity) {
		if (activity != null && activities.contains(activity))
			this.activities.remove(activity);
	}

	public synchronized void addSight(Sight sight) {
		if (sight != null)
			this.sights.add(sight);
	}

	public synchronized void removeSight(Sight sight) {
		if (sight != null && sights.contains(sight))
			this.sights.remove(sight);
	}

	public synchronized void addPicture(String picture) {
		if (picture != null && !picture.isEmpty())
			pictures.add(picture);
	}

	public synchronized void removePicture(String picture) {
		if (picture != null && !picture.isEmpty() && pictures.contains(picture)) {
			this.pictures.remove(picture);
		}
	}

	public synchronized boolean like(String userEmail) {
		if (userEmail != null && !userEmail.isEmpty()) {
			if (addUserLiker(userEmail)) { // if the user has not liked yet
				this.numberOfLikes++;
				return true;
			}
		}
		return false;
	}

	private synchronized boolean addUserLiker(String userEmail) {
		if (userEmail != null && !userEmail.isEmpty() && !userLikers.contains(userEmail)
				&& !userEmail.equals(authorEmail)) {
			if (userDislikers.contains(userEmail)) {
				removeDislike(userEmail);
			}
			userLikers.add(userEmail);
			return true;
		} else {
			return false;
		}
	}

	public synchronized boolean unlike(String userEmail) {
		if (userEmail != null && !userEmail.isEmpty()) {
			if (removeUserLiker(userEmail)) { // if the user has liked it
				this.numberOfLikes--;
				return true;
			}
		}
		return false;
	}

	private synchronized boolean removeUserLiker(String userEmail) {
		if (userEmail != null && !userEmail.isEmpty() && userLikers.contains(userEmail)) {
			userLikers.remove(userEmail);
			return true;
		} else {
			return false;
		}
	}

	public synchronized boolean dislike(String userEmail) {
		if (userEmail != null && !userEmail.isEmpty()) {
			if (addUserDisliker(userEmail)) { // if the user has not disliked
												// yet
				this.numberOfDislikes++;
				return true;
			}
		}
		return false;
	}

	private synchronized boolean addUserDisliker(String userEmail) {
		if (userEmail != null && !userEmail.isEmpty() && !userDislikers.contains(userEmail)
				&& !userEmail.equals(authorEmail)) {
			if (userLikers.contains(userEmail)) {
				unlike(userEmail);
			}
			userDislikers.add(userEmail);
			return true;
		} else {
			return false;
		}
	}

	public synchronized boolean removeDislike(String userEmail) {
		if (userEmail != null && !userEmail.isEmpty()) {
			if (removeUserDisliker(userEmail)) { // if the user has disliked it
				this.numberOfDislikes--;
				return true;
			}
		}
		return false;
	}

	private synchronized boolean removeUserDisliker(String userEmail) {
		if (userEmail != null && !userEmail.isEmpty() && userDislikers.contains(userEmail)) {
			userDislikers.remove(userEmail);
			return true;
		} else {
			return false;
		}
	}

	public synchronized String getName() {
		return name;
	}

	public synchronized String getDescription() {
		return description;
	}

	public synchronized Location getLocation() {
		return location;
	}

	public synchronized CopyOnWriteArrayList<Comment> getComments() {
		CopyOnWriteArrayList<Comment> copy = new CopyOnWriteArrayList<>();
		copy.addAll(comments);
		return comments;
	}

	public synchronized void setName(String name) {
		if (name != null && !name.isEmpty())
			this.name = name;
	}

	public synchronized void setDescription(String description) {
		if (description != null && !description.isEmpty())
			this.description = description;
	}

	public synchronized void setLocation(Location location) {
		if (location != null)
			this.location = location;
	}

	public synchronized String getMainPicture() {
		return mainPicture;
	}

	public synchronized void setMainPicture(String mainPicture) {
		if (mainPicture != null && !mainPicture.isEmpty())
			this.mainPicture = mainPicture;
	}

	public synchronized ConcurrentSkipListSet<PlaceToSleep> getPlacesToSleep() {
		ConcurrentSkipListSet<PlaceToSleep> copy = new ConcurrentSkipListSet<>();
		copy.addAll(placesToSleep);
		return copy;
	}

	public synchronized void setPlacesToSleep(ConcurrentSkipListSet<PlaceToSleep> placesToSleep) {
		if (placesToSleep != null)
			this.placesToSleep = placesToSleep;
	}

	public synchronized ConcurrentSkipListSet<PlaceToEat> getPlacesToEat() {
		ConcurrentSkipListSet<PlaceToEat> copy = new ConcurrentSkipListSet<PlaceToEat>();
		copy.addAll(placesToEat);
		return copy;
	}

	public synchronized void setPlacesToEat(ConcurrentSkipListSet<PlaceToEat> placesToEat) {
		if (placesToEat != null)
			this.placesToEat = placesToEat;
	}

	public synchronized void setAuthorEmail(String authorEmail) {
		if (authorEmail != null && !authorEmail.isEmpty())
			this.authorEmail = authorEmail;
	}

	public synchronized void setComments(CopyOnWriteArrayList<Comment> comments) {
		if (comments != null)
			this.comments = comments;
	}

	public synchronized String getAuthorEmail() {
		return authorEmail;
	}

	public synchronized User getAuthor() {
		return UsersManager.getInstance().getUserFromCache(this.authorEmail);
	}

	public synchronized CopyOnWriteArrayList<String> getPictures() {
		CopyOnWriteArrayList<String> copy = new CopyOnWriteArrayList<>();
		copy.addAll(pictures);
		return copy;
	}

	public synchronized void setPictures(CopyOnWriteArrayList<String> pictures) {
		if (pictures != null)
			this.pictures = pictures;
	}

	public synchronized CopyOnWriteArrayList<String> getVideos() {
		CopyOnWriteArrayList<String> copy = new CopyOnWriteArrayList<>();
		copy.addAll(videos);
		return copy;
	}

	public synchronized void setVideos(CopyOnWriteArrayList<String> videos) {
		if (videos != null)
			this.videos = videos;
	}

	public synchronized int getNumberOfLikes() {
		return numberOfLikes;
	}

	public synchronized void setNumberOfLikes(int numberOfLikes) {
		if (numberOfLikes >= 0)
			this.numberOfLikes = numberOfLikes;
	}

	public synchronized CopyOnWriteArrayList<String> getUserLikers() {
		CopyOnWriteArrayList<String> copy = new CopyOnWriteArrayList<String>();
		copy.addAll(userLikers);
		return copy;
	}

	public synchronized void setUserLikers(CopyOnWriteArrayList<String> userLikers) {
		if (userLikers != null)
			this.userLikers = userLikers;
	}

	public synchronized ConcurrentSkipListSet<Activity> getActivities() {
		ConcurrentSkipListSet<Activity> copy = new ConcurrentSkipListSet<Activity>();
		copy.addAll(activities);
		return copy;
	}

	public synchronized void setActivities(ConcurrentSkipListSet<Activity> activities) {
		if (activities != null)
			this.activities = activities;
	}

	public synchronized ConcurrentSkipListSet<Sight> getSights() {
		ConcurrentSkipListSet<Sight> copy = new ConcurrentSkipListSet<Sight>();
		copy.addAll(sights);
		return copy;
	}

	public synchronized void setSights(ConcurrentSkipListSet<Sight> sights) {
		this.sights = sights;
	}

	public synchronized Category getCategory() {
		return category;
	}

	public synchronized void setCategory(Category category) {
		this.category = category;
	}

	public synchronized int getNumberOfDislikes() {
		return numberOfDislikes;
	}

	public synchronized void setNumberOfDislikes(int numberOfDislikes) {
		if (numberOfDislikes >= 0)
			this.numberOfDislikes = numberOfDislikes;
	}

	public synchronized CopyOnWriteArrayList<String> getUserDislikers() {
		CopyOnWriteArrayList<String> copy = new CopyOnWriteArrayList<>();
		copy.addAll(userDislikers);
		return copy;
	}

	public synchronized void setUserDislikers(CopyOnWriteArrayList<String> userDislikers) {
		if (userDislikers != null)
			this.userDislikers = userDislikers;
	}

	public synchronized void addVideo(String video) {
		this.videos.add(video);
	}

}
