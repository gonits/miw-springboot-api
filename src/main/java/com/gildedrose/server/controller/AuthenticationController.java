package com.gildedrose.server.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gildedrose.server.repository.UserRepository;
import com.gildedrose.server.security.jwt.JwtTokenProvider;

/**
 * This is rest api to generate authentication token for authorized users. It
 * accepts username and password in request body and creats auth token if user
 * gets authenticated.
 * 
 * @author Nitika Goel
 *
 */
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
	public ResponseEntity<Map> signIn(@RequestBody UserLoginForm userLoginForm) {
		String username = userLoginForm.getUserName();
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(username, userLoginForm.getPassword()));
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("userName", username);
		response.put("token", jwtTokenProvider.createToken(username, this.userRepo.findByUserName(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found")).getRoles()));
		return ResponseEntity.ok().body(response);
	}

}
