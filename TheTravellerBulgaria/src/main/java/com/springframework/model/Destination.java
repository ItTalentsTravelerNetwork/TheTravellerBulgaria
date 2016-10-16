package com.springframework.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;

import com.springframework.functionality.UsersManager;

public class Destination {

	public static final DateTimeFormatter DATE_AND_TIME_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

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
	private LocalDateTime dateAndTime;

	public Destination(String name, String description, double lattitude, double longitude, String mainPicture,
			String authorEmail, Category category, int numberOfLikes, int numberOfDislikes) { // creating
																								// dest.
																								// without
																								// time
																								// (now)

		setName(name);
		setDescription(description);
		setLocation(lattitude, longitude);
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
		setDateAndTime(LocalDateTime.now());
	}

	public Destination(String name, String description, double lattitude, double longitude, String mainPicture,
			String authorEmail, Category category, int numberOfLikes, int numberOfDislikes, String dateAndTimeString) {
		// creating destination with a fixed dateTime value
		setName(name);
		setDescription(description);
		setLocation(lattitude, longitude);
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
		setDateAndTimeFromString(dateAndTimeString);
	}

	private void setLocation(double lattitude, double longitude) {
		this.location = new Location(lattitude, longitude);
	}

	public void addComment(Comment comment) {
		if (comment != null)
			this.comments.add(comment);
	}

	public void removeComment(Comment comment) {
		if (comment != null && comments.contains(comment))
			this.comments.remove(comment);
	}

	public void addPlaceToSleep(PlaceToSleep place) {
		if (place != null)
			this.placesToSleep.add(place);
	}

	public void removePlaceToSleep(PlaceToSleep place) {
		if (place != null && placesToSleep.contains(place))
			this.placesToSleep.remove(place);
	}

	public void addPlaceToEat(PlaceToEat place) {
		if (place != null)
			this.placesToEat.add(place);
	}

	public void removePlaceToEat(PlaceToEat place) {
		if (place != null && placesToEat.contains(place))
			this.placesToEat.remove(place);
	}

	public void addActivity(Activity activity) {
		if (activity != null)
			this.activities.add(activity);
	}

	public void removeActivity(Activity activity) {
		if (activity != null && activities.contains(activity))
			this.activities.remove(activity);
	}

	public void addSight(Sight sight) {
		if (sight != null)
			this.sights.add(sight);
	}

	public void removeSight(Sight sight) {
		if (sight != null && sights.contains(sight))
			this.sights.remove(sight);
	}

	public void addPicture(String picture) {
		if (picture != null && !picture.isEmpty())
			pictures.add(picture);
	}

	public void removePicture(String picture) {
		if (picture != null && !picture.isEmpty() && pictures.contains(picture)) {
			this.pictures.remove(picture);
		}
	}

	public boolean like(String userEmail) {
		if (userEmail != null && !userEmail.isEmpty()) {
			if (addUserLiker(userEmail)) { // if the user has not liked yet
				this.numberOfLikes++;
				return true;
			}
		}
		return false;
	}

