package com.nsylmz.payx.netflixzuulapigatewayserver.proxy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class UserRoleBean {

	private String id;
	
	private String roleId;
	
	private String customerId;
}
