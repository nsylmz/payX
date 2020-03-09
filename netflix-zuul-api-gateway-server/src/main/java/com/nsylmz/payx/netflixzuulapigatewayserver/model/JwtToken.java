package com.nsylmz.payx.netflixzuulapigatewayserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@RedisHash("jwtToken")
public class JwtToken {

	@Id
	private String id;

}
