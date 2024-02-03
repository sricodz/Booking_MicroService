package com.user.service.exceptionHandler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.user.service.exception.UserDataException;
import com.user.service.exception.UserNotFoundException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalException {

	@ExceptionHandler(UserDataException.class)
	public ResponseEntity<?> handleUserDataException(UserDataException userDataEx){
		log.error("The userDataException thrown because of :: {} ",userDataEx.getMessage());
		Map<String,String> errorMap = new HashMap<>();
		errorMap.put("errorMessage", userDataEx.getMessage());
		errorMap.put("status", HttpStatus.BAD_REQUEST.toString());
		return ResponseEntity.ok(errorMap);
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException userDataEx){
		log.error("The userNotFoundException thrown because of :: {} ",userDataEx.getMessage());
		Map<String,String> errorMap = new HashMap<>();
		errorMap.put("errorMessage", userDataEx.getMessage());
		errorMap.put("status", HttpStatus.BAD_REQUEST.toString());
		return ResponseEntity.ok(errorMap);
	}
}
