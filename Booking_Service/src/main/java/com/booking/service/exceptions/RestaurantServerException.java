package com.booking.service.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantServerException extends RuntimeException{

	 private String message;

	public RestaurantServerException(String message) {
		super(message);
		this.message = message;
	}
	 
	 
}
