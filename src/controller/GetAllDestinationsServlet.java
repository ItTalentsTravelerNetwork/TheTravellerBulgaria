package controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import functionality.DestinationsManager;
import models.Destination;

/**
 * Servlet implementation class GetAllDestinationsServlet
 */
@WebServlet("/GetAllDestinationsServlet")
public class GetAllDestinationsServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Map<String, Destination> destinationsAndAuthors = DestinationsManager.getInstance().getAllDestinations();
		String json = new Gson().toJson(destinationsAndAuthors.values());
		response.getWriter().append(json);
	}
}
