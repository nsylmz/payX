package com.nsylmz.payx.netflixzuulapigatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients("com.nsylmz.payx.netflixzuulapigatewayserver")
public class NetflixZuulApiGatewayServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(NetflixZuulApiGatewayServerApplication.class, args);
	}
	
	@Bean
	public LocalValidatorFactoryBean validator() {
	    return new LocalValidatorFactoryBean();
	}

}
