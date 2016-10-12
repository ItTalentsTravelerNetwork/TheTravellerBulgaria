package com.springframework.functionality;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.springframework.dbModels.UserDao;
import com.springframework.exceptions.InvalidCoordinatesException;
import com.springframework.exceptions.InvalidDataException;
import com.springframework.model.Comment;
import com.springframework.model.Destination;
import com.springframework.model.User;

public class UsersManager {

	public static final String PATH_TO_LOG = "log.txt";
	private static final DateTimeFormatter DATE_AND_TIME_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	private static UsersManager instance; // Singleton
	private ConcurrentHashMap<String, User> registerredUsers; // user email and
	private CopyOnWriteArrayList<String> userVisitors; // user

	private UsersManager() {
		registerredUsers = new ConcurrentHashMap<>();
		Set<User> tempAllUsers = UserDao.getInstance().getAllUsers();
		for (User u : tempAllUsers) { // adds all users
										// from DB to
										// collection
			registerredUsers.put(u.getEmail(), u); // add user to cache
		}
		fillVisitedPlacesToUsers(); // fill visited destinations to users
		fillFollowedToUsers(); // fill followed to the users' lists
		userVisitors = UserDao.getInstance().getUserVisitors();
	}

	public static synchronized UsersManager getInstance() {
		if (instance == null) {
			instance = new UsersManager();
		}
		return instance;
	}

	public synchronized boolean validateUser(String email, String password) { // validation
																				// of
																				// login
																				// input
		if (!registerredUsers.containsKey(email)) { // no such user
			return false;
		}
		return registerredUsers.get(email).getPassword().equals(MD5PasswordConvert(password)); // returns
		// the
		// result
		// of
		// the
		// comparing
		// of
		// the
		// login
		// pass
		// and
		// the
		// pass
		// in
		// the
		// collection
	}

	public synchronized boolean validateLoggedInUser(String email, String password) { // validation
		// of already logged in
		// user
		if (!registerredUsers.containsKey(email)) { // no such user
			return false;
		}
		return registerredUsers.get(email).getPassword().equals(password); // returns
																			// the
																			// result
																			// of
																			// the
																			// comparing
																			// of
																			// the
																			// login
																			// pass
																			// and
																			// the
																			// pass
																			// in
																			// the
																			// collection
	}

	public synchronized void registerUser(String email, String password, String firstName, String lastName,
			String description, String profilePicture) {
		User user = new User(email, MD5PasswordConvert(password), firstName, lastName, description, profilePicture,
				new CopyOnWriteArrayList<>(), new CopyOnWriteArrayList<>(), new CopyOnWriteArrayList<>(),
				new ConcurrentHashMap<>(), 0, 0);
		registerredUsers.put(email, user); // adds the new user to the
											// collection
		UserDao.getInstance().saveUserToDB(user); // saves
													// user
													// to
													// DB
	}

	public synchronized boolean updateUserInfo(String email, String password, String firstName, String lastName,
			String description, String profilePicture) {
		if (registerredUsers.containsKey(email)) { // the user exists
			User user = registerredUsers.get(email); // takes the user with the
														// input email and
														// updates
														// their fields
			user.setPassword(MD5PasswordConvert(password));
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setDescription(description);
			user.setProfilePicture(profilePicture);
			if (UserDao.getInstance().updateUserInDB(email, password, firstName, lastName, description,
					profilePicture)) { // updates
										// the
										// DB
										// user
				return true;
			}
		}
		return false;
	}

	public synchronized boolean deleteUser(User user) {
		if (registerredUsers.containsKey(user.getEmail())) {
			// Delete from cache:
			// Removing all user data from destinations and comments cache:
			DestinationsManager.getInstance().deleteAllUserData(user);
			// Removing from other users' following list:
			for (Map.Entry<String, User> entry : registerredUsers.entrySet()) { // going
																				// through
																				// all
																				// the
																				// registerred
																				// users
				User currentUser = entry.getValue();
				removeFromFollowedUsers(currentUser, user.getEmail()); // if
																		// current
																		// user
																		// has
																		// followed
																		// first
																		// user
																		// the
																		// action
																		// undone
			}
			// Remove from DB:
			if (UserDao.getInstance().deleteUserFromDB(user)) {
				return true;
			}
		}
		return false;
	}

