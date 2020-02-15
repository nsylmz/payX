package com.nsylmz.payx.accountservice.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.nsylmz.payx.accountservice.model.Account;

import reactor.core.publisher.Mono;

public interface AccountRepository extends ReactiveMongoRepository<Account, String> {
	
	@Query("{'accountNumber' : ?0}")
	public Mono<Account> retrieveAccountByAccountNumber(Long accountNumber);
	
}
