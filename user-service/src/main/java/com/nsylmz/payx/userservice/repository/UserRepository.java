package com.nsylmz.payx.userservice.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.nsylmz.payx.userservice.model.User;

import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {
	
	@Query("{'userNumber' : ?0}")
	public Mono<User> retrieveUserByUserNumber(Long userNumber);
	
	@Query("{'email' : ?0}")
	public Mono<User> retrieveUserByEmail(String email);

}
