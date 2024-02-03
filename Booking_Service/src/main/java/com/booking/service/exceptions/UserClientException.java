package com.booking.service.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserClientException extends RuntimeException{

	 private String message;
	 private Integer statusCode;
	 
	public UserClientException(String message, Integer statusCode) {
		super(message);
		this.message = message;
		this.statusCode = statusCode;
	}
	 
	 
}
