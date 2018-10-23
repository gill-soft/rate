package com.gillsoft.model;

public class RequestError {

	private Boolean error = true;

	private String message;

	public RequestError() {
	}

	public RequestError(String message) {
		this.message = message;
	}

	public Boolean getError() {
		return error;
	}

	public void setError(Boolean error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
