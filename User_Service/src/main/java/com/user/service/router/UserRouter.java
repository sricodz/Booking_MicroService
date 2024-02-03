package com.user.service.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;

import com.user.service.handler.UserHandler;

@Configuration
public class UserRouter {

	@Bean
	public RouterFunction<ServerResponse> usersRoute(UserHandler userHandler){
		
//		return route()
//				.POST("/v1/user",req->userHandler.addUser(req))
//				.GET("/v1/user",req->userHandler.getAllUsers(req))
//				.GET("/v1/user/{id}",req->userHandler.getUserById(req))
//				.PUT("/v1/user/{id}",req->userHandler.updateUser(req))
//				.DELETE("/v1/user/{id}",req->userHandler.deleteUser(req))
//				.build();
		
		return route()
				.nest(path("/v1/user"), builder->
						builder
							.POST("",userHandler::addUser)
							.GET("/{id}",userHandler::getUserById)
							.GET("",userHandler::getAllUsers)
							.PUT("/{id}",userHandler::updateUser)
							.DELETE("/{id}",userHandler::deleteUser))
							
				.build();
				
	}
}
