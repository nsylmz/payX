package com.nsylmz.payx.account.proxy;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="customer")
@RibbonClient(name="customer") 
public interface CustomerServiceProxy {
	
	@GetMapping("/customer/checkCustomer/{customerNumber}")
	public Boolean checkCustomer(@PathVariable("customerNumber") long customerNumber);

}
