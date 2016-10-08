package com.springframework.model;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class User {

	private String email;
	private String password;
	private String firstName;
	private String lastName;
	private String description;
	private String profilePicture;
	private CopyOnWriteArrayList<String> addedPlaces;
	private CopyOnWriteArrayList<String> visitedPlaces;
	private CopyOnWriteArrayList<String> followedUsers; // emails of users that
														// are
														// followed
	private ConcurrentHashMap<String, Chat> chatHistory; // name of second user
															// -> chat object
	private double rating;
	private int timesLiked; // how many times the user is liked

	public User(String email, String password, String firstName, String lastName, String description,
			String profilePicture, double rating, int timesLiked) { // basic; no
																	// collections
		setEmail(email);
		setPassword(password);
		setFirstName(firstName);
		setLastName(lastName);
		setDescription(description);
		setProfilePicture(profilePicture);
		setRating(rating);
		setTimesLiked(timesLiked);
		this.addedPlaces = new CopyOnWriteArrayList<>();
		this.visitedPlaces = new CopyOnWriteArrayList<>();
		this.followedUsers = new CopyOnWriteArrayList<>();
		this.chatHistory = new ConcurrentHashMap<String, Chat>();
	}

	public User(String email, String password, String firstName, String lastName, String description,
			String profilePicture, CopyOnWriteArrayList<String> addedPlaces, CopyOnWriteArrayList<String> visitedPlaces,
			CopyOnWriteArrayList<String> followedUsers, ConcurrentHashMap<String, Chat> chatHistory, double rating,
			int timesLiked) { // extended creation; with collections
		setFirstName(firstName);
		setLastName(lastName);
		setPassword(password);
		setEmail(email);
		setDescription(description);
		setProfilePicture(profilePicture);
		setAddedPlaces(addedPlaces);
		setVisitedPlaces(visitedPlaces);
		setFollowedUsers(followedUsers);
		setChatHistory(chatHistory);
		setRating(rating);
		setTimesLiked(timesLiked);
	}

	public synchronized void addPlace(String destinationName) {
		if (destinationName != null && !destinationName.isEmpty() && !addedPlaces.contains(destinationName)) {
			this.visitedPlaces.add(destinationName);
			this.addedPlaces.add(destinationName);
		}

	}

	public synchronized void removePlace(String destinationName) {
		if (destinationName != null && !destinationName.isEmpty() && addedPlaces.contains(destinationName))
			this.addedPlaces.remove(destinationName);
	}

	public synchronized void addVisitedPlace(String destinationName) {
		if (destinationName != null && !destinationName.isEmpty() && !visitedPlaces.contains(destinationName))
			this.visitedPlaces.add(destinationName);
	}

	public synchronized void removeVisitedPlace(String destinationName) {
		if (destinationName != null && !destinationName.isEmpty() && visitedPlaces.contains(destinationName))
			this.visitedPlaces.remove(destinationName);
	}

	public synchronized void follow(String followedUserEmail) {
		if (followedUserEmail != null && !followedUserEmail.isEmpty() && !followedUsers.contains(followedUserEmail))
			followedUsers.add(followedUserEmail);
	}

	public synchronized void unFollow(String followedUserEmail) {
		if (followedUserEmail != null && !followedUserEmail.isEmpty() && followedUsers.contains(followedUserEmail))
			followedUsers.remove(followedUserEmail);
	}

	public synchronized void addChat(Chat chat) {
		if (chat != null && !chatHistory.containsKey(chat.getUser2()))
			chatHistory.put(chat.getUser2(), chat);
	}

	public synchronized void removeChat(Chat chat) {
		if (chat != null && chatHistory.containsKey(chat.getUser2()))
			chatHistory.remove(chat);
	}

	public synchronized String getProfilePicture() {
		return profilePicture;
	}

	public synchronized void setProfilePicture(String profilePicture) {
		if (profilePicture != null && !profilePicture.isEmpty())
			this.profilePicture = profilePicture;
	}

	public synchronized CopyOnWriteArrayList<String> getVisitedPlaces() {
		CopyOnWriteArrayList<String> copy = new CopyOnWriteArrayList<>();
		copy.addAll(visitedPlaces);
		return copy;
	}

	public synchronized void setVisitedPlaces(CopyOnWriteArrayList<String> visitedPlaces) {
		if (visitedPlaces != null)
			this.visitedPlaces = visitedPlaces;
	}

	public synchronized CopyOnWriteArrayList<String> getFollowedUsers() {
		CopyOnWriteArrayList<String> copy = new CopyOnWriteArrayList<String>();
		copy.addAll(followedUsers);
		return copy;
	}

	public synchronized void setFollowedUsers(CopyOnWriteArrayList<String> followedUsers) {
		if (followedUsers != null)
			this.followedUsers = followedUsers;
	}

	public synchronized ConcurrentHashMap<String, Chat> getChatHistory() {
		ConcurrentHashMap<String, Chat> copy = new ConcurrentHashMap<>();
		copy.putAll(chatHistory);
		return copy;
	}

	public synchronized void setChatHistory(ConcurrentHashMap<String, Chat> chatHistory) {
		if (chatHistory != null)
			this.chatHistory = chatHistory;
	}

	public synchronized int getTimesLiked() {
		return timesLiked;
	}

	public synchronized void setTimesLiked(int timesLiked) {
		if (timesLiked >= 0)
			this.timesLiked = timesLiked;
	}

	public synchronized double getRating() {
		return rating;
	}

	public synchronized void setRating(double rating) {
		if (rating >= 0)
			this.rating = rating;
	}

	public synchronized void setAddedPlaces(CopyOnWriteArrayList<String> addedPlaces) {
		if (addedPlaces != null)
			this.addedPlaces = addedPlaces;
	}

	public synchronized String getFirstName() {
		return firstName;
	}

	public synchronized String getLastName() {
		return lastName;
	}

	public synchronized String getPassword() {
		return password;
	}

	public synchronized String getEmail() {
		return email;
	}

	public synchronized String getDescription() {
		return description;
	}

	public synchronized CopyOnWriteArrayList<String> getAddedPlaces() {
		CopyOnWriteArrayList<String> copy = new CopyOnWriteArrayList<>();
		copy.addAll(addedPlaces);
		return addedPlaces;
	}

	public synchronized void setFirstName(String firstName) {
		if (firstName != null && !firstName.isEmpty())
			this.firstName = firstName;
	}

	public synchronized void setLastName(String lastName) {
		if (lastName != null && !lastName.isEmpty())
			this.lastName = lastName;
	}

	public synchronized void setPassword(String password) {
		if (password != null && !password.isEmpty())
			this.password = password;
	}

	public synchronized void setEmail(String email) {
		if (email != null && !email.isEmpty())
			this.email = email;
	}

	public synchronized void setDescription(String description) {
		if (description != null && !description.isEmpty())
			this.description = description;
	}

}
