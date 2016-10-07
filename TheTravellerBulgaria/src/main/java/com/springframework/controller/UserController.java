package com.springframework.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

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

import com.springframework.functionality.UsersManager;
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
			File dir = new File("userPics");
			if (!dir.exists()) {
				dir.mkdir();
			}
			File profilePicFile = new File(dir, email + "-profilePic." + multipartFile.getOriginalFilename());
			Files.copy(multipartFile.getInputStream(), profilePicFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			UsersManager.getInstance().registerUser(email, inputPassword, firstName,
					lastName, description, profilePicFile.getName());
			return "User Registration Successful!";
		}

		return "Registration failed!";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestParam("userEmail") String email, @RequestParam("userPassword") String password,
			HttpSession session) {
		User user = UsersManager.getInstance().logIn(email, password);
		if (user != null) {
			session.setAttribute("user", user);
			return "SUCCESS";
		}
		return "FAILURE";
	}

	@RequestMapping(value = "/GetUserInfo", method = RequestMethod.GET)
	@ResponseBody
	public User checkForUser(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		return user;
	}

	@RequestMapping(value = "/GetProfilePicture", method = RequestMethod.GET)
	@ResponseBody
	public String getPicture(HttpServletRequest request, HttpServletResponse response) {
		String email = request.getParameter("email");
		User user = UsersManager.getInstance().getUserFromCache(email);
		File profilePicFile = new File("userPics", user.getProfilePicture());
		String path = profilePicFile.getAbsolutePath();
		return path;

	}

	private static boolean validateData(String firstName, String lastName, String email, String password) {
		if ((firstName != null && lastName != null && email != null && password != null)) {
			return firstName.matches(NAME_PATTERN) && lastName.matches(NAME_PATTERN) && email.matches(EMAIL_PATTERN)
					&& password.length() >= MINIMUM_PASSWORD_LENGTH;
		}
		return false;

	}

}
