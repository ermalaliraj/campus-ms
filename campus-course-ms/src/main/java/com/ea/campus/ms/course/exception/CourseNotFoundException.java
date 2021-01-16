package com.ea.campus.ms.course.exception;

public class CourseNotFoundException extends RuntimeException{

	public CourseNotFoundException(String message){
		super(message);
	}
	
	public CourseNotFoundException(String message, Throwable e){
		super(message,e);
	}
}
