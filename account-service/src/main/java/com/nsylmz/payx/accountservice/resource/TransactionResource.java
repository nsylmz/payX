package com.nsylmz.payx.accountservice.resource;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nsylmz.payx.accountservice.dto.TransactionDTO;
import com.nsylmz.payx.accountservice.dto.TransactionQueryDTO;
import com.nsylmz.payx.accountservice.exception.AccountNotFoundException;
import com.nsylmz.payx.accountservice.model.Transaction;
import com.nsylmz.payx.accountservice.model.TransactionType;
import com.nsylmz.payx.accountservice.repository.AccountRepository;
import com.nsylmz.payx.accountservice.repository.TransactionRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class TransactionResource {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@GetMapping("/transactions/{accountNumber}/accountTransactionList")
	private Flux<TransactionDTO> transactionListByAccount(@PathVariable long accountNumber) {
		return transactionRepository.retrieveAllTransactionsForAccount(accountNumber, Sort.by(Sort.Direction.DESC, "transactionDate"))
				.flatMap(transaction -> {
					TransactionDTO transactionDTO = new TransactionDTO();
					transactionDTO.setAccountNumber(transaction.getAccountNumber());
					transactionDTO.setCustomerNumber(transaction.getCustomerNumber());
					transactionDTO.setDescription(transaction.getDescription());
					transactionDTO.setTransactionType(transaction.getTransactionType());
					transactionDTO.setTransactionDate(transaction.getTransactionDate());
					transactionDTO.setAmount(transaction.getAmount());
					return Mono.just(transactionDTO);
				}).switchIfEmpty(Mono.error(new AccountNotFoundException("No Transaction is found for accountNumber: " + accountNumber + " !!!")));
	}
	
	@GetMapping("/transactions/{customerNumber}/customerTransactionList")
	private Flux<TransactionDTO> transactionListByCustomer(@PathVariable long customerNumber) {
		return transactionRepository.retrieveAllTransactionsForCustomer(customerNumber, Sort.by(Sort.Direction.DESC, "transactionDate"))
				.flatMap(transaction -> {
					TransactionDTO transactionDTO = new TransactionDTO();
					transactionDTO.setAccountNumber(transaction.getAccountNumber());
					transactionDTO.setCustomerNumber(transaction.getCustomerNumber());
					transactionDTO.setDescription(transaction.getDescription());
					transactionDTO.setTransactionType(transaction.getTransactionType());
					transactionDTO.setTransactionDate(transaction.getTransactionDate());
					transactionDTO.setAmount(transaction.getAmount());
					return Mono.just(transactionDTO);
				}).switchIfEmpty(Mono.error(new AccountNotFoundException("No Transaction is found for customerNumber: " + customerNumber + " !!!")));
	}
	
	// Deposit Account
	@PostMapping("/transactions/deposit")
	private Mono<ResponseEntity<Void>> depositAccount(@Valid @RequestBody TransactionQueryDTO transactionQueryDTO) {
		return accountRepository.retrieveAccountByAccountNumber(transactionQueryDTO.getAccountNumber())
				.flatMap(existingAccount -> {
					existingAccount.setBalance(existingAccount.getBalance() + transactionQueryDTO.getAmount()); 
					return Mono.just(existingAccount);
				}).flatMap(existingAccount -> accountRepository.save(existingAccount))
				.flatMap(existingAccount -> { 
					Transaction transaction = new Transaction();
					transaction.setAccountNumber(existingAccount.getAccountNumber());
					transaction.setCustomerNumber(existingAccount.getCustomerNumber());
					transaction.setTransactionType(TransactionType.DEPOSIT);
					transaction.setDescription(transactionQueryDTO.getDescription());
					transaction.setAmount(transactionQueryDTO.getAmount());
					return Mono.just(transaction);					
				}).flatMap(transaction -> transactionRepository.save(transaction).then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
				.switchIfEmpty(Mono.error(new AccountNotFoundException("AccountNumber: " + transactionQueryDTO.getAccountNumber() + " is not found!!!")));
	}
		
	@PostMapping("/transactions/withdraw")
	private Mono<ResponseEntity<Void>> withdrawAccount(@Valid @RequestBody TransactionQueryDTO transactionQueryDTO) {
		return accountRepository.retrieveAccountByAccountNumber(transactionQueryDTO.getAccountNumber())
				.flatMap(existingAccount -> { 
					if (existingAccount.getBalance() >= transactionQueryDTO.getAmount()) { 
						existingAccount.setBalance(existingAccount.getBalance() - transactionQueryDTO.getAmount());
						return Mono.just(existingAccount);
					} else {
						return Mono.error(new IllegalArgumentException("Given Withdrawal Amount " + transactionQueryDTO.getAmount() + " is higeher than Account Balance: " + existingAccount.getBalance()));
					}
				}).flatMap(existingAccount -> accountRepository.save(existingAccount))
				.flatMap(existingAccount -> { 
					Transaction transaction = new Transaction();
					transaction.setAccountNumber(existingAccount.getAccountNumber());
					transaction.setCustomerNumber(existingAccount.getCustomerNumber());
					transaction.setTransactionType(TransactionType.WITHDRAWAL);
					transaction.setDescription(transactionQueryDTO.getDescription());
					transaction.setAmount(transactionQueryDTO.getAmount());
					return Mono.just(transaction);					
				}).flatMap(transaction -> transactionRepository.save(transaction).then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
				.switchIfEmpty(Mono.error(new AccountNotFoundException("AccountNumber: " + transactionQueryDTO.getAccountNumber() + " is not found!!!")));
	}
		
}
