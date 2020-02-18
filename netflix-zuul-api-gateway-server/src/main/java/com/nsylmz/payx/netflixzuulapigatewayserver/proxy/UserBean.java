package com.nsylmz.payx.netflixzuulapigatewayserver.proxy;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class UserBean {

		private String id;
		
		private String userNumber;
		
		private String firstName;
		
		private String lastName;
		
		private String email;
		
		private String password;
		
		private String phoneNumber;
		
		private String gender;
		
		private String birthDate;
		
		private Date signUpDate;
		
		private Integer active;
	    
		private boolean isLoacked;
	    
		private boolean isExpired;
	    
		private boolean isEnabled;
}
