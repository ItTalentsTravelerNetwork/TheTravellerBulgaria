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

import exceptions.InvalidCoordinatesException;
import functionality.DestinationsManager;
import models.Destination.Category;
import models.Location;
import models.User;

@WebServlet("/AddDestinationServlet")
@MultipartConfig
public class AddDestinationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ServletUtils.checkIfLoggedIn(request, response);

		String name = request.getParameter("name");
		if (DestinationsManager.getInstance().getDestinationFromCache(name) != null) {
			System.out.println("Destination already exists");
			response.sendRedirect("AddDestination.jsp");
			return;
		}
		String longitude = request.getParameter("long");
		String lattitude = request.getParameter("lat");
		String description = request.getParameter("description");
		String category = request.getParameter("category");
		Location location = null;
		try {
			location = new Location(Double.parseDouble(longitude), Double.parseDouble(lattitude));
		} catch (NumberFormatException e) {
			System.out.println("invalid coordinates type");
		}
		Part destinationImage = request.getPart("picture");

		if (name != null && description != null && location != null) {
			if (!name.trim().equals("") && !description.trim().equals("")) {
				if (destinationImage != null) {
					String destinationImageName = destinationImage.getSubmittedFileName();
					System.out.println("Input profile picture name: " + destinationImageName);
					if (ServletUtils.checkIfValidPictureType(destinationImageName)) {
						InputStream picStream = destinationImage.getInputStream();
						File dir = new File("destinationPhotos");
						if (!dir.exists()) {
							dir.mkdir();
						}
						File destinationPicFile = new File(dir,
								name + "-image." + destinationImage.getContentType().split("/")[1]);
						Files.copy(picStream, destinationPicFile.toPath());

						try {
							DestinationsManager.getInstance().addDestination(
									((User) request.getSession().getAttribute("user")), name, description,
									Double.parseDouble(lattitude), Double.parseDouble(longitude),
									destinationPicFile.getName(), Category.valueOf(category)); // ***
																								// added
																								// the
																								// rest
																								// of
																								// the
																								// fields
						} catch (NumberFormatException | InvalidCoordinatesException e) {
							System.out.println("Invalid destination Data");
						}
						System.out.println("Destination Registration Successful!");
						// request.getRequestDispatcher("AllDestinations.jsp").forward(request,
						// response);
						response.getWriter().append("SUCCESS");
					} else {
						System.out.println("Destination registration failed! Incorrect picture type!");
						// request.getRequestDispatcher("AddDestination.jsp").forward(request,
						// response);
						response.getWriter().append("FAILURE");
					}
				} else {
					System.out.println("Destination registration failed! File error!");
					// request.getRequestDispatcher("AddDestination.jsp").forward(request,
					// response);
					response.getWriter().append("FAILURE");
				}
			} else {
				System.out.println("Destination registration failed! Incorrect destination name and/or description!");
				// request.getRequestDispatcher("AddDestination.jsp").forward(request,
				// response);
				response.getWriter().append("FAILURE");
			}

		}
	}

}
