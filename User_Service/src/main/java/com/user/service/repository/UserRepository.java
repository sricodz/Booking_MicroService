package com.user.service.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.user.service.model.User;

public interface UserRepository extends ReactiveMongoRepository<User, String>{

}
