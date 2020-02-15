package com.nsylmz.payx.accountservice.dto;

import java.util.Date;

import com.nsylmz.payx.accountservice.model.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
	
	private Long accountNumber;
	
	private Long customerNumber;
	
	private TransactionType transactionType; 
	
	private Double amount;
	
	private String description;
	
	private Date transactionDate; 

}
