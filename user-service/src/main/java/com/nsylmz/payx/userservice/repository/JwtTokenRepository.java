package com.nsylmz.payx.userservice.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.nsylmz.payx.userservice.model.JwtToken;

public interface JwtTokenRepository extends ReactiveMongoRepository<JwtToken, String> {
	
}
