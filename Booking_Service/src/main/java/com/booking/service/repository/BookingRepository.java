package com.booking.service.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.booking.service.model.Booking;

public interface BookingRepository extends ReactiveMongoRepository<Booking, String>{

}