	private boolean addUserLiker(String userEmail) {
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

	public boolean unlike(String userEmail) {
		if (userEmail != null && !userEmail.isEmpty()) {
			if (removeUserLiker(userEmail)) { // if the user has liked it
				this.numberOfLikes--;
				return true;
			}
		}
		return false;
	}

	private boolean removeUserLiker(String userEmail) {
		if (userEmail != null && !userEmail.isEmpty() && userLikers.contains(userEmail)) {
			userLikers.remove(userEmail);
			return true;
		} else {
			return false;
		}
	}

	public boolean dislike(String userEmail) {
		if (userEmail != null && !userEmail.isEmpty()) {
			if (addUserDisliker(userEmail)) { // if the user has not disliked
												// yet
				this.numberOfDislikes++;
				return true;
			}
		}
		return false;
	}

	private boolean addUserDisliker(String userEmail) {
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

	public boolean removeDislike(String userEmail) {
		if (userEmail != null && !userEmail.isEmpty()) {
			if (removeUserDisliker(userEmail)) { // if the user has disliked it
				this.numberOfDislikes--;
				return true;
			}
		}
		return false;
	}

	private boolean removeUserDisliker(String userEmail) {
		if (userEmail != null && !userEmail.isEmpty() && userDislikers.contains(userEmail)) {
			userDislikers.remove(userEmail);
			return true;
		} else {
			return false;
		}
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Location getLocation() {
		return location;
	}

	public CopyOnWriteArrayList<Comment> getComments() {
		CopyOnWriteArrayList<Comment> copy = new CopyOnWriteArrayList<>();
		copy.addAll(comments);
		return comments;
	}

	public void setName(String name) {
		if (name != null && !name.isEmpty())
			this.name = name;
	}

	public void setDescription(String description) {
		if (description != null && !description.isEmpty())
			this.description = description;
	}

	public void setLocation(Location location) {
		if (location != null)
			this.location = location;
	}

	public String getMainPicture() {
		return mainPicture;
	}

	public void setMainPicture(String mainPicture) {
		if (mainPicture != null && !mainPicture.isEmpty())
			this.mainPicture = mainPicture;
	}

	public ConcurrentSkipListSet<PlaceToSleep> getPlacesToSleep() {
		ConcurrentSkipListSet<PlaceToSleep> copy = new ConcurrentSkipListSet<>();
		copy.addAll(placesToSleep);
		return copy;
	}

	public void setPlacesToSleep(ConcurrentSkipListSet<PlaceToSleep> placesToSleep) {
		if (placesToSleep != null)
			this.placesToSleep = placesToSleep;
	}

	public ConcurrentSkipListSet<PlaceToEat> getPlacesToEat() {
		ConcurrentSkipListSet<PlaceToEat> copy = new ConcurrentSkipListSet<PlaceToEat>();
		copy.addAll(placesToEat);
		return copy;
	}

	public void setPlacesToEat(ConcurrentSkipListSet<PlaceToEat> placesToEat) {
		if (placesToEat != null)
			this.placesToEat = placesToEat;
	}

	public void setAuthorEmail(String authorEmail) {
		if (authorEmail != null && !authorEmail.isEmpty())
			this.authorEmail = authorEmail;
	}

	public void setComments(CopyOnWriteArrayList<Comment> comments) {
		if (comments != null)
			this.comments = comments;
	}

	public String getAuthorEmail() {
		return authorEmail;
	}

	public User getAuthor() {
		return UsersManager.getInstance().getUserFromCache(this.authorEmail);
	}

	public CopyOnWriteArrayList<String> getPictures() {
		CopyOnWriteArrayList<String> copy = new CopyOnWriteArrayList<>();
		copy.addAll(pictures);
		return copy;
	}

	public void setPictures(CopyOnWriteArrayList<String> pictures) {
		if (pictures != null)
			this.pictures = pictures;
	}

	public CopyOnWriteArrayList<String> getVideos() {
		CopyOnWriteArrayList<String> copy = new CopyOnWriteArrayList<>();
		copy.addAll(videos);
		return copy;
	}

	public void setVideos(CopyOnWriteArrayList<String> videos) {
		if (videos != null)
			this.videos = videos;
	}

	public int getNumberOfLikes() {
		return numberOfLikes;
	}

	public void setNumberOfLikes(int numberOfLikes) {
		if (numberOfLikes >= 0)
			this.numberOfLikes = numberOfLikes;
	}

	public CopyOnWriteArrayList<String> getUserLikers() {
		CopyOnWriteArrayList<String> copy = new CopyOnWriteArrayList<String>();
		copy.addAll(userLikers);
		return copy;
	}

	public void setUserLikers(CopyOnWriteArrayList<String> userLikers) {
		if (userLikers != null)
			this.userLikers = userLikers;
	}

	public ConcurrentSkipListSet<Activity> getActivities() {
		ConcurrentSkipListSet<Activity> copy = new ConcurrentSkipListSet<Activity>();
		copy.addAll(activities);
		return copy;
	}

	public void setActivities(ConcurrentSkipListSet<Activity> activities) {
		if (activities != null)
			this.activities = activities;
	}

	public ConcurrentSkipListSet<Sight> getSights() {
		ConcurrentSkipListSet<Sight> copy = new ConcurrentSkipListSet<Sight>();
		copy.addAll(sights);
		return copy;
	}

	public void setSights(ConcurrentSkipListSet<Sight> sights) {
		this.sights = sights;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public int getNumberOfDislikes() {
		return numberOfDislikes;
	}

	public void setNumberOfDislikes(int numberOfDislikes) {
		if (numberOfDislikes >= 0)
			this.numberOfDislikes = numberOfDislikes;
	}

	public CopyOnWriteArrayList<String> getUserDislikers() {
		CopyOnWriteArrayList<String> copy = new CopyOnWriteArrayList<>();
		copy.addAll(userDislikers);
		return copy;
	}

	public void setUserDislikers(CopyOnWriteArrayList<String> userDislikers) {
		if (userDislikers != null)
			this.userDislikers = userDislikers;
	}

	public void addVideo(String video) {
		this.videos.add(video);
	}

	public LocalDateTime getDateAndTime() {
		return dateAndTime;
	}

	public String getDateAndTimeToString() {
		String dateAndTimeToString = dateAndTime.format(DATE_AND_TIME_FORMAT);
		return dateAndTimeToString;
	}

	public void setDateAndTimeFromString(String dateAndTimeString) {
		LocalDateTime dateAndTime = LocalDateTime.parse(dateAndTimeString, DATE_AND_TIME_FORMAT);
		this.dateAndTime = dateAndTime;
	}

	public void setDateAndTime(LocalDateTime dateAndTime) {
		this.dateAndTime = dateAndTime;
	}

}
