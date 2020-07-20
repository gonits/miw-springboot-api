package com.gildedrose.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gildedrose.server.repository.UserRepository;

/**
 * UserDetailsService interface has been implemented here to fetch user details
 * from user repository. This is needed when validating auth token and setting
 * spring security context with Authentication principal.
 * 
 * @author Nitika Goel
 *
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
	@Autowired
	UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return this.userRepo.findByUserName(username)
				.orElseThrow(() -> new UsernameNotFoundException("Username not found"));
	}

}
