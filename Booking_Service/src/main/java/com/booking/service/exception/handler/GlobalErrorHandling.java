package com.booking.service.exception.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

import com.booking.service.exceptions.ResourceNotFoundException;
import com.booking.service.exceptions.UserClientException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalErrorHandling {

	@ExceptionHandler(UserClientException.class)
	public ResponseEntity<String> handleClientException(UserClientException exception){
		log.error("Exception caught in handleClientException: {}", exception.getMessage());
		// returning the status code and exception message in ResponseEntity (fetched from userClientException)
		return ResponseEntity.status(exception.getStatusCode()).body(exception.getMessage());
	}
	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<String> handleRuntimeException(RuntimeException exception){
		log.error("Exception caught in handleClientException: {}", exception.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleNotFoundException(ResourceNotFoundException ex){
		log.error("Exception caught in ResourceNotFound Exception: {}", ex.getMessage());
		Map<String,String> errorMap = new HashMap<>();
		errorMap.put("errorMessage", ex.getMessage());
		errorMap.put("status", HttpStatus.NOT_FOUND.toString());
		return ResponseEntity.ok(errorMap);
	}
}
