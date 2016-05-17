package com.pipai.wf.save;

public class CorruptedSaveException extends Exception {

	private static final long serialVersionUID = 1L;

	public CorruptedSaveException(String message) {
		super(message);
	}

	public CorruptedSaveException(String message, Throwable cause) {
		super(message, cause);
	}

}
