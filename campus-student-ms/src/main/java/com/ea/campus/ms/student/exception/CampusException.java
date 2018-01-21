package com.ea.campus.ms.student.exception;

public class CampusException extends RuntimeException{

	private static final long serialVersionUID = -8324923600114758946L;

	public CampusException(String message){
		super(message);
	}
	
	public CampusException(String message, Throwable e){
		super(message,e);
	}
}
