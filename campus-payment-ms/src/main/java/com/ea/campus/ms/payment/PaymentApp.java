package com.ea.campus.ms.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.hateoas.config.EnableHypermediaSupport;

@SpringBootApplication
@ComponentScan(basePackages="com.ea") 
@EnableEurekaClient
@EnableHypermediaSupport(type=EnableHypermediaSupport.HypermediaType.HAL)
public class PaymentApp {

	public static void main(String[] args) {
		SpringApplication.run(PaymentApp.class, args);
	}
}
