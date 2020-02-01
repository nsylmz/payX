package com.nsylmz.payx.customer.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.nsylmz.payx.customer.model.Customer;

public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {

}
