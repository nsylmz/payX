package com.nsylmz.payx.account.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.nsylmz.payx.account.model.Transaction;

import reactor.core.publisher.Flux;

public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String> {
	
	@Query("{ 'accountNumber' : ?0}")
	public Flux<Transaction> retrieveAllTransactionsForAccount(Long accountNumber, Sort sort);
	
	@Query("{ 'customerNumber' : ?0}")
	public Flux<Transaction> retrieveAllTransactionsForCustomer(Long customerNumber, Sort sort);

}
