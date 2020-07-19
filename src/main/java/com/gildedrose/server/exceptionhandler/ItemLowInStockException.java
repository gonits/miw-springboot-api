package com.gildedrose.server.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ItemLowInStockException extends Exception {

	private static final long serialVersionUID = 3654599770117433734L;

	public ItemLowInStockException(String message) {
		super(message);
	}
}
