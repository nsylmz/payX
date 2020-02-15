package com.nsylmz.payx.userservice.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.nsylmz.payx.userservice.model.UserRole;

public interface CustomerRoleRepository extends ReactiveMongoRepository<UserRole, String> {
	
}
