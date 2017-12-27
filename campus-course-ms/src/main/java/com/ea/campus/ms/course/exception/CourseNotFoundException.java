package com.ea.campus.ms.course.exception;

public class CourseNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 4190822390529981452L;

	public CourseNotFoundException(String message){
		super(message);
	}
	
	public CourseNotFoundException(String message, Throwable e){
		super(message,e);
	}
}
