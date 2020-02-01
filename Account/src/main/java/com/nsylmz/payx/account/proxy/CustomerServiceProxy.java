package com.nsylmz.payx.account.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="customer", url="localhost:8090")
public interface CustomerServiceProxy {
	
	@GetMapping("/customer/checkCustomer/{customerId}")
	public Boolean checkCustomer(@PathVariable("customerId") String customerId);

}
