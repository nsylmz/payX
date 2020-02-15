package com.nsylmz.payx.accountservice.model;

public enum TransactionType {
	
	DEPOSIT("1"), WITHDRAWAL("2");
	
	private String value;
	
	TransactionType(String value) {
		this.value = value;
	}
	
	private String getValue() {
		return value;
	}

}
