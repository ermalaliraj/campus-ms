package com.ea.campus.ms.student;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages="com.ea") 
public class StudentApp {

	public static void main(String[] args) {
		SpringApplication.run(StudentApp.class, args);
	}
}
