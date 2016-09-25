package models;

import java.util.ArrayList;

import exceptions.InvalidAuthorException;
import exceptions.InvalidDataException;

public class Comment {

	private String authorEmail;
	private String placeName;
	private String text;
	private int numberOfLikes;
	private ArrayList<String> userLikers; // List of users who like the comment

	public Comment(String authorEmail, String placeName, String text, int numberOfLikes)
			throws InvalidDataException, InvalidAuthorException {
		setAuthorEmail(authorEmail);
		this.placeName = placeName;
		this.setText(text);
		this.numberOfLikes = numberOfLikes;
		this.userLikers = new ArrayList<>();
	}

	public void setAuthorEmail(String authorEmail) {
		synchronized (this) {
			if (authorEmail != null)
				this.authorEmail = authorEmail;
		}
	}

	public String getAuthorEmail() {
		synchronized (this) {
			return authorEmail;
		}
	}

	public String getText() {
		synchronized (this) {
			return text;
		}
	}

	private void setText(String text) throws InvalidDataException {
		synchronized (this) {
			if (text != null && !text.isEmpty()) {
				this.text = text;
			} else {
				throw new InvalidDataException();
			}
		}
	}

	public int getNumberOfLikes() {
		synchronized (this) {
			return numberOfLikes;
		}
	}

	public void like(String userEmail) {
		synchronized (this) {
			if (userEmail != null && !userEmail.isEmpty()) {
				if (addUserLiker(userEmail)) { // if the user has not liked yet
					this.numberOfLikes++;
				}
			}
		}
	}

	public ArrayList<String> getUserLikers() {
		synchronized (this) {
			ArrayList<String> copy = new ArrayList<>();
			copy.addAll(userLikers);
			return copy;
		}
	}

	private boolean addUserLiker(String userEmail) {
		synchronized (this) {
			if (userEmail != null && !userEmail.isEmpty()) {
				for (int i = 0; i < userLikers.size(); i++) {
					if (userLikers.get(i) == userEmail) {
						return false;
					}
				}
				userLikers.add(userEmail);
				return true;
			} else {
				return false;
			}
		}
	}

	public String getPlaceName() {
		synchronized (this) {
			return placeName;
		}
	}

	public void setPlaceName(String placeName) {
		synchronized (this) {
			if (placeName != null && !placeName.isEmpty())
				this.placeName = placeName;
		}
	}

	public void setNumberOfLikes(int numberOfLikes) {
		synchronized (this) {
			if (numberOfLikes > -1)
				this.numberOfLikes = numberOfLikes;
		}
	}

}
