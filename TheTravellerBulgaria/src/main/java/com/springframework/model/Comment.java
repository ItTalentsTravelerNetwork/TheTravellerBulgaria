package com.springframework.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CopyOnWriteArrayList;

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

	public Comment(String authorEmail, String placeName, String text, int numberOfLikes, int numberOfDislikes,
			String dateAndTime, String video) throws InvalidDataException { // without
																			// id
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

	public Comment(long id, String authorEmail, String placeName, String text, int numberOfLikes, int numberOfDislikes,
			String dateAndTime, String video) throws InvalidDataException { // with
																			// id
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

	public void setAuthorEmail(String authorEmail) {
		if (authorEmail != null)
			this.authorEmail = authorEmail;
	}

	public String getAuthorEmail() {
		return authorEmail;
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

	public void setUserLikers(CopyOnWriteArrayList<String> userLikers) {
		if (userLikers != null)
			this.userLikers = userLikers;
	}

	public void setUserDislikers(CopyOnWriteArrayList<String> userDislikers) {
		if (userDislikers != null)
			this.userDislikers = userDislikers;
	}

	public String getText() {
		return text;
	}

	private void setText(String text) throws InvalidDataException {
		if (text != null && !text.isEmpty()) {
			this.text = text;
		} else {
			throw new InvalidDataException();
		}
	}

	public int getNumberOfLikes() {
		return numberOfLikes;
	}

	public int getNumberOfDislikes() {
		return numberOfDislikes;
	}

	public int like(String userEmail) {
		if (userEmail != null && !userEmail.isEmpty()) { // if correct data
			if (!userLikers.contains(userEmail)) { // if not liked yet
				if (userDislikers.contains(userEmail)) { // if already disliked
					userDislikers.remove(userEmail);
					numberOfDislikes--;
					userLikers.add(userEmail);
					numberOfLikes++;
					return 2; // removing dislike and adding like
				}
				userLikers.add(userEmail);
				numberOfLikes++;
				return 1; // only adding like
			}
		}
		return 3; // false data
	}

	public boolean unlike(String userEmail) {
		if (userEmail != null && !userEmail.isEmpty()) {
			if (userLikers.contains(userEmail)) {
				userLikers.remove(userEmail);
				numberOfLikes--;
				return true;
			}
		}
		return false;
	}

	public int dislike(String userEmail) {
		if (userEmail != null && !userEmail.isEmpty()) { // if correct data
			if (!userDislikers.contains(userEmail)) { // if not disliked yet
				if (userLikers.contains(userEmail)) { // if already liked
					userLikers.remove(userEmail);
					numberOfLikes--;
					userDislikers.add(userEmail);
					numberOfDislikes++;
					return 2; // adding dislike and removing like
				}
				userDislikers.add(userEmail);
				numberOfDislikes++;
				return 1; // only adding dislike
			}
		}
		return 3; // false data
	}

	public boolean undislike(String userEmail) {
		if (userEmail != null && !userEmail.isEmpty()) {
			if (userDislikers.contains(userEmail)) {
				userDislikers.remove(userEmail);
				numberOfDislikes--;
				return true;
			}
		}
		return false;
	}

	public CopyOnWriteArrayList<String> getUserLikers() {
		CopyOnWriteArrayList<String> copy = new CopyOnWriteArrayList<>();
		copy.addAll(userLikers);
		return copy;
	}

	public CopyOnWriteArrayList<String> getUserDislikers() {
		CopyOnWriteArrayList<String> copy = new CopyOnWriteArrayList<>();
		copy.addAll(userDislikers);
		return copy;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		if (placeName != null && !placeName.isEmpty())
			this.placeName = placeName;
	}

	public void setNumberOfLikes(int numberOfLikes) {
		if (numberOfLikes >= 0)
			this.numberOfLikes = numberOfLikes;
	}

	public void setNumberOfDislikes(int numberOfDislikes) {
		if (numberOfDislikes >= 0)
			this.numberOfDislikes = numberOfDislikes;
	}

	public boolean hasVideo() {
		return hasVideo;
	}

	public void setHasVideo(boolean hasVideo) {
		this.hasVideo = hasVideo;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		if (video != null && !video.isEmpty()) {
			this.video = video;
			setHasVideo(true);
		} else {
			setHasVideo(false);
		}
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		if (id >= 0)
			this.id = id;
	}

}
