package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

import exceptions.InvalidAuthorException;
import exceptions.InvalidDataException;

public class Comment {

	private static final DateTimeFormatter DATE_AND_TIME_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	private long id;
	private String authorEmail;
	private String placeName;
	private String text;
	private int numberOfLikes;
	private CopyOnWriteArrayList<String> userLikers; // List of users who like
														// the comment
	// (emails)
	private LocalDateTime dateAndTime;
	private boolean hasVideo;
	private String video;

	public Comment(String authorEmail, String placeName, String text, int numberOfLikes, String dateAndTime,
			String video) throws InvalidDataException { // without id
		setAuthorEmail(authorEmail);
		setPlaceName(placeName);
		this.setText(text);
		setNumberOfLikes(numberOfLikes);
		setUserLikers(new CopyOnWriteArrayList<>());
		LocalDateTime date = LocalDateTime.parse(dateAndTime, DATE_AND_TIME_FORMAT);
		setDateAndTime(date);
		setVideo(video); // can be null -> hasVideo=false
	}

	public Comment(long id, String authorEmail, String placeName, String text, int numberOfLikes, String dateAndTime,
			String video) throws InvalidDataException { // with id
		setId(id);
		setAuthorEmail(authorEmail);
		setPlaceName(placeName);
		this.setText(text);
		setNumberOfLikes(numberOfLikes);
		LocalDateTime date = LocalDateTime.parse(dateAndTime, DATE_AND_TIME_FORMAT);
		setDateAndTime(date);
		setVideo(video); // can be null -> hasVideo=false
	}

	public Comment(String authorEmail, String placeName, String text, int numberOfLikes, ArrayList<String> userLikers,
			Date date) throws InvalidDataException, InvalidAuthorException {

	}

	public synchronized void setAuthorEmail(String authorEmail) {
		if (authorEmail != null)
			this.authorEmail = authorEmail;
	}

	public synchronized String getAuthorEmail() {
		return authorEmail;
	}

	public synchronized LocalDateTime getDateAndTime() {
		return dateAndTime;
	}

	public synchronized String getDateAndTimeToString() {
		String dateAndTimeToString = dateAndTime.format(DATE_AND_TIME_FORMAT);
		return dateAndTimeToString;
	}

	public synchronized void setDateAndTimeFromString(String dateAndTimeString) {
		LocalDateTime dateAndTime = LocalDateTime.parse(dateAndTimeString, DATE_AND_TIME_FORMAT);
		this.dateAndTime = dateAndTime;
	}

	public synchronized void setDateAndTime(LocalDateTime dateAndTime) {
		this.dateAndTime = dateAndTime;
	}

	public synchronized void setUserLikers(CopyOnWriteArrayList<String> userLikers) {
		if (userLikers != null)
			this.userLikers = userLikers;
	}

	public synchronized String getText() {
		return text;
	}

	private synchronized void setText(String text) throws InvalidDataException {
		if (text != null && !text.isEmpty()) {
			this.text = text;
		} else {
			throw new InvalidDataException();
		}
	}

	public synchronized int getNumberOfLikes() {
		return numberOfLikes;
	}

	public synchronized void like(String userEmail) {
		if (userEmail != null && !userEmail.isEmpty()) {
			if (addUserLiker(userEmail)) { // if the user has not liked yet
				this.numberOfLikes++;
			}
		}
	}

	private synchronized boolean addUserLiker(String userEmail) {
		if (userEmail != null && !userEmail.isEmpty() && !userLikers.contains(userEmail)
				&& !userEmail.equals(authorEmail)) {
			userLikers.add(userEmail);
			return true;
		} else {
			return false;
		}
	}

	public synchronized void unlike(String userEmail) {
		if (userEmail != null && !userEmail.isEmpty()) {
			if (removeUserLiker(userEmail)) { // if the user has liked it
				this.numberOfLikes--;
			}
		}
	}

	private synchronized boolean removeUserLiker(String userEmail) {
		if (userEmail != null && !userEmail.isEmpty() && userLikers.contains(userEmail)) {
			userLikers.remove(userEmail);
			return true;
		} else {
			return false;
		}
	}

	public synchronized CopyOnWriteArrayList<String> getUserLikers() {
		CopyOnWriteArrayList<String> copy = new CopyOnWriteArrayList<>();
		copy.addAll(userLikers);
		return copy;
	}

	public synchronized String getPlaceName() {
		return placeName;
	}

	public synchronized void setPlaceName(String placeName) {
		if (placeName != null && !placeName.isEmpty())
			this.placeName = placeName;
	}

	public synchronized void setNumberOfLikes(int numberOfLikes) {
		if (numberOfLikes >= 0)
			this.numberOfLikes = numberOfLikes;
	}

	public synchronized boolean hasVideo() {
		return hasVideo;
	}

	public synchronized void setHasVideo(boolean hasVideo) {
		this.hasVideo = hasVideo;
	}

	public synchronized String getVideo() {
		return video;
	}

	public synchronized void setVideo(String video) {
		if (video != null && !video.isEmpty()) {
			this.video = video;
			setHasVideo(true);
		} else {
			setHasVideo(false);
		}
	}

	public synchronized long getId() {
		return id;
	}

	public synchronized void setId(long id) {
		if (id >= 0)
			this.id = id;
	}

}
