package com.ea.campus.ms.course;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages="com.ea") 
public class CourseApp {

	public static void main(String[] args) {
		SpringApplication.run(CourseApp.class, args);
	}
}
