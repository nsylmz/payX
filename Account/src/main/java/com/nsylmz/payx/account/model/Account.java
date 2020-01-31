package com.nsylmz.payx.account.model;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Document
public class Account {
	
	@Transient
    public static final String SEQUENCE_NAME = "account_number_sequence";
	
	private String id;
	
    private String userId;
    
    @Indexed(unique=true)
    private Long accountNumber;
    
    private AccountStatus accountStatus = AccountStatus.ACTIVE;
    
    private AccountType accountType;
    
    private Double balance;
    
    @CreatedDate
    private Date creationTime = new Date();

}
