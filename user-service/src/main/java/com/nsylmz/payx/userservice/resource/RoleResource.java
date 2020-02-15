package com.nsylmz.payx.userservice.resource;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nsylmz.payx.userservice.exception.RoleNotFoundException;
import com.nsylmz.payx.userservice.model.Role;
import com.nsylmz.payx.userservice.repository.RoleRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class RoleResource {

private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private RoleRepository roleRepository;
	
	@GetMapping("/role")
	public Flux<Role> retrieveAllRoles() {
		return roleRepository.findAll().switchIfEmpty(Mono.error(new RoleNotFoundException("No Role Found In DB!!!")));
	}
	
	@GetMapping("/role/{id}")
	public Mono<Role> retrieveRole(@PathVariable String id) {
		return roleRepository.findById(id).switchIfEmpty(Mono.error(new RoleNotFoundException("No Role Found In DB with given id: " + id +" !!!")));
	}
	
	@PostMapping("/role")
	public Mono<ResponseEntity<Void>> defineARole(@RequestBody Role role) {
		return roleRepository.save(role).map(savedRole -> ResponseEntity.created(URI.create("/roles/" + savedRole.getId()))
				.contentType(MediaType.APPLICATION_JSON).build());
	}
	
	@DeleteMapping("/role/{role}")
	public Mono<ResponseEntity<Void>> deleteRole(@PathVariable String id) {
		return roleRepository.findById(id)
				.flatMap(existingRole -> roleRepository.delete(existingRole).then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
				.switchIfEmpty(Mono.error(new RoleNotFoundException("No Role Found In DB with given id: " + id +" !!!")));
	}
}
