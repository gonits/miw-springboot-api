package com.gildedrose.server.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ItemNotFoundException extends Exception {

	private static final long serialVersionUID = -2797930027016971615L;

	public ItemNotFoundException(String message) {
		super(message);
	}
}
