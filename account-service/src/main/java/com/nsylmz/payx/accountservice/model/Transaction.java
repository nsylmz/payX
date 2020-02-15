package com.nsylmz.payx.accountservice.model;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Document
public class Transaction {
	
	private String id;
	
	@NotNull(message = "Account Number can't be empty!!!")
	private Long accountNumber;
	
	@NotNull(message = "Customer Number can't be empty!!!")
	private Long customerNumber;
	
	private TransactionType transactionType; 
	
	@NotNull(message = "Transacton Amount can't be empty!!!")
	private Double amount;
	
	@Size(max = 120)
	private String description;
	
	@CreatedDate
	private Date transactionDate = new Date(); 

}
