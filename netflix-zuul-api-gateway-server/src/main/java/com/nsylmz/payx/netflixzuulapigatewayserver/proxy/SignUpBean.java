package com.nsylmz.payx.netflixzuulapigatewayserver.proxy;

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
public class SignUpBean {
	
	@NotEmpty(message = "First name can't be empty!")
	private String firstName;
	
	@NotEmpty(message = "Last name can't be empty!")
	private String lastName;
	
	@NotEmpty(message = "Email address can't be empty!")
	@Email(message = "Invalid Email Address!")
	private String email;
	
	@NotEmpty(message = "Password can't be empty!")
	@Size(min = 6 , message = "Password should be at least 6 characters!")
	@Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Password Should Be in Alphanumeric Format!")
	private String password;
	
	@Pattern(regexp ="[0-9\\s]{10}", message = "Invalid Phone Number!")
	private String phoneNumber;
	
	@NotEmpty(message = "Gender can't be empty!")
	private String gender;
	
	@Pattern(regexp = "^(19|20)\\d\\d[- /.](0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])$", message = "Invalid Date Format!")
	private String birthDate;

}
