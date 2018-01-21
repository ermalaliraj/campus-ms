package com.ea.campus.ms.student;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages="com.ea") 
@EnableEurekaClient
//@EnableCircuitBreaker
public class StudentApp {

	public static void main(String[] args) {
		SpringApplication.run(StudentApp.class, args);
	}
}
