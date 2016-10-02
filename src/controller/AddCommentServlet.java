package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import exceptions.InvalidDataException;
import functionality.DestinationsManager;
import functionality.UsersManager;
import models.User;

/**
 * Servlet implementation class AddCommentServlet
 */
@WebServlet("/AddCommentServlet")
public class AddCommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletUtils.checkIfLoggedIn(request, response);

		String destName = request.getParameter("name");
		if (destName == null || destName == "") {
			request.getRequestDispatcher("Comment.jsp").forward(request, response);
		}

		User user = (User) request.getSession().getAttribute("user");
		String comment = request.getParameter("comment");

		String video = request.getParameter("video"); // added video to
														// parameters!!!!!!!!

		if (DestinationsManager.getInstance().getDestinationFromCache(destName) == null) {
			request.getRequestDispatcher("AllDestinations.jsp").forward(request, response);
		}
		try {
			UsersManager.getInstance().addComment(user.getEmail(), destName, comment, video);
		} catch (InvalidDataException e) {
			// TODO show user friendly message ***
			e.printStackTrace();
		}
		request.getRequestDispatcher("Destination.jsp?name=" + destName).forward(request, response);
	}

}
