package com.nsylmz.payx.account.proxy;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Document
public class CustomerBean {

		private String id;
		
		private String firstName;
		
		private String lastName;
		
		private String email;
		
		private String password;
		
		private String phoneNumber;
		
		private String gender;
		
		private String birthDate;
		
		private Date signUpDate;
}
