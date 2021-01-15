package com.ea.campus.ms.course;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages="com.ea")
//@EnableEurekaClient  //removed Eureka por test purpose
//@EnableCircuitBreaker //removed Eureka por test purpose
public class CourseApp {

	public static void main(String[] args) {
		SpringApplication.run(CourseApp.class, args);
	}
}
