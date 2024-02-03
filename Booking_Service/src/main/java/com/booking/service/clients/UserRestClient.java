package com.booking.service.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.booking.service.exceptions.UserClientException;
import com.booking.service.exceptions.UserServerException;
import com.booking.service.model.User;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class UserRestClient {

	@Value("${rest.client.user.url}")
	private String userUrl;
	
	@Autowired
	private WebClient wclient;
	
	public Mono<User> retrieveUserData(String userId){
		var url = userUrl.concat("/{id}");
		
		return wclient
					.get()
					.uri(url,userId)
					.retrieve()
					// Handling 4xx errors (only if returned / emitted by the User service)
					.onStatus(HttpStatus::is4xxClientError, (clientResponse->{
						var errorReason = clientResponse.statusCode().getReasonPhrase();
						log.error("Exception got for Data not found case !!");
						//Handling 404
						if(clientResponse.statusCode().equals(HttpStatus.NOT_FOUND)) {
							return Mono.error(new UserClientException(
									String.format("No Such User exist for the id : %s", userId), clientResponse.statusCode().value()));
						}
						
						//Handling 4xx errors
						return clientResponse.bodyToMono(String.class)
									.switchIfEmpty(Mono.error(new UserClientException(String.format("Server Exception in UserService : %s", errorReason), clientResponse.statusCode().value())))
									.flatMap(resMessage -> Mono.error(new UserClientException(resMessage, clientResponse.statusCode().value())));
					}))
					
					// Handling 5xx errors (only if returned / emitted by the User service)
					.onStatus(HttpStatus::is5xxServerError, (clientRes->{
						log.info("User Rest status code: {}", clientRes.statusCode().value());
						var errorRes = clientRes.statusCode().getReasonPhrase();
						log.error("Exception got from the server side case !!");
						//Handling default 5xx errors
						return clientRes.bodyToMono(String.class)
								.switchIfEmpty(Mono.error(new UserServerException(String.format("Server Exception in UserService: %s", errorRes))))
								.flatMap(resMes->Mono.error(new UserServerException(String.format("Server Exception in UserService : %s", resMes))));
					}))
					.bodyToMono(User.class)
					.log();
	}
}
