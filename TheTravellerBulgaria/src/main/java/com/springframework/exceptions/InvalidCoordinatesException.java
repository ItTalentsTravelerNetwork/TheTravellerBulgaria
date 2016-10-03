package com.springframework.exceptions;

public class InvalidCoordinatesException extends InvalidDataException {
	@Override
	public String getMessage() {
		return "Invalid Coordinates Entered";
	}
}
