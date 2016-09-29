package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import functionality.DestinationsManager;
import models.Destination;
import models.User;

/**
 * Servlet implementation class GetUserDestinations
 */
@WebServlet("/GetUserDestinations")
public class GetUserDestinations extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ArrayList<Destination> addedDestinations = new ArrayList<>();
		for (Map.Entry<String, Destination> destinationEntry : DestinationsManager.getInstance().getAllDestinations()
				.entrySet()) {
			if (((User) request.getSession().getAttribute("user")).getAddedPlaces()
					.contains(destinationEntry.getKey())) {
				addedDestinations.add(destinationEntry.getValue());
			}
		}
		String json = new Gson().toJson(addedDestinations);
		response.getWriter().append(json);
	}
}
