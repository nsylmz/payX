package com.nsylmz.payx.account.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nsylmz.payx.account.model.Account;
import com.nsylmz.payx.account.model.AccountStatus;
import com.nsylmz.payx.account.model.AccountType;
import com.nsylmz.payx.account.repository.AccountRepository;
import com.nsylmz.payx.account.util.SequenceGenerator;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class AccountService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SequenceGenerator sequenceGenerator; 
	
	@Autowired
	private AccountRepository accountRepository;
	
	@GetMapping("/accounts/getAccount/{id}")
	private Mono<Account> retrieveAccountById(@PathVariable String accountId) {
		// TODO: throw error - no such an account error
		return accountRepository.findOne(Example.of(new Account(accountId, null, null, null, null, null, null)));
	}
	
	@GetMapping("/accounts/{userId}")
	private Flux<Account> retrieveAllAccountsOfUser(@PathVariable String userId) {
		// TODO: throw error - no such an account error
		return accountRepository.findAll(Example.of(new Account(null, userId, null, null, null, null, null)));
	}
	
	@GetMapping("/accounts/{userId}/account/{accountNumber}")
	private Mono<Account> retrieveAccountByUserAndAccountNumber(@PathVariable String userId, @PathVariable Long accountNumber) {
		// TODO: throw error - no such an account error
		return accountRepository.findOne(Example.of(new Account(null, userId, accountNumber, null, null, null, null)));
	}
	
	// Active & Inactive methods
	@GetMapping("/accounts/{userId}/actives")
	private Flux<Account> retrieveAllActiveAccountsOfUser(@PathVariable String userId) {
		// TODO: throw error - no such an account error
		return accountRepository.findAll(Example.of(new Account(null, userId, null, AccountStatus.ACTIVE, null, null, null)));
	}
	
	@GetMapping("/accounts/{userId}/inactives")
	private Flux<Account> retrieveAllInactiveAccountsOfUser(@PathVariable String userId) {
		// TODO: throw error - no such an account error
		return accountRepository.findAll(Example.of(new Account(null, userId, null, AccountStatus.INACTIVE, null, null, null)));
	}
	
	//Debit Methods
	@GetMapping("/accounts/{userId}/debits")
	private Flux<Account> retrieveAllDebitAccountsOfUser(@PathVariable String userId) {
		// TODO: throw error - no such an account error
		return accountRepository.findAll(Example.of(new Account(null, userId, null, null, AccountType.DEBIT, null, null)));
	}
	
	@GetMapping("/accounts/{userId}/debitActives")
	private Flux<Account> retrieveAllDebitAndActiveAccountsOfUser(@PathVariable String userId) {
		// TODO: throw error - no such an account error
		return accountRepository.findAll(Example.of(new Account(null, userId, null, AccountStatus.ACTIVE, AccountType.DEBIT, null, null)));
	}
	
	@GetMapping("/accounts/{userId}/debitInactives")
	private Flux<Account> retrieveAllDebitAndInactiveAccountsOfUser(@PathVariable String userId) {
		// TODO: throw error - no such an account error
		return accountRepository.findAll(Example.of(new Account(null, userId, null, AccountStatus.INACTIVE, AccountType.DEBIT, null, null)));
	}
	
	//Deposit Methods
	@GetMapping("/accounts/{userId}/deposits")
	private Flux<Account> retrieveAllDepositAccountsOfUser(@PathVariable String userId) {
		// TODO: throw error - no such an account error
		return accountRepository.findAll(Example.of(new Account(null, userId, null, null, AccountType.DEPOSIT, null, null)));
	}
		
	@GetMapping("/accounts/{userId}/depositActives")
	private Flux<Account> retrieveAllDepositAndActiveAccountsOfUser(@PathVariable String userId) {
		// TODO: throw error - no such an account error
		return accountRepository.findAll(Example.of(new Account(null, userId, null, AccountStatus.ACTIVE, AccountType.DEPOSIT, null, null)));
	}
		
	@GetMapping("/accounts/{userId}/depositInactives")
	private Flux<Account> retrieveAllDepositAndInactiveAccountsOfUser(@PathVariable String userId) {
		// TODO: throw error - no such an account error
		return accountRepository.findAll(Example.of(new Account(null, userId, null, AccountStatus.INACTIVE, AccountType.DEPOSIT, null, null)));
	}
	
	// Update account
	@PostMapping("/accounts")
	private Mono<ResponseEntity<Account>> updateAccount(@RequestBody Account account) {
		return accountRepository.save(account).map(savedAccount -> ResponseEntity.ok(savedAccount));
	}
	
	// Open Account Methods
	@PostMapping("/accounts/{userId}/openDebitAccount")
	private Mono<ResponseEntity<Account>> openDebitAccount(@PathVariable String userId) {
		Account account = new Account();
		account.setUserId(userId);
		account.setAccountNumber(sequenceGenerator.generateAccountNumberSequence(Account.SEQUENCE_NAME));
		account.setAccountType(AccountType.DEBIT);
		account.setBalance(0.00);
		return accountRepository.save(account).map(savedAccount -> ResponseEntity.ok(savedAccount));
	}
	
	@PostMapping("/accounts/{userId}/openDepositAccount")
	private Mono<ResponseEntity<Account>> openDepositAccount(@PathVariable String userId) {
		Account account = new Account();
		account.setUserId(userId);
		account.setAccountNumber(sequenceGenerator.generateAccountNumberSequence(Account.SEQUENCE_NAME));
		account.setAccountType(AccountType.DEPOSIT);
		account.setBalance(0.00);
		return accountRepository.save(account).map(savedAccount -> ResponseEntity.ok(savedAccount));
	}
	
	// Active Account
	@PostMapping("/accounts/{accountId}/activeAccount")
	private Mono<ResponseEntity<Account>> activeAccount(@PathVariable String accountId) {
		Account account = accountRepository.findById(accountId).block();
		if (account == null) {
			// TODO: throw error - no such an account error
			return null;
		} else if (account.getAccountStatus().equals(AccountStatus.INACTIVE)) {
			account.setAccountStatus(AccountStatus.ACTIVE);
			return accountRepository.save(account).map(savedAccount -> ResponseEntity.ok(savedAccount));
		} else {
			// TODO: throw error - account is already active
			return null;
		}
	}
	
	// Inactive Account
	@PostMapping("/accounts/{accountId}/inactiveAccount")
	private Mono<ResponseEntity<Account>> inactiveAccount(@PathVariable String accountId) {
		Account account = accountRepository.findById(accountId).block();
		if (account == null) {
			// TODO: throw error - no such an account error
			return null;
		} else if (account.getAccountStatus().equals(AccountStatus.ACTIVE)) {
			account.setAccountStatus(AccountStatus.INACTIVE);
			return accountRepository.save(account).map(savedAccount -> ResponseEntity.ok(savedAccount));
		} else {
			// TODO: throw error - account is already inactive
			return null;
		}
	}
	
	// Deposit Account
	@PostMapping("/accounts/{accountId}/deposit/{amount}")
	private Mono<ResponseEntity<Account>> depositAccount(@PathVariable String accountId, @RequestBody double amount) {
		Account account = accountRepository.findById(accountId).block();
		if (account == null) {
			// TODO: throw error - no such an account error
			return null;
		} else if (account.getAccountStatus().equals(AccountStatus.ACTIVE)) {
			account.setBalance(account.getBalance() + amount);
			return accountRepository.save(account).map(savedAccount -> ResponseEntity.ok(savedAccount));
		} else {
			// TODO: throw error - account is inactive
			return null;
		}
	}
	
	@PostMapping("/accounts/{accountId}/withdraw/{amount}")
	private Mono<ResponseEntity<Account>> withdrawAccount(@PathVariable String accountId, @RequestBody double amount) {
		Account account = accountRepository.findById(accountId).block();
		if (account == null) {
			// TODO: throw error - no such an account error
			return null;
		} else if (account.getAccountStatus().equals(AccountStatus.ACTIVE)) {
			if (account.getBalance() >= amount) {
				account.setBalance(account.getBalance() - amount);
				return accountRepository.save(account).map(savedAccount -> ResponseEntity.ok(savedAccount));
			} else {
				// TODO: throw error - account balance is smaller than the amount
				return null;
			}
		} else {
			// TODO: throw error - account is inactive
			return null;
		}
	}
	
	// Delete account methods
	@DeleteMapping("/accounts/{accountId}")
	private void deleteAccountById(@PathVariable String accountId) {
		// TODO: null check
		accountRepository.delete(accountRepository.findOne(Example.of(new Account(accountId, null, null, null, null, null, null))).block());
		logger.debug("Account has been deleted!!!");
	}
	
	@DeleteMapping("/accounts/{userId}/account/{accountNumber}")
	private void deleteAccountByOwnerAndAccountNumber(@PathVariable String userId, @PathVariable Long accountNumber) {
		// TODO: null check
		accountRepository.delete(accountRepository.findOne(Example.of(new Account(null, userId, accountNumber, null, null, null, null))).block());
		logger.debug("Account has been deleted!!!");
	}	

}
