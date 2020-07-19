package com.gildedrose.server.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OrderNotFoundException extends Exception {

	private static final long serialVersionUID = -6476660788051729668L;

	public OrderNotFoundException(String message) {
		super(message);
	}

}
