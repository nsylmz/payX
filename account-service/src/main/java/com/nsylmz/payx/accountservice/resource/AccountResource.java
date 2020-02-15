package com.nsylmz.payx.accountservice.resource;

import java.net.URI;

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

import com.nsylmz.payx.accountservice.exception.AccountNotFoundException;
import com.nsylmz.payx.accountservice.exception.CustomerNotFoundException;
import com.nsylmz.payx.accountservice.model.Account;
import com.nsylmz.payx.accountservice.model.AccountStatus;
import com.nsylmz.payx.accountservice.model.AccountType;
import com.nsylmz.payx.accountservice.proxy.UserServiceProxy;
import com.nsylmz.payx.accountservice.repository.AccountRepository;
import com.nsylmz.payx.accountservice.service.SequenceGenerator;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class AccountResource {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SequenceGenerator sequenceGenerator; 
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private UserServiceProxy userServiceProxy;
	
	@GetMapping("/accounts/getAccountById/{accountId}")
	private Mono<ResponseEntity<Account>> retrieveAccountById(@PathVariable String accountId) {
		return accountRepository.findById(accountId)
				.map(account -> ResponseEntity.ok(account))
				.switchIfEmpty(Mono.error(new AccountNotFoundException("accountId: " + accountId + " is not found!!!")));
	}

	@GetMapping("/accounts/getAccountByNumber/{accountNumber}")
	private Mono<ResponseEntity<Account>> retrieveAccountById(@PathVariable long accountNumber) {
		return accountRepository.retrieveAccountByAccountNumber(accountNumber)
				.map(account -> ResponseEntity.ok(account))
				.switchIfEmpty(Mono.error(new AccountNotFoundException("accountNumber: " + accountNumber + " is not found!!!")));
	}
	
	@GetMapping("/accounts/{customerNumber}")
	private Flux<Account> retrieveAllAccountsOfCustomer(@PathVariable Long customerNumber) {
		return accountRepository.findAll(Example.of(new Account(null, customerNumber, null, null, null, null, null)))
				.switchIfEmpty(Mono.error(new AccountNotFoundException("No account belongs to given customerNumber: " + customerNumber + " !!!")));
	}
	
	@GetMapping("/accounts/{customerNumber}/account/{accountNumber}")
	private Mono<ResponseEntity<Account>> retrieveAccountByCustomerAndAccountNumber(@PathVariable Long customerNumber, @PathVariable Long accountNumber) {
		return accountRepository.findOne(Example.of(new Account(null, customerNumber, accountNumber, null, null, null, null)))
				.map(account -> ResponseEntity.ok(account))
				.switchIfEmpty(Mono.error(new AccountNotFoundException("No account belongs to given customerNumber: " + customerNumber + " and is accountNumber" + accountNumber + " !!!")));
	}
	
	// Active & Inactive methods
	@GetMapping("/accounts/{customerNumber}/actives")
	private Flux<Account> retrieveAllActiveAccountsOfCustomer(@PathVariable Long customerNumber) {
		return accountRepository.findAll(Example.of(new Account(null, customerNumber, null, AccountStatus.ACTIVE, null, null, null)))
				.switchIfEmpty(Mono.error(new AccountNotFoundException("No account ACTIVE belongs to given customerNumber: " + customerNumber + " !!!")));
	}
	
	@GetMapping("/accounts/{customerNumber}/inactives")
	private Flux<Account> retrieveAllInactiveAccountsOfCustomer(@PathVariable Long customerNumber) {
		return accountRepository.findAll(Example.of(new Account(null, customerNumber, null, AccountStatus.INACTIVE, null, null, null)))
				.switchIfEmpty(Mono.error(new AccountNotFoundException("No account INACTIVE belongs to given customerNumber: " + customerNumber + " !!!")));
	}
	
	//Debit Methods
	@GetMapping("/accounts/{customerNumber}/debits")
	private Flux<Account> retrieveAllDebitAccountsOfCustomer(@PathVariable Long customerNumber) {
		return accountRepository.findAll(Example.of(new Account(null, customerNumber, null, null, AccountType.DEBIT, null, null)))
				.switchIfEmpty(Mono.error(new AccountNotFoundException("No account DEBIT belongs to given customerNumber: " + customerNumber + " !!!")));
	}
	
	@GetMapping("/accounts/{customerNumber}/debitActives")
	private Flux<Account> retrieveAllDebitAndActiveAccountsOfCustomer(@PathVariable Long customerNumber) {
		return accountRepository.findAll(Example.of(new Account(null, customerNumber, null, AccountStatus.ACTIVE, AccountType.DEBIT, null, null)))
				.switchIfEmpty(Mono.error(new AccountNotFoundException("No account ACTIVE and DEBIT belongs to given customerNumber: " + customerNumber + " !!!")));
	}
	
	@GetMapping("/accounts/{customerNumber}/debitInactives")
	private Flux<Account> retrieveAllDebitAndInactiveAccountsOfCustomer(@PathVariable Long customerNumber) {
		return accountRepository.findAll(Example.of(new Account(null, customerNumber, null, AccountStatus.INACTIVE, AccountType.DEBIT, null, null)))
				.switchIfEmpty(Mono.error(new AccountNotFoundException("No account INACTIVE and DEBIT belongs to given customerNumber: " + customerNumber + " !!!")));
	}
	
	//Deposit Methods
	@GetMapping("/accounts/{customerNumber}/deposits")
	private Flux<Account> retrieveAllDepositAccountsOfCustomer(@PathVariable Long customerNumber) {
		return accountRepository.findAll(Example.of(new Account(null, customerNumber, null, null, AccountType.DEPOSIT, null, null)))
				.switchIfEmpty(Mono.error(new AccountNotFoundException("No account DEPOSIT belongs to given customerNumber: " + customerNumber + " !!!")));
	}
		
	@GetMapping("/accounts/{customerNumber}/depositActives")
	private Flux<Account> retrieveAllDepositAndActiveAccountsOfCustomer(@PathVariable Long customerNumber) {
		return accountRepository.findAll(Example.of(new Account(null, customerNumber, null, AccountStatus.ACTIVE, AccountType.DEPOSIT, null, null)))
				.switchIfEmpty(Mono.error(new AccountNotFoundException("No account ACTIVE and DEPOSIT belongs to given customerNumber: " + customerNumber + " !!!")));
	}
		
	@GetMapping("/accounts/{customerNumber}/depositInactives")
	private Flux<Account> retrieveAllDepositAndInactiveAccountsOfCustomer(@PathVariable Long customerNumber) {
		return accountRepository.findAll(Example.of(new Account(null, customerNumber, null, AccountStatus.INACTIVE, AccountType.DEPOSIT, null, null)))
				.switchIfEmpty(Mono.error(new AccountNotFoundException("No account INACTIVE and DEPOSIT belongs to given customerNumber: " + customerNumber + " !!!")));
	}
	
	// Update account
	/*
	@PostMapping("/accounts")
	private Mono<ResponseEntity<Account>> updateAccount(@RequestBody Account account) {
		return accountRepository.save(account).map(savedAccount -> ResponseEntity.ok(savedAccount));
	}*/
	
	// Open Account Methods
	@PostMapping("/accounts/{customerNumber}/openDebitAccount")
	private Mono<ResponseEntity<Void>> openDebitAccount(@PathVariable Long customerNumber) {
		Boolean checkCustomer = userServiceProxy.checkUser(customerNumber);
		return Mono.just(checkCustomer).flatMap(isCustomerAvailable -> {
			if (!isCustomerAvailable) {
				return Mono.error(new CustomerNotFoundException("Customer " + customerNumber + " is not found!!!"));
			} else {
				Account account = new Account();
				account.setCustomerNumber(customerNumber);
				account.setAccountNumber(sequenceGenerator.generateAccountNumberSequence(Account.SEQUENCE_NAME));
				account.setAccountType(AccountType.DEBIT);
				account.setBalance(0.00);
				//linkTo(methodOn(AccountResource.class).retrieveAccountById(savedAccount.getId())).withRel("account").toUri())
				return accountRepository.save(account).map(savedAccount -> ResponseEntity.created(URI.create("/accounts/getAccountByNumber/" + savedAccount.getAccountNumber()))
						.contentType(MediaType.APPLICATION_JSON).build());
			}
		});
		
	}
	
	@PostMapping("/accounts/{customerNumber}/openDepositAccount")
	private Mono<ResponseEntity<Void>> openDepositAccount(@PathVariable Long customerNumber) {
		Boolean checkCustomer = userServiceProxy.checkUser(customerNumber);
		return Mono.just(checkCustomer).flatMap(isCustomerAvailable -> {
			if (!isCustomerAvailable) {
				return Mono.error(new CustomerNotFoundException("Customer" + customerNumber + " is not found!!!"));
			} else {
				Account account = new Account();
				account.setCustomerNumber(customerNumber);
				account.setAccountNumber(sequenceGenerator.generateAccountNumberSequence(Account.SEQUENCE_NAME));
				account.setAccountType(AccountType.DEPOSIT);
				account.setBalance(0.00);
				return accountRepository.save(account).map(savedAccount -> ResponseEntity.created(URI.create("/accounts/getAccountByNumber/" + savedAccount.getAccountNumber()))
						.contentType(MediaType.APPLICATION_JSON).build());
			}
		});
	}
	
	// Active Account
	@PostMapping("/accounts/{accountNumber}/activeAccount")
	private Mono<ResponseEntity<Void>> activeAccount(@PathVariable long accountNumber) {
		return accountRepository.retrieveAccountByAccountNumber(accountNumber)
				.flatMap(account -> {
					if (account.getAccountStatus().equals(AccountStatus.INACTIVE)) {
						account.setAccountStatus(AccountStatus.ACTIVE);
						return accountRepository.save(account).then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)));
					} else {
						return Mono.error(new RuntimeException("Given Account " + accountNumber + " is already active!!!"));
					}
				}).switchIfEmpty(Mono.error(new AccountNotFoundException("AccountNumber: " + accountNumber + " is not found!!!")));
	}
	
	// Inactive Account
	@PostMapping("/accounts/{accountNumber}/inactiveAccount")
	private Mono<ResponseEntity<Void>> inactiveAccount(@PathVariable long accountNumber) {
		return accountRepository.retrieveAccountByAccountNumber(accountNumber)
				.flatMap(account -> {
					if (account.getAccountStatus().equals(AccountStatus.ACTIVE)) {
						account.setAccountStatus(AccountStatus.INACTIVE);
						return accountRepository.save(account).then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)));
					} else {
						return Mono.error(new RuntimeException("Given Account " + accountNumber + " is already inactive!!!"));
					}
				}).switchIfEmpty(Mono.error(new AccountNotFoundException("AccountNumber: " + accountNumber + " is not found!!!")));
	}
	
	// Delete account methods
	@DeleteMapping("/accounts/{accountId}")
	private Mono<ResponseEntity<Void>> deleteAccountById(@PathVariable String accountId) {
		return accountRepository.findById(accountId)
					.flatMap(existingAccount -> accountRepository.delete(existingAccount).then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
					.switchIfEmpty(Mono.error(new AccountNotFoundException("AccountId: " + accountId + " is not found!!!")));
	}
	
	@DeleteMapping("/accounts/{customerNumber}/account/{accountNumber}")
	private Mono<ResponseEntity<Void>> deleteAccountByCustomerAndAccountNumber(@PathVariable Long customerNumber, @PathVariable Long accountNumber) {
		return accountRepository.retrieveAccountByAccountNumber(accountNumber)
				.flatMap(existingAccount -> accountRepository.delete(existingAccount).then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
				.switchIfEmpty(Mono.error(new AccountNotFoundException("No account belongs to given customerNumber: " + customerNumber + " and is accountNumber" + accountNumber + " !!!")));
		
	}	

}
