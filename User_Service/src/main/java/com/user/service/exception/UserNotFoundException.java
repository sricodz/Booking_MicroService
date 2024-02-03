package com.user.service.exception;

public class UserNotFoundException extends RuntimeException{

	
	public UserNotFoundException(String message, Throwable ex) {
		super(message,ex);
	}
	public UserNotFoundException(String message) {
		super(message);
	}
	
	
}
