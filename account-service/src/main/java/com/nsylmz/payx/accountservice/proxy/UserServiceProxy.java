package com.nsylmz.payx.accountservice.proxy;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="netflix-zuul-api-gateway-server")
@RibbonClient(name="user-service") 
public interface UserServiceProxy {
	
	@GetMapping("/user-serivce/user/checkUser/{userNumber}")
	public Boolean checkUser(@PathVariable("userNumber") long userNumber);

}
