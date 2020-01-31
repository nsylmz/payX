package com.nsylmz.payx.account.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.nsylmz.payx.account.model.Account;

public interface AccountRepository extends ReactiveMongoRepository<Account, String> {

}
