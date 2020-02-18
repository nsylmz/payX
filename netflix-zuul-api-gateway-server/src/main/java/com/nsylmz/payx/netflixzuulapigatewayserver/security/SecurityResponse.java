package com.nsylmz.payx.netflixzuulapigatewayserver.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class SecurityResponse {
	
	private String accessToken;

}
