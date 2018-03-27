package com.ea.campus.ms.student;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@ComponentScan(basePackages="com.ea") 
@EnableEurekaClient
@EnableCircuitBreaker
public class StudentApp extends WebMvcConfigurerAdapter {

	private static final Logger log = LoggerFactory.getLogger(StudentApp.class);
	
	public static void main(String[] args) {
		SpringApplication.run(StudentApp.class, args);
	}
	
	@Override
	public void addInterceptors(final InterceptorRegistry registry) {
		//registry.addInterceptor(jwtInterceptor()).addPathPatterns("/**");
//		registry.addInterceptor(jwtInterceptor());
		log.info("JWT Interceptor registered");
	}

//	@Bean
//	public JwtInterceptor jwtInterceptor() {
//		return new JwtInterceptor();
//	}

}
