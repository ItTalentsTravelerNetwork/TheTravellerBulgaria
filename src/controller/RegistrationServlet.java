package controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import functionality.UsersManager;

/**
 * Servlet implementation class RegistrationServlet
 */
@WebServlet("/registration")
@MultipartConfig
public class RegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String NAME_PATTERN = "^[A-Za-z]+$";
	private static final int MINIMUM_PASSWORD_LENGTH = 6;
	private static final String EMAIL_PATTERN = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9]+.[a-z.]+$";

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String inputPassword = request.getParameter("userPassword");

		String firstName = request.getParameter("userFirstName");
		String lastName = request.getParameter("userLastName");
		String email = request.getParameter("userEmailAddress");
		String description = request.getParameter("userDescription");
		Part profilePic = request.getPart("profilePic");

		if (validateData(firstName, lastName, email, inputPassword)) {
			if (profilePic != null) {
				String profilePicName = profilePic.getSubmittedFileName();
				System.out.println("Input profile picture name: " + profilePicName);
				if (ServletUtils.checkIfValidPictureType(profilePicName)) {
					InputStream picStream = profilePic.getInputStream();
					File dir = new File("userPics");
					if (!dir.exists()) {
						dir.mkdir();
					}
					File profilePicFile = new File(dir,
							email + "-profilePic." + profilePic.getContentType().split("/")[1]);
					if (!profilePicFile.exists()) {
						Files.copy(picStream, profilePicFile.toPath());

						UsersManager.getInstance().registerUser(firstName, lastName, email, inputPassword, description,
								profilePicFile.getName());

						response.getWriter().append("User Registration Successful!");
						return;
					}
				}
			}
		}
		response.getWriter().append("Registration failed!");
	}

	private static boolean validateData(String firstName, String lastName, String email, String password) {
		if ((firstName != null && lastName != null && email != null && password != null)) {
			return firstName.matches(NAME_PATTERN) && lastName.matches(NAME_PATTERN) && email.matches(EMAIL_PATTERN)
					&& password.length() >= MINIMUM_PASSWORD_LENGTH;
		}
		return false;
	}

}
