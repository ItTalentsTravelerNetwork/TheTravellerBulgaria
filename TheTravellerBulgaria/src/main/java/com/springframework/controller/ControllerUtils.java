package com.springframework.controller;

import javax.servlet.http.HttpServletResponse;

public class ControllerUtils {
	private static final String[] availablePictureTypes = { ".jpg", ".jpeg", ".bmp", ".gif", ".png" };

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

	public static void setHeaders(HttpServletResponse response) {
		response.setHeader("Cache-Control",
				"no-cache,no-store,private,must-revalidate,max-stale=0,post-check=0,pre-check=0");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
	}
}
