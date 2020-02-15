package com.nsylmz.payx.accountservice.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Document
public class AccountNumberSequence {

	private String id;

    private long seq;
}
