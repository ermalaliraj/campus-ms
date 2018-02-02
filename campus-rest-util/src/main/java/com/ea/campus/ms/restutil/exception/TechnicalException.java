package com.ea.campus.ms.restutil.exception;

public class TechnicalException extends RuntimeException{

	private static final long serialVersionUID = -8324923600114758946L;

	public TechnicalException(String message){
		super(message);
	}
	
	public TechnicalException(String message, Throwable e){
		super(message,e);
	}
}
