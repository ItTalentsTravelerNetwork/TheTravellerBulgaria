package com.springframework.exceptions;

public class InvalidPasswordException extends InvalidDataException {
	@Override
	public String getMessage() {
		return "Invalid password";
	}
}
