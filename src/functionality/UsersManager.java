package functionality;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;

import dbModels.UserDao;
import exceptions.InvalidCoordinatesException;
import exceptions.InvalidDataException;
import models.Activity;
import models.Comment;
import models.Destination;
import models.Destination.Category;
import models.PlaceToEat;
import models.PlaceToSleep;
import models.Sight;
import models.User;

public class UsersManager {

	public static final String PATH_TO_LOG = "log.txt";
	private static final DateTimeFormatter DATE_AND_TIME_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	private static UsersManager instance; // Singleton
	private ConcurrentHashMap<String, User> registerredUsers; // user email and
																// user

	private UsersManager() {
		registerredUsers = new ConcurrentHashMap<>();
		Set<User> tempAllUsers = UserDao.getInstance().getAllUsers();
		for (User u : tempAllUsers) { // adds all users
										// from DB to
										// collection
			registerredUsers.put(u.getEmail(), u); // add user to cache
		}
	}

	public static synchronized UsersManager getInstance() {
		if (instance == null) {
			instance = new UsersManager();
		}
		return instance;
	}

	public boolean validateUser(String email, String password) { // validation
																	// of login
																	// input
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

	public void registerUser(String firstName, String lastName, String email, String password, String description,
			String profilePic) {
		User user = new User(firstName, lastName, password, email, description, profilePic,
				new CopyOnWriteArrayList<>(), new CopyOnWriteArrayList<>(), new CopyOnWriteArrayList<>(),
				new ConcurrentHashMap<>(), 0, 0);
		registerredUsers.put(email, user); // adds the new user to the
											// collection
		UserDao.getInstance().saveUserToDB(user); // saves user to DB
	}

	public void addDestination(User user, String destinationName) {
		user.getAddedPlaces().add(destinationName);
	}

	public boolean addComment(String userEmail, String destinationName, String text, String video)
			throws InvalidDataException {
		if (!registerredUsers.containsKey(userEmail)) {
			return false;
		}
		String dateAndTimeString = LocalDateTime.now().format(DATE_AND_TIME_FORMAT); // date&Time
																						// to
																						// string
		Comment comment = new Comment(userEmail, destinationName, text, 0, dateAndTimeString, video); // creating
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

	public boolean addDestination(User user, Destination destination) throws InvalidCoordinatesException {
		String destName = destination.getName();
		String destDescription = destination.getDescription();
		double destLattitude = destination.getLocation().getLattitude();
		double destLongitude = destination.getLocation().getLongitude();
		String destMainPicture = destination.getMainPicture();
		ConcurrentSkipListSet<PlaceToSleep> placesToSleep = destination.getPlacesToSleep();
		ConcurrentSkipListSet<PlaceToEat> placesToEat = destination.getPlacesToEat();
		Category category = destination.getCategory();
		CopyOnWriteArrayList<String> pictures = destination.getPictures();
		CopyOnWriteArrayList<String> videos = destination.getVideos();
		ConcurrentSkipListSet<Activity> activities = destination.getActivities();
		ConcurrentSkipListSet<Sight> sights = destination.getSights();
		if (DestinationsManager.getInstance().addDestination(user, destName, destDescription, destLattitude,
				destLongitude, destMainPicture, placesToSleep, placesToEat, category, pictures, videos, activities,
				sights)) {
			addDestination(user, destName);
			return true;
		} else {
			return false;
		}
	}

	public boolean updateUserInfo(String email, String password, String firstName, String lastName, String description,
			String profilePic) {
		if (!registerredUsers.containsKey(email)) { // no such user
			return false;
		}
		User user = registerredUsers.get(email); // takes the user with the
													// input email and updates
													// their fields
		user.setPassword(password);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setDescription(description);
		user.setProfilePic(profilePic);
		UserDao.getInstance().updateUserInDB(email, password, firstName, lastName, description, profilePic); // updates
		// the
		// DB
		// user:
		return true;
	}

	public boolean addVidsitedDestination(User user, String destinationName) {
		if (!DestinationsManager.getInstance().chechDestinationInCache(destinationName)) {
			user.addVisitedPlace(destinationName);
			//TODO add in DB
			dasdasas asdas
			return true;
		}
		return false;
	}

	public boolean removeVisitedDestination(User user, String destinationName) {
		if (DestinationsManager.getInstance().chechDestinationInCache(destinationName)) {
			if (user.getVisitedPlaces().contains(destinationName)) {
				user.removeVisitedPlace(destinationName);
				//TODO remove in DB
				asdsa
				return true;
			}
		}
		return false;
	}

	public boolean addToFollowedUsers(User user, String followedUserEmail) {
		if (!user.getFollowedUsers().contains(followedUserEmail)) {
			user.follow(followedUserEmail);
			//TODO add in DB
			dsadsad
			return true;
		}
		return false;
	}

	public boolean RemoveFromFollowedUsers(User user, String followedUserEmail) {
		if (user.getFollowedUsers().contains(followedUserEmail)) {
			user.unFollow(followedUserEmail);
			//TODO remove in DB
			dsadsad
			return true;
		}
		return false;
	}

	public User logIn(String email, String password) {
		if (validateUser(email, password)) { // valid input
			User user = registerredUsers.get(email);
			return user;
		}
		return null;
	}

	public User getUserFromCache(String userEmail) {
		if (!registerredUsers.containsKey(userEmail)) {
			return null; // no such user
		}
		return registerredUsers.get(userEmail); // returns the user
	}

	public ConcurrentHashMap<String, User> getRegisterredUsers() {
		ConcurrentHashMap<String, User> copy = new ConcurrentHashMap<>();
		copy.putAll(registerredUsers);
		return copy;
	}

	public boolean addUserToComment(String userEmail, Comment comment) {
		if (registerredUsers.containsKey(userEmail)) {
			comment.setAuthorEmail(userEmail);
			return true;
		}
		return false;
	}

	public void likeAComment(String userEmail, Comment comment) {
		CopyOnWriteArrayList<String> userLikersOfComment = comment.getUserLikers(); // all
																					// the
																					// users
																					// who
																					// like
																					// the
																					// comment
		for (int i = 0; i < userLikersOfComment.size(); i++) {
			if (userLikersOfComment.get(i) == userEmail) { // if the current
															// user
															// has already liked
															// the comment
				return; // do nothing
			}
		}
		comment.like(userEmail); // the comment is liked

	}

	private static void printToLog(String message) {
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