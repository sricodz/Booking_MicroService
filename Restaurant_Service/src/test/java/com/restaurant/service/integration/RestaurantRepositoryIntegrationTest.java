package com.restaurant.service.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.restaurant.service.model.BookTable;
import com.restaurant.service.model.Restaurant;
import com.restaurant.service.repository.RestaurantRepository;

import reactor.test.StepVerifier;

@DataMongoTest
@RunWith(SpringRunner.class)
public class RestaurantRepositoryIntegrationTest {

	@Autowired
	RestaurantRepository restaurantRepo;
	
	@BeforeEach
	void setUp() {
		var restaurant = List.of(
					new Restaurant("a","Robo","Tirupathi","Veg",List.of(new BookTable(2,false),new BookTable(3,true))),
					new Restaurant("b","Taj","Bangalore","Non-Veg",List.of(new BookTable(213,false),new BookTable(323,true))),
					new Restaurant("c","Arabic","Hyderabad","Non-Veg",List.of(new BookTable(22,true),new BookTable(233,true)))
				);
		restaurantRepo.saveAll(restaurant)
						.blockLast();
	}
	
	@AfterEach
	void tearDown() {
		restaurantRepo.deleteAll().block();
	}
	
	@Test
	void findAll() {
		var restaurantFlux = restaurantRepo.findAll().log();
		StepVerifier.create(restaurantFlux)
					.expectNextCount(3)
					.verifyComplete();
	}
	
	@Test
	void findById() {
		var resFlux = restaurantRepo.findById("b");
		StepVerifier.create(resFlux)
					.assertNext(r->{
						assertEquals("Taj", r.getRestaurantName());
					})
					.verifyComplete();
	}
	
	@Test
	void updateRestaurantData() {
		var existingResFlux = restaurantRepo.findById("c").block();
		existingResFlux.setRestaurantName("A2B");
		var resFlux = restaurantRepo.save(existingResFlux);
		StepVerifier.create(resFlux)
					.assertNext(res->{
						assertEquals("A2B", res.getRestaurantName());
						assertEquals("Hyderabad", res.getRestaurantName());
						assertEquals("c", res.getId());
					})
					.verifyComplete();
	}
	
	@Test
	void getRestaurantByName() {
		var resFlux = restaurantRepo.findByRestaurantName("Arabic");
		StepVerifier.create(resFlux)
					.assertNext(res->{
						assertEquals("Non-Veg", res.getType());
						assertEquals("Hyderabad", res.getRestaurantName());
					})
					.verifyComplete();
	}
	
	@Test
	void deleteRestaruantData() {
		restaurantRepo.deleteById("a");
		var resFlux = restaurantRepo.findAll().log();
		StepVerifier.create(resFlux)
				.expectNextCount(2)
				.verifyComplete();
	}
}
