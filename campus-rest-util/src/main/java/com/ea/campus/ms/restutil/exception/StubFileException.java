package com.ea.campus.ms.restutil.exception;

public class StubFileException extends Exception {

	private static final long serialVersionUID = -2948111695215922955L;

	public StubFileException(String message) {
		super(message);
	}

	public StubFileException(String message, Throwable e) {
		super(message, e);
	}
}
