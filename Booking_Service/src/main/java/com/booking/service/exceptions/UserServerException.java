package com.booking.service.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserServerException extends RuntimeException{

	 private String message;

	public UserServerException(String message) {
		super(message);
		this.message = message;
	}
	 
	 
}