	public synchronized boolean addDestination(User user, Destination destination) throws InvalidCoordinatesException {
		String destName = destination.getName();
		String destDescription = destination.getDescription();
		double destLattitude = destination.getLocation().getLattitude();
		double destLongitude = destination.getLocation().getLongitude();
		String destMainPicture = destination.getMainPicture();
		String category = destination.getCategory().toString();
		if (DestinationsManager.getInstance().addDestination(user, destName, destDescription, destLattitude,
				destLongitude, destMainPicture, category)) {

			userVisitors.add(0, user.getEmail());
			userVisitors.add(1, destName);
			userVisitors.add(2, LocalDateTime.now().format(DATE_AND_TIME_FORMAT));
			return true;
		} else {
			return false;
		}
	}

	public synchronized void addDestinationToUser(String userEmail, String destinationName) {
		User user = this.getUserFromCache(userEmail);
		user.addPlace(destinationName);
		userVisitors.add(0, user.getEmail());
		userVisitors.add(1, destinationName);
		userVisitors.add(2, LocalDateTime.now().format(DATE_AND_TIME_FORMAT));
	}

	public synchronized boolean addComment(String userEmail, String destinationName, String text, String video)
			throws InvalidDataException {
		if (!registerredUsers.containsKey(userEmail)) {
			return false;
		}
		String dateAndTimeString = LocalDateTime.now().format(DATE_AND_TIME_FORMAT); // date&Time
																						// to
																						// string
		Comment comment = new Comment(userEmail, destinationName, text, 0, 0, dateAndTimeString, video); // creating
																											// a
																											// comment
		CommentsManager.getInstance().saveComment(comment);
		DestinationsManager.getInstance().getDestinationFromCache(destinationName).addComment(comment); // adds
																										// the
																										// comment
																										// to
																										// the
																										// destination
		return true;
	}

	public synchronized boolean addVidsitedDestination(User user, String destinationName) {
		if (DestinationsManager.getInstance().chechDestinationInCache(destinationName)) {
			if (user.getVisitedPlaces().contains(destinationName)) {
				return false;
			}
			user.addVisitedPlace(destinationName);
			this.userVisitors.add(0, user.getEmail());
			this.userVisitors.add(1, destinationName);
			this.userVisitors.add(2, LocalDateTime.now().format(DATE_AND_TIME_FORMAT));
			// add in DB
			if (UserDao.getInstance().addVisitedDestinationToDB(user, destinationName)) {
				return true;
			}
		}
		return false;
	}

	public synchronized boolean removeVisitedDestination(User user, String destinationName) {
		if (DestinationsManager.getInstance().chechDestinationInCache(destinationName)) {
			if (user.getVisitedPlaces().contains(destinationName)) {
				user.removeVisitedPlace(destinationName);
				// remove in DB
				if (UserDao.getInstance().removeVisitedDestinationFromDB(user, destinationName)) {
					return true;
				}
			}
		}
		return false;
	}

	public synchronized boolean addToFollowedUsers(User user, String followedUserEmail) {
		if (registerredUsers.contains(user) && registerredUsers.containsKey(followedUserEmail)) {
			if (!user.getFollowedUsers().contains(followedUserEmail)) {
				user.follow(followedUserEmail);
				// add in DB
				if (UserDao.getInstance().addToFollowedUsers(user, followedUserEmail)) {
					return true;
				}
			}
		}
		return false;
	}

	public synchronized boolean removeFromFollowedUsers(User user, String followedUserEmail) {
		if (registerredUsers.contains(user) && registerredUsers.containsKey(followedUserEmail)) {
			if (user.getFollowedUsers().contains(followedUserEmail)) {
				user.unFollow(followedUserEmail);
				// remove in DB
				if (UserDao.getInstance().removeFromFollowedUsers(user, followedUserEmail)) {
					return true;
				}
			}
		}
		return false;
	}

	public synchronized User logIn(String email, String password) {
		if (validateUser(email, password)) { // valid input
			User user = registerredUsers.get(email);
			return user;
		}
		return null;
	}

