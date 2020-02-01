package com.nsylmz.payx.customer.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Credentials {
	
	@NotEmpty(message = "Email address can't be empty!")
	@Email(message = "Invalid Email Address!")
	private String email;
	
	@NotEmpty(message = "Password can't be empty!")
	@Size(min = 6 , message = "Password should be at least 6 characters!")
	@Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Password Should Be in Alphanumeric Format!")
	private String password; 

}
