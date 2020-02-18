package com.nsylmz.payx.netflixzuulapigatewayserver.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class LoginRequest {
	
	private String username;
    
	private String password;

}
