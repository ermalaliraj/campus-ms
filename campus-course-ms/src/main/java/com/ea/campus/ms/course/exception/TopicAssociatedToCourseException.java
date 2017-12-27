package com.ea.campus.ms.course.exception;

public class TopicAssociatedToCourseException extends RuntimeException{

	private static final long serialVersionUID = -8324923600114758946L;

	public TopicAssociatedToCourseException(String message){
		super(message);
	}
	
	public TopicAssociatedToCourseException(String message, Throwable e){
		super(message,e);
	}
}
