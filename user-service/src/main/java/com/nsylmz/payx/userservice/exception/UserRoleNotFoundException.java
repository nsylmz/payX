package com.nsylmz.payx.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserRoleNotFoundException extends RuntimeException {

	public UserRoleNotFoundException(String message) {
		super(message);
	}

}
