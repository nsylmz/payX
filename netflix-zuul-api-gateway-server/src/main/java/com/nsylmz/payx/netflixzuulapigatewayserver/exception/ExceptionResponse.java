package com.nsylmz.payx.netflixzuulapigatewayserver.exception;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ExceptionResponse {
	
	private Date timestamp;
	
	private String message;
	
	private String details;

}
