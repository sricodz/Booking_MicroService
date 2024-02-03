package com.restaurant.service.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleUserDataException(ResourceNotFoundException userDataEx){
		log.error("Inside the exception causing the exception!! :: {} ",userDataEx.getMessage());
		Map<String,String> errorMap = new HashMap<>();
		errorMap.put("errorMessage", userDataEx.getMessage());
		errorMap.put("status", HttpStatus.NOT_FOUND.toString());
		return ResponseEntity.ok(errorMap);
	}
}
