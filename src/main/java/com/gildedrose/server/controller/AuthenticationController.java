package com.gildedrose.server.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gildedrose.server.repository.UserRepository;
import com.gildedrose.server.security.jwt.JwtTokenProvider;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

	@Autowired
	UserRepository userRepo;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtTokenProvider jwtTokenProvider;

	@PostMapping("/signIn")
	public ResponseEntity<?> signIn(@RequestBody UserLoginForm userLoginForm) {
		try {
			String username = userLoginForm.getUserName();
			authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(username, userLoginForm.getPassword()));
			Map<Object, Object> response = new HashMap<Object, Object>();
			response.put("username", username);
			response.put("token", jwtTokenProvider.createToken(username, this.userRepo.findByUserName(username)
					.orElseThrow(() -> new UsernameNotFoundException("User not found")).getRoles()));
			return ResponseEntity.ok().body(response);
		} catch (AuthenticationException ex) {
			throw new UsernameNotFoundException("User not found");
		}
	}

}
