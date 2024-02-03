package com.restaurant.service.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.restaurant.service.model.Restaurant;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface RestaurantRepository extends ReactiveMongoRepository<Restaurant, String>{
	
	public Mono<Restaurant> findByRestaurantName(String name);
	public Flux<Restaurant> findByLocation(String location);
}
