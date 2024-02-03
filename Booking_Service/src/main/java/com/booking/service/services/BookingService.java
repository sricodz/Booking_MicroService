package com.booking.service.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.booking.service.clients.RestaurantRestClient;
import com.booking.service.clients.UserRestClient;
import com.booking.service.exceptions.ResourceNotFoundException;
import com.booking.service.model.BookTable;
import com.booking.service.model.Booking;
import com.booking.service.model.Restaurant;
import com.booking.service.model.User;
import com.booking.service.repository.BookingRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class BookingService {

	@Autowired
	private UserRestClient userClient;
	@Autowired
	private RestaurantRestClient restaurantClient;
	
	@Autowired
	private BookingRepository bookRepo;
	
	
	public Mono<Booking> performBookingOperation(String userId,String resName){
		Mono<User> userDetails = userClient.retrieveUserData(userId);
		Mono<Restaurant> restaurant = restaurantClient.retrieveRestData(resName);

		log.info("Inside the booking service save method !!");
		Booking b = new Booking();
		return userDetails.zipWith(restaurant)
					.flatMap(tuple->{
						User u = tuple.getT1();
						Restaurant r = tuple.getT2();
						log.info("The user details are the u :: {} ",u.toString());
						log.info("The restaurant details are the r :: {} ",r.toString());
						if(u!=null && r!=null) {							 
							return checkAvailability(r.getTables())
									.flatMap(check->{
										if(check) {
											log.info("the value of check is passed inside the if check condition");
											BookTable table = r.getTables().stream().filter(BookTable::getIsAvailable).findFirst().get();
											r.setTables(Arrays.asList(table));
											b.setRestDetails(r);
											b.setUserDetails(u);
											return bookRepo.save(b);
										}
										log.info("Not gone inside the if check block");
										return Mono.error(new ResourceNotFoundException("Sorry the tables are already booked for the Restaurant :: "+r.getRestaurantName()));
									});						
						}
						return Mono.error(new ResourceNotFoundException("The Information you provided is Incorrect either userId / restaurant Name"));
					});
	}
	
	private Mono<Boolean> checkAvailability(List<BookTable> tables){
		log.info("Inside the checkAvailability method");
		return Mono.just(tables.stream()
					.anyMatch(BookTable::getIsAvailable));
					
	}
	
	public Mono<List<Booking>> getAllBookings(){
		return bookRepo.findAll().collectList();
	}
	
	public Mono<Booking> getBookingById(String id){
		return bookRepo.findById(id);
	}
	
	public Mono<Void> deleteBooking(String id){
		return bookRepo.deleteById(id);
	}
	
}
