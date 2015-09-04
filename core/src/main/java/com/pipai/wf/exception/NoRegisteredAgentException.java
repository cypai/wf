package com.pipai.wf.exception;

public class NoRegisteredAgentException extends Exception {

	private static final long serialVersionUID = 1L;

	public NoRegisteredAgentException() {
		super("Ability needs an Agent registered to call this function");
	}

}
