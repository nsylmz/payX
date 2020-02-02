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

import com.nsylmz.payx.customer.dto.CredentialsQueryDTO;
import com.nsylmz.payx.customer.exception.CustomerNotFoundException;
import com.nsylmz.payx.customer.model.Customer;
import com.nsylmz.payx.customer.repository.CustomerRepository;
import com.nsylmz.payx.customer.service.SequenceGenerator;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class CustomerResource {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private SequenceGenerator sequenceGenerator;
	
	@PostMapping("/signUp")
	private Mono<ResponseEntity<Void>> signUp(@Valid @RequestBody Customer customer) {
		customer.setCustomerNumber(sequenceGenerator.generateAccountNumberSequence(Customer.SEQUENCE_NAME));
		return customerRepository.save(customer).map(savedCustomer -> ResponseEntity.created(URI.create("/customer/" + savedCustomer.getCustomerNumber()))
				.contentType(MediaType.APPLICATION_JSON).build());
	}
	
	@GetMapping("/customers")
	private Flux<Customer> getAllCustomers() {
		return customerRepository.findAll().map(existingCustomer -> existingCustomer)
				.switchIfEmpty(Mono.error(new CustomerNotFoundException("No Customer Found In DB!!!")));
	}
	
	@GetMapping("/customer/checkCustomer/{customerNumber}")
	private Mono<Boolean> checkCustomer(@PathVariable long customerNumber) {
		return customerRepository.findOne(Example.of(new Customer(null, customerNumber, null, null, null, null, null, null, null, null)))
				.map(existingCustomer -> true)
				.switchIfEmpty(Mono.just(false));
	}
	
	@GetMapping("/customer/{customerNumber}")
	private Mono<ResponseEntity<Customer>> getCustomer(@PathVariable Long customerNumber) {
		return customerRepository.findOne(Example.of(new Customer(null, customerNumber, null, null, null, null, null, null, null, null)))
				.map(existingCustomer -> ResponseEntity.ok(existingCustomer))
				.switchIfEmpty(Mono.error(new CustomerNotFoundException("customerNumber: " + customerNumber + " is not found!!!")));
	}
	
	@PostMapping("/signIn")
	private Mono<ResponseEntity<Object>> signIn(@Valid @RequestBody CredentialsQueryDTO credentials) {
		return customerRepository.findOne(Example.of(new Customer(null, null, null, null, credentials.getEmail(), null, null, null, null, null)))
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
