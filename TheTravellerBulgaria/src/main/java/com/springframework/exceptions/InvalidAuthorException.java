package com.springframework.exceptions;

public class InvalidAuthorException extends Exception {

	@Override
	public String getMessage() {
		return "Invalid Author!";
	}
}
