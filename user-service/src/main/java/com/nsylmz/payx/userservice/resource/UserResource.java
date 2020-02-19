package com.nsylmz.payx.userservice.resource;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import com.nsylmz.payx.userservice.exception.UserRoleNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nsylmz.payx.userservice.exception.RoleNotFoundException;
import com.nsylmz.payx.userservice.exception.UserNotFoundException;
import com.nsylmz.payx.userservice.model.User;
import com.nsylmz.payx.userservice.model.UserRole;
import com.nsylmz.payx.userservice.repository.RoleRepository;
import com.nsylmz.payx.userservice.repository.UserRepository;
import com.nsylmz.payx.userservice.repository.UserRoleRepository;
import com.nsylmz.payx.userservice.service.SequenceGenerator;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class UserResource {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserRoleRepository userRoleRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private SequenceGenerator sequenceGenerator;
	
	@PostMapping("/signUp")
	public Mono<ResponseEntity<Void>> signUp(@Valid @RequestBody User user) {
		user.setUserNumber(sequenceGenerator.generateUserNumberSequence(User.SEQUENCE_NAME));
		return userRepository.save(user).map(savedUser -> ResponseEntity.created(URI.create("/user/" + savedUser.getUserNumber()))
				.contentType(MediaType.APPLICATION_JSON).build());
	}
	
	@PostMapping("/user/{userNumber}/addRole/{roleId}")
	public Mono<ResponseEntity<Void>> addRoleToUser(@PathVariable long userNumber, @PathVariable String roleId) {
		return roleRepository.findById(roleId)
				.flatMap(existingRole -> userRepository.retrieveUserByUserNumber(userNumber)
						.map(existingUser -> UserRole.builder().roleId(roleId).userId(existingUser.getId()).build())
						.switchIfEmpty(Mono.error(new UserNotFoundException("userNumber: " + userNumber + " is not found!!!"))))
				.flatMap(userRole -> userRoleRepository.save(userRole).then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
				.switchIfEmpty(Mono.error(new RoleNotFoundException("No Role Found In DB with given id: " + roleId +" !!!")));
	}
	
	
	@GetMapping("/user")
	public Flux<User> getAllUsers() {
		return userRepository.findAll().map(existingUser -> existingUser)
				.switchIfEmpty(Mono.error(new UserNotFoundException("No User Found In DB!!!")));
	}
	
	@GetMapping("/user/checkUser/{userNumber}")
	public Mono<Boolean> checkUser(@PathVariable long userNumber) {
		return userRepository.retrieveUserByUserNumber(userNumber)
				.map(existingUser -> true)
				.switchIfEmpty(Mono.just(false));
	}
	
	@GetMapping("/user/{userNumber}")
	public Mono<ResponseEntity<User>> getUser(@PathVariable Long userNumber) {
		return userRepository.retrieveUserByUserNumber(userNumber)
				.map(existingUser -> ResponseEntity.ok(existingUser))
				.switchIfEmpty(Mono.error(new UserNotFoundException("userNumber: " + userNumber + " is not found!!!")));
	}
	
	@GetMapping("/user/getUserByEmail/{email}")
	public Mono<ResponseEntity<User>> getUserByEmail(@PathVariable String email) {
		return userRepository.retrieveUserByEmail(email)
				.map(existingUser -> ResponseEntity.ok(existingUser))
				.switchIfEmpty(Mono.error(new UserNotFoundException("No User Found In DB with email address : " + email + " !!!")));
	}
	
	@GetMapping("/user/getUserRolesByEmail/{email}")
	public Mono<List<String>> getUserRolesByEmail(@PathVariable String email) {
		return userRepository.retrieveUserByEmail(email)
				.flatMapMany(existingUser -> userRoleRepository.findAll(Example.of(new UserRole(null, null, existingUser.getId())))
                                                .switchIfEmpty(Mono.error(new UserRoleNotFoundException("No User Role Found In DB for User Id " + existingUser.getId() + " !!!"))))
				.flatMap(existingUserRoles -> roleRepository.findById(existingUserRoles.getRoleId()))
				.map(existingRoles -> existingRoles.getRole()).collectList()
				.switchIfEmpty(Mono.error(new UserNotFoundException("No User Found In DB with email address : " + email + " !!!")));
	}
	
	/*
	@PostMapping("/signIn")
	public Mono<ResponseEntity<Object>> signIn(@Valid @RequestBody CredentialsQueryDTO credentials) {
		return userRepository.retrieveUserByEmail(credentials.getEmail())
				.flatMap(existingUser -> {
					if (existingUser == null) {
						return Mono.error(new UserNotFoundException("No User matched with email address: " + credentials.getEmail() + " !!!"));
					} else if (existingUser.getPassword().equals(credentials.getPassword())) {
						return Mono.just(ResponseEntity.ok().build());
					} else {
						return Mono.just(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
					}
				}).switchIfEmpty(Mono.error(new UserNotFoundException("No User Found In DB with email address : " + credentials.getEmail() + " !!!")));
	}*/

}
