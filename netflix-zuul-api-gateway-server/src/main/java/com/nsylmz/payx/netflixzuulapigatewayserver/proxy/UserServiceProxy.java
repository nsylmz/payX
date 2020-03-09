package com.nsylmz.payx.netflixzuulapigatewayserver.proxy;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@FeignClient(name="user-service")
@RibbonClient(name="user-service") 
public interface UserServiceProxy {
	
	@GetMapping("/user/{userNumber}")
	public ResponseEntity<UserBean> getUser(@PathVariable("userNumber") long userNumber);
	
	@GetMapping("/user/getUserByEmail/{email}")
	public ResponseEntity<UserBean> getUserByEmail(@PathVariable("email") String email);

	@GetMapping("/user/getUserRolesByEmail/{email}")
	public List<String> getUserRolesByEmail(@PathVariable("email") String email);
	
	@GetMapping("/role/{id}")
	public RoleBean retrieveRole(@PathVariable("id") String id);

	/*
	@GetMapping("/token/{token}")
	public TokenBean retrieveToken(@PathVariable("token") String token);
	
	@PostMapping("/token")
	public ResponseEntity<Void> addToken(@RequestBody TokenBean token);
	
	@DeleteMapping("/token/{token}")
	public ResponseEntity<Void> removeToken(@PathVariable("token") String token);
*/
}
