package com.springframework.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeansException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.springframework.exceptions.InvalidCoordinatesException;
import com.springframework.functionality.DestinationsManager;
import com.springframework.functionality.UsersManager;
import com.springframework.model.Comment;
import com.springframework.model.Destination;
import com.springframework.model.User;

@RestController
@MultipartConfig(maxFileSize = 200000000)
public class DestinationController {

	@RequestMapping(value = "/addDestination", method = RequestMethod.POST)
	@ResponseBody
	public String addDestination(@RequestParam("mainPicture") MultipartFile multipartFile, HttpServletRequest request,
			HttpSession session) throws IOException {

		String name = request.getParameter("name");
		String lattitude = request.getParameter("lattitude");
		String longitude = request.getParameter("longitude");
		String description = request.getParameter("description");
		String category = request.getParameter("category");

		if (validateData(name, lattitude, longitude, description, category)) {
			File dir = new File("destinationPics");
			if (!dir.exists()) {
				dir.mkdir();
			}
			File destinationMainPicFile = new File(dir,
					name + "-destinationMainPic." + multipartFile.getOriginalFilename());
			Files.copy(multipartFile.getInputStream(), destinationMainPicFile.toPath(),
					StandardCopyOption.REPLACE_EXISTING);
			try {
				if (DestinationsManager.getInstance().addDestination(((User) session.getAttribute("user")), name,
						description, Double.parseDouble(lattitude), Double.parseDouble(longitude),
						destinationMainPicFile.getName(), category)) {
					session.setAttribute("destination",
							DestinationsManager.getInstance().getDestinationFromCache(name));
				}
			} catch (BeansException | NumberFormatException | InvalidCoordinatesException e) {
				e.printStackTrace();
				return "Destination registration failed!";
			}
			return "Destination added successfully!";
		}
		return "Adding destination failed!";
	}

	private static boolean validateData(String name, String lattitude, String longitude, String description,
			String category) {
		if ((name != null && lattitude != null && longitude != null && description != null && category != null)) {
			if (!DestinationsManager.getInstance().chechDestinationInCache(name)) {
				return true;
			}
		}
		return false;
	}

	@RequestMapping(value = "/getDestinationMainPicture", method = RequestMethod.GET)
	@ResponseBody
	public void getPicture(HttpServletRequest request, HttpServletResponse response) {
		String destinationName = request.getParameter("destinationName");
		Destination destination = DestinationsManager.getInstance().getDestinationFromCache(destinationName);
		File mainPicFile = new File("destinationPics", destination.getMainPicture());

		try {
			OutputStream out = response.getOutputStream();
			Files.copy(mainPicFile.toPath(), out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@RequestMapping(value = "/getAllDestinations", method = RequestMethod.GET)
	@ResponseBody
	public ArrayList<Destination> getAllDestinations(HttpServletRequest request, HttpServletResponse response) {
		ArrayList<Destination> allDestinations = new ArrayList<>();
		for (Destination destination : DestinationsManager.getInstance().getAllDestinations().values()) {
			allDestinations.add(destination);
		}
		return allDestinations;
	}

	@RequestMapping(value = "/getDestination", method = RequestMethod.GET)
	@ResponseBody
	public Destination showDestination(HttpServletRequest request, HttpServletResponse response) {
		String destinationName = request.getParameter("destinationName");
		Destination destination = DestinationsManager.getInstance().getDestinationFromCache(destinationName);
		return destination;
	}

	@RequestMapping(value = "/getDestinationCommentsUsers", method = RequestMethod.GET)

	public @ResponseBody ArrayList<Object> getDestinationCommentsUsers(HttpServletRequest request) {

		String destinationName = request.getParameter("destinationName");

		Destination destination = DestinationsManager.getInstance().getDestinationFromCache(destinationName);

		ArrayList<Object> commentsAndUsers = new ArrayList<>();

		for (Comment comment : destination.getComments()) {
			commentsAndUsers.add(comment);
			commentsAndUsers.add(UsersManager.getInstance().getUserFromCache(comment.getAuthorEmail()));
		}

		return commentsAndUsers;
	}

	@RequestMapping(value = "/AddPicture", method = RequestMethod.POST)
	public void addPicture(@RequestParam("pic") MultipartFile multipartFile, HttpServletRequest request) {
		String destName = request.getParameter("destinationName").replaceAll("%20", " ");
		Destination dest = DestinationsManager.getInstance().getDestinationFromCache(destName);

		File dir = new File("destinationPics");
		if (!dir.exists()) {
			dir.mkdir();
		}
		File destinationMainPicFile = new File(dir,
				dest.getName() + "-destinationMainPic." + multipartFile.getOriginalFilename());
		try {
			Files.copy(multipartFile.getInputStream(), destinationMainPicFile.toPath(),
					StandardCopyOption.REPLACE_EXISTING);
			DestinationsManager.getInstance().addPicture(dest.getName(), destinationMainPicFile.getName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
