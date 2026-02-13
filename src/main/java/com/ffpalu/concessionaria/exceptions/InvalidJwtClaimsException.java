package com.ffpalu.concessionaria.exceptions;

public class InvalidJwtClaimsException extends RuntimeException {
	public InvalidJwtClaimsException(String message) {
		super(message);
	}
}
