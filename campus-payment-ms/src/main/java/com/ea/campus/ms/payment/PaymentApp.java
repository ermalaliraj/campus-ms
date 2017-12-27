package com.ea.campus.ms.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages="com.ea") 
public class PaymentApp {

	public static void main(String[] args) {
		SpringApplication.run(PaymentApp.class, args);
	}
}
