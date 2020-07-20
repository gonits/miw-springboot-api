package com.gildedrose.server.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * This class contains integration test cases for AuthenticationController.
 * 
 * @author Nitika Goel
 *
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationControllerITTest {
	@LocalServerPort
	private int randomPort;
	TestRestTemplate restTemplate = new TestRestTemplate();

	@Test
	public void shouldReturnTokenWhenCorrectDetailsArePassed() {
		String url = "http://localhost:" + randomPort + "/auth/signIn";
		HttpEntity<UserLoginForm> httpEntity = new HttpEntity<>(
				UserLoginForm.builder().userName("user").password("password").build());
		ResponseEntity<HashMap> authResponse = restTemplate.exchange(url, HttpMethod.POST, httpEntity, HashMap.class);
		assertEquals(HttpStatus.OK, authResponse.getStatusCode());
		assertNotNull(authResponse.getBody().get("token"));
		assertEquals("user", authResponse.getBody().get("userName"));

	}

	@Test
	public void shouldReturnUnAuthorizedWhenInCorrectDetailsArePassed() {
		String url = "http://localhost:" + randomPort + "/auth/signIn";
		HttpEntity<UserLoginForm> httpEntity = new HttpEntity<>(
				UserLoginForm.builder().userName("user1").password("password").build());
		ResponseEntity<HashMap> authResponse = restTemplate.exchange(url, HttpMethod.POST, httpEntity, HashMap.class);
		assertEquals(HttpStatus.UNAUTHORIZED, authResponse.getStatusCode());

	}

}
