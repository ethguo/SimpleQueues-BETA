package com.github.ethg242.simplequeues.misc;

public class LocationFormatException extends IllegalArgumentException {
	private static final long serialVersionUID = 1L;
	public String message;
	
	public LocationFormatException(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return message;
	}
}
