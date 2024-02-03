package com.booking.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.booking.service.exceptions.ResourceNotFoundException;
import com.booking.service.model.Booking;
import com.booking.service.services.BookingService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1")
public class BookingController {
	
	@Autowired
	private BookingService bookServ;
	
	@PostMapping("/booking/id/{id}/resName/{name}")
	public Mono<ResponseEntity<Booking>> bookingOperation(@PathVariable("id")String id,@PathVariable("name")String name){
		return bookServ.performBookingOperation(id, name)
						.map(book->{
							return ResponseEntity.status(HttpStatus.CREATED).body(book);
						})
						.switchIfEmpty(Mono.error(new ResourceNotFoundException("Booking is not Available now , Please try After Some time!!")));
	}
	
	
	@GetMapping("/booking")
	public Mono<ResponseEntity<List<Booking>>> getAllBookingsList(){
		return bookServ.getAllBookings()
						.map(booking->{
							return ResponseEntity.ok().body(booking);
						}).switchIfEmpty(Mono.error(new ResourceNotFoundException("Unable to Show the bookings / there are no bookings are availble")));
	}
	
	@GetMapping("/booking/id/{id}")
	public Mono<ResponseEntity<Booking>> getBookingById(@PathVariable("id")String id){
		return bookServ.getBookingById(id)
						.map(booking->{
							return ResponseEntity.ok().body(booking);
						}).switchIfEmpty(Mono.error(new ResourceNotFoundException("There is no booking available for given booking id :: "+id)));							
	}
	
	@DeleteMapping("/booking/id/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<Void> cancelBooking(@PathVariable("id")String id){
		return bookServ.deleteBooking(id)
							//.switchIfEmpty(Mono.error(new ResourceNotFoundException("Unable to Delete the Booking for given id ::"+id)))
							.log();
	}
	
	
}
