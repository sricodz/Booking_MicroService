//package com.user.service.exceptionHandler;
//
//import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
//import org.springframework.core.io.buffer.DataBufferFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//
//import com.user.service.exception.UserDataException;
//import com.user.service.exception.UserNotFoundException;
//
//import lombok.extern.slf4j.Slf4j;
//import reactor.core.publisher.Mono;
//
//@Component
//@Slf4j
//public class GlobalErrorHandler implements ErrorWebExceptionHandler{
//	
//	@Override
//	public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
//		log.error("Exception message is : {} ",ex.getMessage(),ex);
//		DataBufferFactory dataBufferFactory = exchange.getResponse().bufferFactory();
//		var errorMessage = dataBufferFactory.wrap(ex.getMessage().getBytes());
//		
//		//Handling userDataException
//		if(ex instanceof UserDataException) {
//			exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
//			return exchange.getResponse().writeWith(Mono.just(errorMessage));
//		}
//		
//		//Handling UserNotFoundException
//		if(ex instanceof UserNotFoundException) {
//			exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
//			return exchange.getResponse().writeWith(Mono.just(errorMessage));
//		}
//		
//		//Handling any other Exception (default)
//		exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
//		return exchange.getResponse().writeWith(Mono.just(errorMessage));
//	}
//
//	
//}
