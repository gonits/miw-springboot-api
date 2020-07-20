package com.gildedrose.server.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import lombok.extern.slf4j.Slf4j;

/**
 * AuthenticationEntryPoint interface has been provided custom implementation to
 * send custom message in authentication failures and also log the same on
 * server side.
 * 
 * @author Nitika Goel
 *
 */
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		log.debug("Authentication Error", authException.getMessage());
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
				"Authorization header value is either Invalid or expired or missing");

	}

}
