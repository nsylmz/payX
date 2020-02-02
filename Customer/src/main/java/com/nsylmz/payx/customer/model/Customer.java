package com.nsylmz.payx.customer.model;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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
public class Customer {
	
	@Transient
    public static final String SEQUENCE_NAME = "customer_number_sequence";
	
	private String id;
	
	@NotNull(message = "CustomerNumber can't be empty!!!")
	private Long customerNumber;
	
	@NotEmpty(message = "First name can't be empty!")
	private String firstName;
	
	@NotEmpty(message = "Last name can't be empty!")
	private String lastName;
	
	@NotEmpty(message = "Email address can't be empty!")
	@Email(message = "Invalid Email Address!")
	@Indexed(unique=true)
	private String email;
	
	@NotEmpty(message = "Password can't be empty!")
	@Size(min = 6 , message = "Password should be at least 6 characters!")
	@Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Password Should Be in Alphanumeric Format!")
	private String password;
	
	@Pattern(regexp ="[0-9\\s]{10}", message = "Invalid Phone Number!")
	private String phoneNumber;
	
	private Gender gender;
	
	@Pattern(regexp = "^(19|20)\\d\\d[- /.](0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])$", message = "Invalid Date Format!")
	private String birthDate;
	
	@CreatedDate
	private Date signUpDate = new Date();

}
