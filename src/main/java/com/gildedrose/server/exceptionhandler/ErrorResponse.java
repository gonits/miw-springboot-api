package com.gildedrose.server.exceptionhandler;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse implements Serializable {

	private static final long serialVersionUID = 1808420181173918065L;

	private Date date;

	private String message;

	private String details;
}
