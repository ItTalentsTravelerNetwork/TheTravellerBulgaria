package com.springframework.model;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = { "password" })
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

	public void addPlace(String destinationName) {
		if (destinationName != null && !destinationName.isEmpty() && !addedPlaces.contains(destinationName)) {
			this.visitedPlaces.add(destinationName);
			this.addedPlaces.add(destinationName);
		}

	}

	public void removePlace(String destinationName) {
		if (destinationName != null && !destinationName.isEmpty() && addedPlaces.contains(destinationName))
			this.addedPlaces.remove(destinationName);
	}

	public void addVisitedPlace(String destinationName) {
		if (destinationName != null && !destinationName.isEmpty() && !visitedPlaces.contains(destinationName))
			this.visitedPlaces.add(destinationName);
	}

	public void removeVisitedPlace(String destinationName) {
		if (destinationName != null && !destinationName.isEmpty() && visitedPlaces.contains(destinationName))
			this.visitedPlaces.remove(destinationName);
	}

	public void follow(String followedUserEmail) {
		if (followedUserEmail != null && !followedUserEmail.isEmpty() && !followedUsers.contains(followedUserEmail))
			followedUsers.add(followedUserEmail);
	}

	public void unFollow(String followedUserEmail) {
		if (followedUserEmail != null && !followedUserEmail.isEmpty() && followedUsers.contains(followedUserEmail))
			followedUsers.remove(followedUserEmail);
	}

	public void addChat(Chat chat) {
		if (chat != null && !chatHistory.containsKey(chat.getUser2()))
			chatHistory.put(chat.getUser2(), chat);
	}

	public void removeChat(Chat chat) {
		if (chat != null && chatHistory.containsKey(chat.getUser2()))
			chatHistory.remove(chat);
	}

	public String getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(String profilePicture) {
		if (profilePicture != null && !profilePicture.isEmpty())
			this.profilePicture = profilePicture;
	}

	public CopyOnWriteArrayList<String> getVisitedPlaces() {
		CopyOnWriteArrayList<String> copy = new CopyOnWriteArrayList<>();
		copy.addAll(visitedPlaces);
		return copy;
	}

	public void setVisitedPlaces(CopyOnWriteArrayList<String> visitedPlaces) {
		if (visitedPlaces != null)
			this.visitedPlaces = visitedPlaces;
	}

	public CopyOnWriteArrayList<String> getFollowedUsers() {
		CopyOnWriteArrayList<String> copy = new CopyOnWriteArrayList<String>();
		copy.addAll(followedUsers);
		return copy;
	}

	public void setFollowedUsers(CopyOnWriteArrayList<String> followedUsers) {
		if (followedUsers != null)
			this.followedUsers = followedUsers;
	}

	public ConcurrentHashMap<String, Chat> getChatHistory() {
		ConcurrentHashMap<String, Chat> copy = new ConcurrentHashMap<>();
		copy.putAll(chatHistory);
		return copy;
	}

	public void setChatHistory(ConcurrentHashMap<String, Chat> chatHistory) {
		if (chatHistory != null)
			this.chatHistory = chatHistory;
	}

	public int getTimesLiked() {
		return timesLiked;
	}

	public void setTimesLiked(int timesLiked) {
		if (timesLiked >= 0)
			this.timesLiked = timesLiked;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		if (rating >= 0)
			this.rating = rating;
	}

	public void setAddedPlaces(CopyOnWriteArrayList<String> addedPlaces) {
		if (addedPlaces != null)
			this.addedPlaces = addedPlaces;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}

	public String getDescription() {
		return description;
	}

	public CopyOnWriteArrayList<String> getAddedPlaces() {
		CopyOnWriteArrayList<String> copy = new CopyOnWriteArrayList<>();
		copy.addAll(addedPlaces);
		return addedPlaces;
	}

	public void setFirstName(String firstName) {
		if (firstName != null && !firstName.isEmpty())
			this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		if (lastName != null && !lastName.isEmpty())
			this.lastName = lastName;
	}

	public void setPassword(String password) {
		if (password != null && !password.isEmpty())
			this.password = password;
	}

	public void setEmail(String email) {
		if (email != null && !email.isEmpty())
			this.email = email;
	}

	public void setDescription(String description) {
		if (description != null && !description.isEmpty())
			this.description = description;
	}

}
