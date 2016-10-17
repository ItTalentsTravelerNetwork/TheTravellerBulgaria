package com.springframework.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.TreeSet;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.tika.Tika;
import org.springframework.beans.BeansException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.springframework.exceptions.InvalidCoordinatesException;
import com.springframework.exceptions.InvalidDataException;
import com.springframework.functionality.DestinationsManager;
import com.springframework.functionality.UsersManager;
import com.springframework.model.Comment;
import com.springframework.model.Destination;
import com.springframework.model.User;

@RestController
@MultipartConfig
public class DestinationController {
	private static final String[] availablePictureTypes = { "image/jpeg", "image/x-ms-bmp", "image/gif", "image/png" };

	@RequestMapping(value = "/addDestination", method = RequestMethod.POST)
	@ResponseBody
	public String addDestination(@RequestParam("mainPicture") MultipartFile multipartFile, HttpServletRequest request,
			HttpSession session) {
		if (session.getAttribute("user") == null) {
			return "{\"msg\" : \"No user in session\"}";
		}
		Tika tika = new Tika();
		String realFileType;
		try {
			realFileType = tika.detect(multipartFile.getBytes());
			for (String type : availablePictureTypes) {
				if (type.equals(realFileType)) { // if the real picture type is
													// one of the available
					String name = request.getParameter("name").replaceAll("%20", " ").trim();
					String lattitude = request.getParameter("lattitude");
					String longitude = request.getParameter("longitude");
					String description = StringEscapeUtils.escapeHtml(request.getParameter("description"));
					String category = request.getParameter("category");
					if (DestinationsManager.getInstance().getDestinationFromCache(name) != null) {
						return "{\"msg\" : \"EXISTS\"}";
					}
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
							if (DestinationsManager.getInstance().addDestination(((User) session.getAttribute("user")),
									name, description, Double.parseDouble(lattitude), Double.parseDouble(longitude),
									destinationMainPicFile.getName(), category)) {
								session.setAttribute("destination",
										DestinationsManager.getInstance().getDestinationFromCache(name));
								return "{\"msg\" : \"Destination added successfully!\"}";
							}
						} catch (BeansException | InvalidCoordinatesException | IllegalArgumentException e) {
							e.printStackTrace();
							return "{\"msg\" : \"Destination registration failed!\"}";
						}
					}
					return "{\"msg\" : \"Wrong data!\"}";
				}
			}
			return "{\"msg\" : \"Wrong picture format!\"}";
		} catch (IOException e1) {
			e1.printStackTrace();
			return "{\"msg\" : \"Destination registration failed!\"}";
		}
	}

	private static boolean validateData(String name, String lattitude, String longitude, String description,
			String category) {
		if ((name != null && lattitude != null && longitude != null && description != null && category != null
				&& !name.isEmpty() && !lattitude.isEmpty() && !longitude.isEmpty() && !description.isEmpty()
				&& !category.isEmpty())) {
			try {
				double destinationLattitude = Double.parseDouble(lattitude);
				double destinationLongitude = Double.parseDouble(longitude);
				if (destinationLattitude >= 0 && destinationLattitude <= 90 && destinationLongitude >= 0
						&& destinationLongitude <= 180) {
					for (Destination.Category c : Destination.Category.values()) {
						if (c.name().equals(category)) {
							return true;
						}
					}
				}
				return false;
			} catch (NumberFormatException e) {

				return false;
			}
		}
		return false;
	}

	@RequestMapping(value = "/getDestinationMainPicture", method = RequestMethod.GET)
	@ResponseBody
	public void getPicture(HttpServletRequest request, HttpServletResponse response) {
		String destinationName = request.getParameter("destinationName").replaceAll("%20", " ");
		Destination destination = DestinationsManager.getInstance().getDestinationFromCache(destinationName);
		File mainPicFile = new File("destinationPics", destination.getMainPicture());

		try {
			OutputStream out = response.getOutputStream();
			Files.copy(mainPicFile.toPath(), out);
		} catch (IOException e) {
			System.out.println("IO exception on File copy happened");
		}

	}

	@RequestMapping(value = "/GetDestinationPicture", method = RequestMethod.GET)
	@ResponseBody
	public void getDestPicture(HttpServletRequest request, HttpServletResponse response) {
		String pic = request.getParameter("pic").replaceAll("%20", " ");

		File mainPicFile = new File("destinationPics", pic);

		try {
			OutputStream out = response.getOutputStream();
			Files.copy(mainPicFile.toPath(), out);
		} catch (IOException e) {
			System.out.println("IO exception on File copy happened");
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
		String destinationName = request.getParameter("destinationName").replaceAll("%20", " ");
		Destination destination = DestinationsManager.getInstance().getDestinationFromCache(destinationName);
		return destination;
	}

	@RequestMapping(value = "/getDestinationCommentsUsers", method = RequestMethod.GET)

	public @ResponseBody ArrayList<Object> getDestinationCommentsUsers(HttpServletRequest request) {

		String destinationName = request.getParameter("destinationName").replaceAll("%20", " ");

		Destination destination = DestinationsManager.getInstance().getDestinationFromCache(destinationName);

		ArrayList<Object> commentsAndUsers = new ArrayList<>();

		for (Comment comment : destination.getComments()) {
			commentsAndUsers.add(comment);
			commentsAndUsers.add(UsersManager.getInstance().getUserFromCache(comment.getAuthorEmail()));
		}

		return commentsAndUsers;
	}

	@RequestMapping(value = "/AddPic", method = RequestMethod.POST)
	@ResponseBody
	public String addPicture(@RequestParam("pic") MultipartFile multipartFile, HttpServletRequest request) {
		Tika tika = new Tika();
		String realFileType;
		try {
			realFileType = tika.detect(multipartFile.getBytes());
			for (String type : availablePictureTypes) {
				if (type.equals(realFileType)) { // if the real picture type is
													// one of the available

					String destName = request.getParameter("destinationName").replaceAll("%20", " ");

					Destination dest = DestinationsManager.getInstance().getDestinationFromCache(destName);
					if (dest == null) {
						return "{\"msg\" : \"NOT EXISTING\"}";
					}
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
						return "{\"msg\" : \"SUCCESS\"}";
					} catch (IOException e) {
						System.out.println("IO exception occured");
						return "{\"msg\" : \"FAILURE\"}";
					} catch (InvalidDataException e) {
						return "{\"msg\" : \"PICTURE EXISTS\"}";
					}
				}
			}
			return "{\"msg\" : \"Wrong picture format!\"}";
		} catch (IOException e) {
			e.printStackTrace();
			return "{\"msg\" : \"FAILURE\"}";
		}
	}

	@RequestMapping(value = "/getDestinationAuthor", method = RequestMethod.GET)
	@ResponseBody
	public User getDestinationAuthor(HttpServletRequest request, HttpServletResponse response) {
		String destinationName = request.getParameter("destinationName").replaceAll("%20", " ");
		Destination destination = DestinationsManager.getInstance().getDestinationFromCache(destinationName);
		User user = UsersManager.getInstance().getUserFromCache(destination.getAuthorEmail());
		return user;
	}

	@RequestMapping(value = "/GetDestinationGallery", method = RequestMethod.GET)
	@ResponseBody
	public ArrayList<String> getDestinationGallery(HttpServletRequest request) {
		ArrayList<String> gallery = new ArrayList<>();
		Destination dest = DestinationsManager.getInstance()
				.getDestinationFromCache(request.getParameter("destinationName").replaceAll("%20", " "));
		gallery.add(dest.getMainPicture());
		for (String pic : dest.getPictures()) {
			gallery.add(pic);
		}

		return gallery;
	}

	@RequestMapping(value = "/getDestinationsByCategory", method = RequestMethod.GET)
	@ResponseBody
	public ArrayList<Destination> getDestinationsByCategory(@RequestParam("category") String category) {
		ArrayList<Destination> allDestinations = new ArrayList<>();
		for (Destination destination : DestinationsManager.getInstance().getAllDestinations().values()) {
			if (destination.getCategory().toString().equals(category)) {
				allDestinations.add(destination);
			}
		}
		return allDestinations;
	}

	@RequestMapping(value = "/getDestinationSearchResults", method = RequestMethod.GET)
	@ResponseBody
	public TreeSet<Destination> getDestinationSearchResults(@RequestParam("search") String search) {
		TreeSet<Destination> destinations = new TreeSet<>(
				(d1, d2) -> d1.getDateAndTimeToString().compareTo(d2.getDateAndTimeToString()));
		for (Destination destination : DestinationsManager.getInstance().getAllDestinations().values()) {
			if (destination.getName().toLowerCase().contains(search.toLowerCase())) {
				destinations.add(destination);
			}
		}
		return destinations;
	}
}
