package com.gildedrose.server.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.gildedrose.server.domain.User;
import com.gildedrose.server.repository.UserRepository;
import com.gildedrose.server.security.jwt.JwtTokenProvider;

/**
 * This class contains Unit test cases for AuthenticationController.
 * 
 * @author Nitika Goel
 *
 */
public class AuthenticationControllerTest {
	@Mock
	UserRepository userRepo;

	@Mock
	AuthenticationManager authenticationManager;

	@Mock
	JwtTokenProvider jwtTokenProvider;

	@InjectMocks
	AuthenticationController controller;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldReturnTokenForCorrectUserDetails() {
		UserLoginForm form = UserLoginForm.builder().userName("Nitika").password("Password").build();
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				form.getUserName(), form.getPassword());
		Authentication authentication = mock(Authentication.class);
		List<String> roles = new ArrayList<>();
		roles.add("ROLE_USER");
		User user = User.builder().userName("Nitika").password("password").roles(roles).build();
		when(authenticationManager.authenticate(usernamePasswordAuthenticationToken)).thenReturn(authentication);
		when(userRepo.findByUserName(form.getUserName())).thenReturn(Optional.of(user));
		String token = "##########";
		when(jwtTokenProvider.createToken(form.getUserName(), roles)).thenReturn(token);
		ResponseEntity<Map> response = controller.signIn(form);
		assertEquals(token, response.getBody().get("token"));
		assertEquals(form.getUserName(), response.getBody().get("userName"));

	}

	@Test
	public void ShouldThrowExceptionWhenWrongUserNameIsPassed() {
		UserLoginForm form = UserLoginForm.builder().userName("Nitika").password("Password").build();
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				form.getUserName(), form.getPassword());
		Authentication authentication = mock(Authentication.class);
		when(authenticationManager.authenticate(usernamePasswordAuthenticationToken)).thenReturn(authentication);
		when(userRepo.findByUserName(form.getUserName())).thenThrow(new UsernameNotFoundException("User not found"));
		Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
			controller.signIn(form);
		});
		String expectedMessage = "User not found";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));

	}

}
