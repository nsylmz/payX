package com.nsylmz.payx.userservice.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.nsylmz.payx.userservice.model.Role;

import reactor.core.publisher.Mono;

public interface RoleRepository extends ReactiveMongoRepository<Role, String> {
	
	@Query("{'role' : ?0}")
	public Mono<Role> retrieveRole(String role);
	
}
