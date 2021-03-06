package com.gildedrose.server.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

/**
 * This filter will intercept calls to check Authorization header and will
 * validate auth token. If Auth token is invalid authentication exception is
 * thrown else authentication principal is set in spring security context for
 * the authenticated user.
 * 
 * @author Nitika Goel
 *
 */
@Component
public class JwtAuthenticationFilter extends GenericFilterBean {
	@Autowired
	JwtTokenProvider tokenProvider;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String token = tokenProvider.resolveTokenFromHeader((HttpServletRequest) request);
		if (null != token && tokenProvider.validateTokenExpiry(token)) {
			Authentication authentication = tokenProvider.getAuthentication(token);
			if (null != authentication) {
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		chain.doFilter(request, response);

	}

}
