package com.ea.campus.ms.course.exception;

public class TopicAssociatedToCourseException extends RuntimeException{

	public TopicAssociatedToCourseException(String message){
		super(message);
	}
	
	public TopicAssociatedToCourseException(String message, Throwable e){
		super(message,e);
	}
}
