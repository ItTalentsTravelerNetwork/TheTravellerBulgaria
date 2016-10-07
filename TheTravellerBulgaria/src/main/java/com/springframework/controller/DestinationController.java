package com.springframework.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
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
import com.springframework.model.User;

@RestController
@MultipartConfig(maxFileSize = 200000000)
public class DestinationController {

	
	@RequestMapping(value = "/addDestination", method = RequestMethod.POST)
	@ResponseBody
	public String addDestination(@RequestParam("mainPicture") MultipartFile multipartFile, HttpServletRequest request, HttpSession session)
			throws IOException {
		
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
			File destinationMainPicFile = new File(dir, name + "-destinationMainPic." + multipartFile.getOriginalFilename());
			Files.copy(multipartFile.getInputStream(), destinationMainPicFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			try {
				DestinationsManager.getInstance().addDestination(((User)session.getAttribute("user")), name, description, Double.parseDouble(lattitude), Double.parseDouble(longitude), destinationMainPicFile.getName(), category);
			} catch (BeansException | NumberFormatException | InvalidCoordinatesException e) {
				e.printStackTrace();
				return "Destination registration failed!";
			}
			return "Destination added successfully!";
		}
		return "Adding destination failed!";
	}
	
	private static boolean validateData(String name, String lattitude, String longitude, String description, String category) {
		if ((name != null && lattitude != null && longitude != null && description != null && category!=null)) {
			if (!DestinationsManager.getInstance().chechDestinationInCache(name)) {
				return true;
			}
		}
		return false;
	}
	
	
	
}
