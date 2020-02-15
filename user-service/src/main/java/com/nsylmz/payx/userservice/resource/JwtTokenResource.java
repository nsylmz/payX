package com.nsylmz.payx.userservice.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nsylmz.payx.userservice.exception.TokenNotFoundException;
import com.nsylmz.payx.userservice.model.JwtToken;
import com.nsylmz.payx.userservice.repository.JwtTokenRepository;

import reactor.core.publisher.Mono;

@RestController
public class JwtTokenResource {

private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private JwtTokenRepository jwtTokenRepository;
	
	@GetMapping("/token/{token}")
	public Mono<JwtToken> retrieveToken(@PathVariable String token) {
		return jwtTokenRepository.findById(token).switchIfEmpty(Mono.error(new TokenNotFoundException("No Token Found In DB with given: " + token +" !!!")));
	}
	
	@PostMapping("/token")
	public Mono<ResponseEntity<Void>> addToken(@RequestBody JwtToken jwtToken) {
		return jwtTokenRepository.save(jwtToken).then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)));
	}
	
	@DeleteMapping("/token/{token}")
	public Mono<ResponseEntity<Void>> removeToken(@PathVariable String token) {
		return jwtTokenRepository.findById(token)
				.flatMap(existingRole -> jwtTokenRepository.delete(existingRole).then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
				.switchIfEmpty(Mono.error(new TokenNotFoundException("No Role Found In DB with given id: " + token +" !!!")));
	}
}
