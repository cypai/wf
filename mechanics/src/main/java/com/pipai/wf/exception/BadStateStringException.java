package com.pipai.wf.exception;

public class BadStateStringException extends Exception {

	private static final long serialVersionUID = 1L;

	public BadStateStringException(String message) {
		super(message);
	}

	public BadStateStringException(String message, Throwable cause) {
		super(message, cause);
	}

}
