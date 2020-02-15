package com.nsylmz.payx.accountservice.dto;

import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class TransactionQueryDTO {
	
	private long accountNumber;
	
	@Size(max = 120)
	private String description;
	
	private double amount;

}
