package com.restaurant.service.unit.test;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.restaurant.service.controller.RestaurantController;
import com.restaurant.service.model.BookTable;
import com.restaurant.service.model.Restaurant;
import com.restaurant.service.services.RestaurantService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@WebFluxTest(controllers = RestaurantController.class)
public class RestaurantControllerUnitTest {

	@Autowired
	private WebTestClient wclient;
	
	@MockBean
	RestaurantService resServ;
	
	static String RESTAURANT_URL = "/v1/restaurant";
	
	@Test
	void getAllRestaurant() {
		var restaurant = List.of(
				new Restaurant("a","Robo","Tirupathi","Veg",List.of(new BookTable(2,false),new BookTable(3,true))),
				new Restaurant("b","Taj","Bangalore","Non-Veg",List.of(new BookTable(213,false),new BookTable(323,true))),
				new Restaurant("c","Arabic","Hyderabad","Non-Veg",List.of(new BookTable(22,true),new BookTable(233,true)))
			);
		when(resServ.getAllRestaurants()).thenReturn(Mono.just(restaurant));
		
		Flux<Restaurant> resBody = wclient.get()
											.uri(RESTAURANT_URL)
											.exchange()
											.expectStatus()
											.is2xxSuccessful()
											.returnResult(Restaurant.class)
											.getResponseBody();
		StepVerifier.create(resBody)
					.expectSubscription()
					.expectNextCount(3)
					.verifyComplete();
	}
	
	@Test
	public void addRestaurantTest() {
		Restaurant restaurant = new Restaurant("a","Robo","Tirupathi","Veg",List.of(new BookTable(2,false),new BookTable(3,true)));
		when(resServ.addRestaurant(restaurant)).thenReturn(Mono.just(restaurant));
		
		wclient
			.post()
			.uri(RESTAURANT_URL)
			.body(Mono.just(restaurant),Restaurant.class)
			.exchange()
			.expectStatus()
			.is2xxSuccessful();
	}
	
	@Test
	public void getRestauarntByNameTest() {
		var restaurant = new Restaurant("a","Robo","Tirupathi","Veg",List.of(new BookTable(2,false),new BookTable(3,true)));
		when(resServ.getRestaurantByName(anyString())).thenReturn(Mono.just(restaurant));
		
		wclient
			.get()
			.uri(RESTAURANT_URL+"/name/robo")
			.exchange()
			.expectStatus()
			.is2xxSuccessful();
	}
	
	@Test
	public void updateRestaurantTest() {
		var restaurant = new Restaurant("a","Robo","Tirupathi","Veg",List.of(new BookTable(2,false),new BookTable(3,true)));
		when(resServ.updateRestaurant(restaurant,"id")).thenReturn(Mono.just(restaurant));
		
		wclient
			.put()
			.uri(RESTAURANT_URL+"/id")
			.bodyValue(restaurant)
			.exchange()
			.expectStatus()
			.isOk();
	}

}
