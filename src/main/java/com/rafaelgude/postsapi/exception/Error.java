package com.rafaelgude.postsapi.exception;

import org.springframework.http.HttpStatus;

public class Error {

	private int error;
	private String message;
	
	public Error(HttpStatus status, String message) {
		super();
		this.error = status.value();
		this.message = message;
	}

	public int getError() {
		return error;
	}

	public void setError(int error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
