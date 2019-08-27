package com.app.exception;

public class ValidationError extends RuntimeException{

	public ValidationError(String s) {
		super(s);
	}
}
