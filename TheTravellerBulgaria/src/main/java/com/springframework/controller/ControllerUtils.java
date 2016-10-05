package com.springframework.controller;

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
}
