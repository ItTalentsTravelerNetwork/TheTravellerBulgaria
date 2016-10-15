package com.springframework.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.springframework.exceptions.InvalidEmailException;
import com.springframework.exceptions.InvalidPasswordException;
import com.springframework.functionality.DestinationsManager;
import com.springframework.functionality.UsersManager;
import com.springframework.model.Destination;
import com.springframework.model.User;

@RestController
@MultipartConfig(maxFileSize = 200000000)
public class UserController {
	private static final String NAME_PATTERN = "^[A-Za-z]+$";
	private static final int MINIMUM_PASSWORD_LENGTH = 6;
	private static final String EMAIL_PATTERN = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9]+.[a-z.]+$";

	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	@ResponseBody
	public String register(@RequestParam("profilePic") MultipartFile multipartFile, HttpServletRequest request)
			throws IOException {
		String inputPassword = request.getParameter("userPassword");

		String firstName = request.getParameter("userFirstName");
		String lastName = request.getParameter("userLastName");
		String email = request.getParameter("userEmailAddress");
		String description = request.getParameter("userDescription");

		if (validateData(firstName, lastName, email, inputPassword)) {
			if (UsersManager.getInstance().getUserFromCache(email) != null) {
				return "USER EXISTS";
			}
			File dir = new File("userPics");
			if (!dir.exists()) {
				dir.mkdir();
			}
			File profilePicFile = new File(dir, email + "-profilePic." + multipartFile.getOriginalFilename());
			Files.copy(multipartFile.getInputStream(), profilePicFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			UsersManager.getInstance().registerUser(email, inputPassword, firstName, lastName, description,
					profilePicFile.getName());
			return "User Registration Successful!";
		}

		return "Registration failed!";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestParam("userEmail") String email, @RequestParam("userPassword") String password,
			HttpSession session) {
		User user = null;
		try {
			user = UsersManager.getInstance().logIn(email, password);
		} catch (InvalidEmailException e) {
			return "INVALID EMAIL";

		} catch (InvalidPasswordException e) {
			return "INVALID PASSWORD";
		}
		if (user != null) {
			session.setAttribute("user", user);
			return "SUCCESS";
		}
		return "FAILURE";
	}

	@RequestMapping(value = "/GetUserInfo", method = RequestMethod.GET)
	@ResponseBody
	public User checkForUser(HttpServletRequest request, HttpServletResponse response) {
		ControllerUtils.setHeaders(response);
		User user = (User) request.getSession().getAttribute("user");
		return user;
	}

	@RequestMapping(value = "/GetUser", method = RequestMethod.GET)
	@ResponseBody
	public User getUser(HttpServletRequest request, HttpServletResponse response) {
		ControllerUtils.setHeaders(response);
		User user = UsersManager.getInstance().getUserFromCache(request.getParameter("user"));
		return user;
	}

	@RequestMapping(value = "/GetProfilePicture", method = RequestMethod.GET)
	@ResponseBody
	public void getPicture(HttpServletRequest request, HttpServletResponse response) {
		String email = request.getParameter("email");
		User user = UsersManager.getInstance().getUserFromCache(email);
		File profilePicFile = new File("userPics", user.getProfilePicture());

		try {
			OutputStream out = response.getOutputStream();
			Files.copy(profilePicFile.toPath(), out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@RequestMapping(value = "/GetUserDestinations", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Destination> getUserDestination(HttpServletRequest request) {
		User user = UsersManager.getInstance().getUserFromCache((String) request.getParameter("user"));
		Map<String, Destination> userDestinations = new HashMap<>();
		for (String place : user.getVisitedPlaces()) {
			Destination dest = DestinationsManager.getInstance().getDestinationFromCache(place);
			userDestinations.put(place, dest);
		}
		return userDestinations;
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public void logOut(HttpServletRequest request) {
		if (request.getSession().getAttribute("user") != null) {
			request.getSession().removeAttribute("user");
		}
		request.getSession().invalidate();
	}

	@RequestMapping(value = "/Follow", method = RequestMethod.POST)
	public String follow(HttpServletRequest request) {
		String foreignUserEmail = request.getParameter("user");
		User user = (User) request.getSession().getAttribute("user");
		boolean isFollowed = UsersManager.getInstance().addToFollowedUsers(user, foreignUserEmail);
		if (isFollowed) {
			return "SUCCESS";
		} else {
			return "FAILURE";
		}
	}

	@RequestMapping(value = "/Unfollow", method = RequestMethod.POST)
	public String unFollow(HttpServletRequest request) {
		String foreignUserEmail = request.getParameter("user");
		User user = (User) request.getSession().getAttribute("user");
		boolean isFollowed = UsersManager.getInstance().removeFromFollowedUsers(user, foreignUserEmail);
		if (isFollowed) {
			return "SUCCESS";
		} else {
			return "FAILURE";
		}
	}

	@RequestMapping(value = "GetPlacesForNewsFeed", method = RequestMethod.GET)
	@ResponseBody
	public CopyOnWriteArrayList<Object> getPlacesForNewsFeed(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		CopyOnWriteArrayList<String> usersFollowed = user.getFollowedUsers();
		CopyOnWriteArrayList<Object> visitedPlacesByFollowedUsers = new CopyOnWriteArrayList<>();
		CopyOnWriteArrayList<String> usersVisitors = UsersManager.getInstance().getUserVisitors();
		for (int i = 0; i < usersVisitors.size(); i += 3) {
			for (int j = 0; j < usersFollowed.size(); j++) {
				if (usersFollowed.get(j).equals(usersVisitors.get(i))) {
					Destination dest = DestinationsManager.getInstance()
							.getDestinationFromCache(usersVisitors.get(i + 1));
					User visitor = UsersManager.getInstance().getUserFromCache(usersVisitors.get(i));
					visitedPlacesByFollowedUsers.add(dest);
					visitedPlacesByFollowedUsers.add(visitor);
					visitedPlacesByFollowedUsers.add((String) usersVisitors.get(i + 2));
				}
			}
		}
		return visitedPlacesByFollowedUsers;
	}

	@RequestMapping(value = "/isFollowed", method = RequestMethod.GET)
	@ResponseBody
	public String isFollowed(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		String email = request.getParameter("user");
		if (user.getFollowedUsers().contains(email)) {
			return "followed";
		}
		return "notFollowed";
	}

	@RequestMapping(value = "/addToVisited", method = RequestMethod.POST)
	@ResponseBody
	public String addToVisited(HttpServletRequest request) {
		String place = request.getParameter("destinationName").replaceAll("%20", " ");
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			return "FAILURE";
		}
		boolean isAdded = UsersManager.getInstance().addVidsitedDestination(user, place);
		if (!isAdded) {
			return "VISITED";
		}
		return "SUCCESS";
	}

	@RequestMapping(value = "/isVisited", method = RequestMethod.GET)
	@ResponseBody
	public String isVisited(HttpServletRequest request) {
		String place = request.getParameter("destinationName").replaceAll("%20", " ");
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			return "FAILURE";
		}
		boolean isVisited = user.getVisitedPlaces().contains(place);
		if (isVisited) {
			return "VISITED";
		}
		return "NOT VISITED";
	}

	@RequestMapping(value = "/getUsersSearchResult", method = RequestMethod.GET)
	@ResponseBody
	public ArrayList<User> getUsersSearchResult(@RequestParam("search") String search) {
		ArrayList<User> userSearch = new ArrayList<>();
		for (User user : UsersManager.getInstance().getRegisterredUsers().values()) {
			if (user.getFirstName().toLowerCase().contains(search.toLowerCase())
					|| user.getLastName().toLowerCase().contains(search.toLowerCase())) {
				userSearch.add(user);
			}
		}
		return userSearch;
	}
	
	@RequestMapping(value = "/getCurrentUserEmail", method = RequestMethod.GET)
	@ResponseBody
	public String getCurrentUserEmail(HttpServletRequest request, HttpServletResponse response) {
		User currentUser = (User) request.getSession().getAttribute("user");
		if (currentUser!=null) {
			String userEmail = currentUser.getEmail();
			if (userEmail!=null && !userEmail.isEmpty()) {
				return userEmail;
			}
		}
		return null;
	}

	private static boolean validateData(String firstName, String lastName, String email, String password) {
		if ((firstName != null && lastName != null && email != null && password != null)) {
			return firstName.matches(NAME_PATTERN) && lastName.matches(NAME_PATTERN) && email.matches(EMAIL_PATTERN)
					&& password.length() >= MINIMUM_PASSWORD_LENGTH;
		}
		return false;

	}

}
