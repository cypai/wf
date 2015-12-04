package com.pipai.wf.exception;

public class NoRegisteredAgentException extends Exception {

	private static final long serialVersionUID = 1L;

	public NoRegisteredAgentException() {
		super("Ability needs an Agent registered to call this function");
	}

	public NoRegisteredAgentException(String message) {
		super(message);
	}

	public NoRegisteredAgentException(String message, Throwable cause) {
		super(message, cause);
	}

}