	public synchronized User getUserFromCache(String userEmail) {
		if (!registerredUsers.containsKey(userEmail)) {
			return null; // no such user
		}
		return registerredUsers.get(userEmail); // returns the user
	}

	public synchronized ConcurrentHashMap<String, User> getRegisterredUsers() {
		ConcurrentHashMap<String, User> copy = new ConcurrentHashMap<>();
		copy.putAll(registerredUsers);
		return copy;
	}

	public synchronized boolean addUserToComment(String userEmail, Comment comment) {
		if (registerredUsers.containsKey(userEmail)) {
			comment.setAuthorEmail(userEmail);
			return true;
		}
		return false;
	}

	public synchronized boolean likeAComment(String userEmail, Comment comment) {
		if (registerredUsers.containsKey(userEmail)) { // if user exists
			if (CommentsManager.getInstance().likeComment(comment.getId(), userEmail)) {
				return true; // the comment is liked
			}
		}
		return false; // already liked or incorrect data
	}

	public synchronized boolean unlikeAComment(String userEmail, Comment comment) {
		if (registerredUsers.containsKey(userEmail)) { // if user exists
			if (CommentsManager.getInstance().unlikeComment(comment.getId(), userEmail)) {
				return true; // the comment is unliked
			}
		}
		return false; // not liked yet or incorrect data
	}

	public synchronized boolean dislikeAComment(String userEmail, Comment comment) {
		if (registerredUsers.containsKey(userEmail)) { // if user exists
			if (CommentsManager.getInstance().dislikeComment(comment.getId(), userEmail)) {
				return true; // the comment is disliked
			}
		}
		return false; // already disliked or incorrect data
	}

	public synchronized boolean undislikeAComment(String userEmail, Comment comment) {
		if (registerredUsers.containsKey(userEmail)) { // if user exists
			if (CommentsManager.getInstance().undislikeComment(comment.getId(), userEmail)) {
				return true; // the comment is undisliked
			}
		}
		return false; // not disliked yet or incorrect data
	}

	private synchronized void fillVisitedPlacesToUsers() {
		if (UserDao.getInstance().getAllVisitedPlacesFromDB() != null) {
			for (Map.Entry<String, ArrayList<String>> entry : UserDao.getInstance().getAllVisitedPlacesFromDB()
					.entrySet()) { // for
				// each
				// (destination
				// name->list
				// of
				// users)
				for (String userEmail : entry.getValue()) { // for each user

					registerredUsers.get(userEmail).addVisitedPlace(entry.getKey()); // add
																						// visited
																						// place
																						// to
																						// user
				}
			}
		}
	}

	public CopyOnWriteArrayList<String> getUserVisitors() {
		CopyOnWriteArrayList<String> copy = new CopyOnWriteArrayList<>();
		copy.addAll(userVisitors);
		return copy;
	}

	private synchronized void fillFollowedToUsers() {
		if (UserDao.getInstance().getAllFollowersFromDB() != null) {
			for (Map.Entry<String, CopyOnWriteArrayList<String>> entry : UserDao.getInstance().getAllFollowersFromDB()
					.entrySet()) { // for
									// each
									// (follower->list
									// of
									// followed)
				if (registerredUsers.containsKey(entry.getKey())) { // if user
																	// is in
																	// cache
					registerredUsers.get(entry.getKey()).setFollowedUsers(entry.getValue()); // adds
																								// followed
																								// users
																								// to
																								// current
																								// user
				}
			}
		}
	}

	private String MD5PasswordConvert(String password) {
		MessageDigest md;
		StringBuffer encodedPassword = null;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());
			encodedPassword = new StringBuffer();
			byte byteData[] = md.digest();
			for (int i = 0; i < byteData.length; i++) {
				encodedPassword.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return encodedPassword.toString();

	}

	private synchronized static void printToLog(String message) {
		File file = new File(PATH_TO_LOG);
		FileOutputStream out;
		try {
			out = new FileOutputStream(file, true);
			PrintStream logPrint = new PrintStream(out);
			logPrint.println(message);
			logPrint.close();
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}

}