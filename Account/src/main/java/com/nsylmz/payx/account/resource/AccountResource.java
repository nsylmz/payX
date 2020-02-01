package com.nsylmz.payx.account.resource;

import java.net.URI;

import javax.security.auth.login.AccountNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nsylmz.payx.account.model.Account;
import com.nsylmz.payx.account.model.AccountStatus;
import com.nsylmz.payx.account.model.AccountType;
import com.nsylmz.payx.account.repository.AccountRepository;
import com.nsylmz.payx.account.service.SequenceGenerator;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class AccountResource {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SequenceGenerator sequenceGenerator; 
	
	@Autowired
	private AccountRepository accountRepository;
	
	@GetMapping("/accounts/getAccount/{accountId}")
	private Mono<ResponseEntity<Account>> retrieveAccountById(@PathVariable String accountId) {
		return accountRepository.findById(accountId)
				.map(account -> ResponseEntity.ok(account))
				.switchIfEmpty(Mono.error(new AccountNotFoundException("accountId: " + accountId + " is not found!!!")));
	}
	
	@GetMapping("/accounts/{customerId}")
	private Flux<Account> retrieveAllAccountsOfCustomer(@PathVariable String customerId) {
		// TODO: throw error - no such an customer error
		return accountRepository.findAll(Example.of(new Account(null, customerId, null, null, null, null, null)))
				.switchIfEmpty(Mono.error(new AccountNotFoundException("No account belongs to given customerId: " + customerId + " !!!")));
	}
	
	@GetMapping("/accounts/{customerId}/account/{accountNumber}")
	private Mono<ResponseEntity<Account>> retrieveAccountByCustomerAndAccountNumber(@PathVariable String customerId, @PathVariable Long accountNumber) {
		// TODO: throw error - no such an customer error
		return accountRepository.findOne(Example.of(new Account(null, customerId, accountNumber, null, null, null, null)))
				.map(account -> ResponseEntity.ok(account))
				.switchIfEmpty(Mono.error(new AccountNotFoundException("No account belongs to given customerId: " + customerId + " and is accountNumber" + accountNumber + " !!!")));
	}
	
	// Active & Inactive methods
	@GetMapping("/accounts/{customerId}/actives")
	private Flux<Account> retrieveAllActiveAccountsOfCustomer(@PathVariable String customerId) {
		// TODO: throw error - no such an customer error
		return accountRepository.findAll(Example.of(new Account(null, customerId, null, AccountStatus.ACTIVE, null, null, null)))
				.switchIfEmpty(Mono.error(new AccountNotFoundException("No account ACTIVE belongs to given customerId: " + customerId + " !!!")));
	}
	
	@GetMapping("/accounts/{customerId}/inactives")
	private Flux<Account> retrieveAllInactiveAccountsOfCustomer(@PathVariable String customerId) {
		// TODO: throw error - no such an customer error
		return accountRepository.findAll(Example.of(new Account(null, customerId, null, AccountStatus.INACTIVE, null, null, null)))
				.switchIfEmpty(Mono.error(new AccountNotFoundException("No account INACTIVE belongs to given customerId: " + customerId + " !!!")));
	}
	
	//Debit Methods
	@GetMapping("/accounts/{customerId}/debits")
	private Flux<Account> retrieveAllDebitAccountsOfCustomer(@PathVariable String customerId) {
		// TODO: throw error - no such an customer error
		return accountRepository.findAll(Example.of(new Account(null, customerId, null, null, AccountType.DEBIT, null, null)))
				.switchIfEmpty(Mono.error(new AccountNotFoundException("No account DEBIT belongs to given customerId: " + customerId + " !!!")));
	}
	
	@GetMapping("/accounts/{customerId}/debitActives")
	private Flux<Account> retrieveAllDebitAndActiveAccountsOfCustomer(@PathVariable String customerId) {
		// TODO: throw error - no such an customer error
		return accountRepository.findAll(Example.of(new Account(null, customerId, null, AccountStatus.ACTIVE, AccountType.DEBIT, null, null)))
				.switchIfEmpty(Mono.error(new AccountNotFoundException("No account ACTIVE and DEBIT belongs to given customerId: " + customerId + " !!!")));
	}
	
	@GetMapping("/accounts/{customerId}/debitInactives")
	private Flux<Account> retrieveAllDebitAndInactiveAccountsOfCustomer(@PathVariable String customerId) {
		// TODO: throw error - no such an customer error
		return accountRepository.findAll(Example.of(new Account(null, customerId, null, AccountStatus.INACTIVE, AccountType.DEBIT, null, null)))
				.switchIfEmpty(Mono.error(new AccountNotFoundException("No account INACTIVE and DEBIT belongs to given customerId: " + customerId + " !!!")));
	}
	
	//Deposit Methods
	@GetMapping("/accounts/{customerId}/deposits")
	private Flux<Account> retrieveAllDepositAccountsOfCustomer(@PathVariable String customerId) {
		// TODO: throw error - no such an customer error
		return accountRepository.findAll(Example.of(new Account(null, customerId, null, null, AccountType.DEPOSIT, null, null)))
				.switchIfEmpty(Mono.error(new AccountNotFoundException("No account DEPOSIT belongs to given customerId: " + customerId + " !!!")));
	}
		
	@GetMapping("/accounts/{customerId}/depositActives")
	private Flux<Account> retrieveAllDepositAndActiveAccountsOfCustomer(@PathVariable String customerId) {
		// TODO: throw error - no such a customer error
		return accountRepository.findAll(Example.of(new Account(null, customerId, null, AccountStatus.ACTIVE, AccountType.DEPOSIT, null, null)))
				.switchIfEmpty(Mono.error(new AccountNotFoundException("No account ACTIVE and DEPOSIT belongs to given customerId: " + customerId + " !!!")));
	}
		
	@GetMapping("/accounts/{customerId}/depositInactives")
	private Flux<Account> retrieveAllDepositAndInactiveAccountsOfCustomer(@PathVariable String customerId) {
		// TODO: throw error - no such an customer error
		return accountRepository.findAll(Example.of(new Account(null, customerId, null, AccountStatus.INACTIVE, AccountType.DEPOSIT, null, null)))
				.switchIfEmpty(Mono.error(new AccountNotFoundException("No account INACTIVE and DEPOSIT belongs to given customerId: " + customerId + " !!!")));
	}
	
	// Update account
	/*
	@PostMapping("/accounts")
	private Mono<ResponseEntity<Account>> updateAccount(@RequestBody Account account) {
		return accountRepository.save(account).map(savedAccount -> ResponseEntity.ok(savedAccount));
	}*/
	
	// Open Account Methods
	@PostMapping("/accounts/{customerId}/openDebitAccount")
	private Mono<ResponseEntity<Void>> openDebitAccount(@PathVariable String customerId) {
		Account account = new Account();
		account.setCustomerId(customerId);
		account.setAccountNumber(sequenceGenerator.generateAccountNumberSequence(Account.SEQUENCE_NAME));
		account.setAccountType(AccountType.DEBIT);
		account.setBalance(0.00);
		//linkTo(methodOn(AccountResource.class).retrieveAccountById(savedAccount.getId())).withRel("account").toUri())
		return accountRepository.save(account).map(savedAccount -> ResponseEntity.created(URI.create("/accounts/getAccount/" + savedAccount.getId()))
				.contentType(MediaType.APPLICATION_JSON).build());
	}
	
	@PostMapping("/accounts/{customerId}/openDepositAccount")
	private Mono<ResponseEntity<Void>> openDepositAccount(@PathVariable String customerId) {
		Account account = new Account();
		account.setCustomerId(customerId);
		account.setAccountNumber(sequenceGenerator.generateAccountNumberSequence(Account.SEQUENCE_NAME));
		account.setAccountType(AccountType.DEPOSIT);
		account.setBalance(0.00);
		return accountRepository.save(account).map(savedAccount -> ResponseEntity.created(URI.create("/accounts/getAccount/" + savedAccount.getId()))
				.contentType(MediaType.APPLICATION_JSON).build());
	}
	
	// Active Account
	@PostMapping("/accounts/{accountId}/activeAccount")
	private Mono<ResponseEntity<Void>> activeAccount(@PathVariable String accountId) {
		return Mono.just(accountId).flatMap(accountRepository::findById)
				.flatMap(account -> {
					if (account == null) {
						return Mono.error(new AccountNotFoundException());
					} else if (account.getAccountStatus().equals(AccountStatus.INACTIVE)) {
						account.setAccountStatus(AccountStatus.ACTIVE);
						return accountRepository.save(account).then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)));
					} else {
						return Mono.error(new RuntimeException("Given Account " + accountId + " is already active!!!"));
					}
				});
	}
	
	// Inactive Account
	@PostMapping("/accounts/{accountId}/inactiveAccount")
	private Mono<ResponseEntity<Void>> inactiveAccount(@PathVariable String accountId) {
		return Mono.just(accountId).flatMap(accountRepository::findById)
							.flatMap(account -> {
								if (account == null) {
									return Mono.error(new AccountNotFoundException());
								} else if (account.getAccountStatus().equals(AccountStatus.ACTIVE)) {
									account.setAccountStatus(AccountStatus.INACTIVE);
									return accountRepository.save(account).then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)));
								} else {
									return Mono.error(new RuntimeException("Given Account " + accountId + " is already inactive!!!"));
								}
							});
	}
	
	// Deposit Account
	@PostMapping("/accounts/{accountId}/deposit/{amount}")
	private Mono<ResponseEntity<Void>> depositAccount(@PathVariable String accountId, @PathVariable double amount) {
		return accountRepository.findById(accountId)
				.map(existingAccount -> {existingAccount.setBalance(existingAccount.getBalance() + amount); return existingAccount;})
				.flatMap(existingAccount -> accountRepository.save(existingAccount).then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
				.switchIfEmpty(Mono.error(new AccountNotFoundException("accountId: " + accountId + " is not found!!!")));
	}
	
	@PostMapping("/accounts/{accountId}/withdraw/{amount}")
	private Mono<ResponseEntity<Void>> withdrawAccount(@PathVariable String accountId, @PathVariable double amount) {
		return accountRepository.findById(accountId)
				.flatMap(existingAccount -> { if (existingAccount.getBalance() >= amount) { 
											  existingAccount.setBalance(existingAccount.getBalance() - amount); 
										  } else {
											  return Mono.error(new IllegalArgumentException("Given Withdrawal Amount" + amount + " is higeher than Account Balance: " + existingAccount.getBalance()));
										  }
										  return Mono.just(existingAccount);
										})
				.flatMap(existingAccount -> accountRepository.save(existingAccount).then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
				.switchIfEmpty(Mono.error(new AccountNotFoundException("accountId: " + accountId + " is not found!!!")));
	}
	
	// Delete account methods
	@DeleteMapping("/accounts/{accountId}")
	private Mono<ResponseEntity<Void>> deleteAccountById(@PathVariable String accountId) {
		return accountRepository.findById(accountId)
					.flatMap(existingAccount -> accountRepository.delete(existingAccount).then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
					.switchIfEmpty(Mono.error(new AccountNotFoundException("accountId: " + accountId + " is not found!!!")));
	}
	
	@DeleteMapping("/accounts/{customerId}/account/{accountNumber}")
	private Mono<ResponseEntity<Void>> deleteAccountByCustomerAndAccountNumber(@PathVariable String customerId, @PathVariable Long accountNumber) {
		return accountRepository.findOne(Example.of(new Account(null, customerId, accountNumber, null, null, null, null)))
				.flatMap(existingAccount -> accountRepository.delete(existingAccount).then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
				.switchIfEmpty(Mono.error(new AccountNotFoundException("No account belongs to given customerId: " + customerId + " and is accountNumber" + accountNumber + " !!!")));
		
	}	

}
