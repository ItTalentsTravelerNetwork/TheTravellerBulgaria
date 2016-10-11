package com.springframework.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

import com.springframework.exceptions.InvalidAuthorException;
import com.springframework.exceptions.InvalidDataException;


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
	private int numberOfDislikes;
	private CopyOnWriteArrayList<String> userDislikers;
	private LocalDateTime dateAndTime;
	private boolean hasVideo;
	private String video;

	public Comment(String authorEmail, String placeName, String text, int numberOfLikes, int numberOfDislikes, String dateAndTime,
			String video) throws InvalidDataException { // without id
		setAuthorEmail(authorEmail);
		setPlaceName(placeName);
		this.setText(text);
		setNumberOfLikes(numberOfLikes);
		setNumberOfDislikes(numberOfDislikes);
		setUserLikers(new CopyOnWriteArrayList<>());
		setUserDislikers(new CopyOnWriteArrayList<>());
		LocalDateTime date = LocalDateTime.parse(dateAndTime, DATE_AND_TIME_FORMAT);
		setDateAndTime(date);
		setVideo(video); // can be null -> hasVideo=false
	}

	public Comment(long id, String authorEmail, String placeName, String text, int numberOfLikes, int numberOfDislikes, String dateAndTime,
			String video) throws InvalidDataException { // with id
		setId(id);
		setAuthorEmail(authorEmail);
		setPlaceName(placeName);
		this.setText(text);
		setNumberOfLikes(numberOfLikes);
		setNumberOfDislikes(numberOfDislikes);
		setUserLikers(new CopyOnWriteArrayList<>());
		setUserDislikers(new CopyOnWriteArrayList<>());
		LocalDateTime date = LocalDateTime.parse(dateAndTime, DATE_AND_TIME_FORMAT);
		setDateAndTime(date);
		setVideo(video); // can be null -> hasVideo=false
	}

//	public Comment(String authorEmail, String placeName, String text, int numberOfLikes, ArrayList<String> userLikers,
//			Date date) throws InvalidDataException, InvalidAuthorException {
//
//	}

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
	
	public synchronized void setUserDislikers(CopyOnWriteArrayList<String> userDislikers) {
		if (userDislikers != null)
			this.userDislikers = userDislikers;
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
	
	public synchronized int getNumberOfDislikes() {
		return numberOfDislikes;
	}

	public synchronized boolean like(String userEmail) {
		if (userEmail != null && !userEmail.isEmpty()) { // if correct data
			if (!userLikers.contains(userEmail)) { // if not liked yet
				if (userDislikers.contains(userEmail)) { // if already disliked
					userDislikers.remove(userEmail);
					numberOfDislikes--;
				}
				userLikers.add(userEmail);
				numberOfLikes++;
				return true;
			}
		}
		return false;
	}

	public synchronized boolean unlike(String userEmail) {
		if (userEmail != null && !userEmail.isEmpty()) {
			if (userLikers.contains(userEmail)) {
				userLikers.remove(userEmail);
				numberOfLikes--;
				return true;
			}
		}
		return false;
	}

	public synchronized boolean dislike(String userEmail) {
		if (userEmail != null && !userEmail.isEmpty()) { // if correct data
			if (!userDislikers.contains(userEmail)) { // if not disliked yet
				if (userLikers.contains(userEmail)) { // if already liked
					userLikers.remove(userEmail);
					numberOfLikes--;
				}
				userDislikers.add(userEmail);
				numberOfDislikes++;
				return true;
			}
		}
		return false;
	}

	public synchronized boolean undislike(String userEmail) {
		if (userEmail != null && !userEmail.isEmpty()) {
			if (userDislikers.contains(userEmail)) {
				userDislikers.remove(userEmail);
				numberOfDislikes--;
				return true;
			}
		}
		return false;
	}

	public synchronized CopyOnWriteArrayList<String> getUserLikers() {
		CopyOnWriteArrayList<String> copy = new CopyOnWriteArrayList<>();
		copy.addAll(userLikers);
		return copy;
	}
	
	public synchronized CopyOnWriteArrayList<String> getUserDislikers() {
		CopyOnWriteArrayList<String> copy = new CopyOnWriteArrayList<>();
		copy.addAll(userDislikers);
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
	
	public synchronized void setNumberOfDislikes(int numberOfDislikes) {
		if (numberOfDislikes >= 0)
			this.numberOfDislikes = numberOfDislikes;
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
