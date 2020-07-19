package com.gildedrose.server.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends Exception {

	private static final long serialVersionUID = 8413593137718084512L;

	public BadRequestException(String message) {
		super(message);
	}

}
