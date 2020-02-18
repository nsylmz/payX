package com.nsylmz.payx.netflixzuulapigatewayserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthException extends RuntimeException {
	
	public AuthException(String message) {
		super(message);
	}

}
