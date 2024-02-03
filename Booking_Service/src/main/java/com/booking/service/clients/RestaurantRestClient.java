package com.booking.service.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.booking.service.exceptions.RestaurantClientException;
import com.booking.service.exceptions.RestaurantServerException;
import com.booking.service.exceptions.UserServerException;
import com.booking.service.model.Restaurant;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class RestaurantRestClient {

	@Autowired
	private WebClient wclient;
	
	@Value("${rest.client.restaurant.url}")
	private String restaurantUrl;
	
	public Mono<Restaurant> retrieveRestData(String resId){
		
		var url = restaurantUrl.concat("/name/{name}");
		
		return wclient
					.get()
					.uri(url,resId)
					.retrieve()
					// Handling 4xx errors (only if returned / emitted by the Restaurant service)
					.onStatus(HttpStatus::is4xxClientError, (clientRes->{
						var errorRes = clientRes.statusCode().getReasonPhrase();
						
						//Handling 404
						if(clientRes.statusCode().equals(HttpStatus.NOT_FOUND)) {
							log.error("Exception got for Data not found case !!");
							return Mono.error(new RestaurantClientException(String.format("No Such Restaurant exist for the id :: %s", resId), clientRes.statusCode().value()));							
						}
						
						//Handling 4xx errors
						return clientRes.bodyToMono(String.class)
									.switchIfEmpty(Mono.error(new RestaurantClientException(String.format("Server Exception in Restaurant Service : %s", errorRes), clientRes.statusCode().value())))
									.flatMap(resMes->Mono.error(new RestaurantClientException(resMes, clientRes.statusCode().value())));
					}))
					
					//Handling 5xx errors (only if returned / emitted by the Restaurant service)
					.onStatus(HttpStatus::is5xxServerError, (ServerRes->{
						log.info(" Restaurant status code: {}", ServerRes.statusCode().value());
						var errorRes = ServerRes.statusCode().getReasonPhrase();
						log.error("Exception got from the server side case !!");
						//Handling default 5xx errors
						return ServerRes.bodyToMono(String.class)
									.switchIfEmpty(Mono.error(new RestaurantServerException(String.format("Server Exception in RestaurantService :: %s", errorRes))))
									.flatMap(resMes->Mono.error(new UserServerException(String.format("Server Exception in Restaurant Service is :: %s", resMes))));
					}))
					.bodyToMono(Restaurant.class)
					.log();
	}
}
