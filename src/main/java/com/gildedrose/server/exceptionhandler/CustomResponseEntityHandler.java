package com.gildedrose.server.exceptionhandler;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class CustomResponseEntityHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler(value = { BadRequestException.class })
	public ResponseEntity<Object> handleBadRequestException(Exception ex, WebRequest request) {
		ErrorResponse errorResponse = ErrorResponse.builder().date(new Date()).message(ex.getMessage())
				.details(request.getDescription(false)).build();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	@ExceptionHandler(value = { ItemNotFoundException.class, UsernameNotFoundException.class,
			OrderNotFoundException.class })
	public ResponseEntity<Object> handleItemNotFoundException(Exception ex, WebRequest request) {
		ErrorResponse errorResponse = ErrorResponse.builder().date(new Date()).message(ex.getMessage())
				.details(request.getDescription(false)).build();
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
	}

	@ExceptionHandler(ItemLowInStockException.class)
	public ResponseEntity<Object> handleItemLowInStockException(ItemLowInStockException ex, WebRequest request) {
		ErrorResponse errorResponse = ErrorResponse.builder().date(new Date()).message(ex.getMessage())
				.details(request.getDescription(false)).build();
		return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
	}

}
