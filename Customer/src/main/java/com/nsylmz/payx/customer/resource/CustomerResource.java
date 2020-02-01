package com.nsylmz.payx.customer.resource;

import java.net.URI;

import javax.validation.Valid;

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

import com.nsylmz.payx.customer.exception.CustomerNotFoundException;
import com.nsylmz.payx.customer.model.Credentials;
import com.nsylmz.payx.customer.model.Customer;
import com.nsylmz.payx.customer.repository.CustomerRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class CustomerResource {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@PostMapping("/signUp")
	private Mono<ResponseEntity<Void>> signUp(@Valid @RequestBody Customer customer) {
		return customerRepository.save(customer).map(savedCustomer -> ResponseEntity.created(URI.create("/customer/" + savedCustomer.getId()))
				.contentType(MediaType.APPLICATION_JSON).build());
	}
	
	@GetMapping("/customers")
	private Flux<Customer> getAllCustomers() {
		return customerRepository.findAll().map(existingCustomer -> existingCustomer)
				.switchIfEmpty(Mono.error(new CustomerNotFoundException("No Customer Found In DB!!!")));
	}
	
	@GetMapping("/customer/checkCustomer/{customerId}")
	private Mono<Boolean> checkCustomer(@PathVariable String customerId) {
		return customerRepository.findById(customerId).map(existingCustomer -> true)
				.switchIfEmpty(Mono.just(false));
	}
	
	@GetMapping("/customer/{customerId}")
	private Mono<ResponseEntity<Customer>> getCustomer(@PathVariable String customerId) {
		return customerRepository.findById(customerId).map(existingCustomer -> ResponseEntity.ok(existingCustomer))
				.switchIfEmpty(Mono.error(new CustomerNotFoundException("customerId: " + customerId + " is not found!!!")));
	}
	
	@PostMapping("/signIn")
	private Mono<ResponseEntity<Object>> signIn(@Valid @RequestBody Credentials credentials) {
		return customerRepository.findOne(Example.of(new Customer(null, null, null, credentials.getEmail(), null, null, null, null, null)))
				.flatMap(existingCustomer -> {
					if (existingCustomer == null) {
						return Mono.error(new CustomerNotFoundException("No Customer matched with email address: " + credentials.getEmail() + " !!!"));
					} else if (existingCustomer.getPassword().equals(credentials.getPassword())) {
						return Mono.just(ResponseEntity.ok().build());
					} else {
						return Mono.just(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
					}
				}).switchIfEmpty(Mono.error(new CustomerNotFoundException("No Customer Found In DB with email address : " + credentials.getEmail() + " !!!")));
	}

}
