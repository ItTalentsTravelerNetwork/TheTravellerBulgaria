package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletUtils {

	private static final String[] availablePictureTypes = { ".jpg", ".jpeg", ".bmp", ".gif", ".png" };

	public static void checkIfLoggedIn(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (request.getSession().getAttribute("user") == null) {
			response.reset();
			request.getRequestDispatcher("SignIn.jsp").forward(request, response);
		}
	}

	public static boolean checkIfValidPictureType(String pictureName) {
		if (pictureName != null && !pictureName.isEmpty()) {
			String pictureType = pictureName.substring(pictureName.lastIndexOf("."));
			for (String s : availablePictureTypes) {
				if (pictureType.equals(s)) {
					System.out.println("Picture type: " + pictureType);
					return true;
				}
			}
			System.out.println("Picture type: " + pictureType);
			return false;
		}
		System.out.println("Picture type: null");
		return false;
	}

}
