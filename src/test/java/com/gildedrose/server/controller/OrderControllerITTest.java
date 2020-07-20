package com.gildedrose.server.controller;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gildedrose.server.domain.Order;
import com.gildedrose.server.security.jwt.JwtTokenProvider;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;

/**
 * This class contains integration test cases for OrderController.
 * 
 * @author Nitika Goel
 *
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderControllerITTest {
	@LocalServerPort
	private int randomPort;
	TestRestTemplate restTemplate = new TestRestTemplate();
	private String token;
	@Autowired
	JwtTokenProvider jwtTokenProvider;

	@BeforeEach
	public void setup() {
		RestAssured.port = this.randomPort;
		token = given().contentType(ContentType.JSON)
				.body(UserLoginForm.builder().userName("user").password("password").build()).when().post("/auth/signIn")
				.andReturn().jsonPath().getString("token");
		log.debug("Got token:" + token);
	}

	@Test
	public void shouldPlaceOrderWhenauthorized() {
		String itemurl = "http://localhost:" + randomPort + "/items";
		ResponseEntity<List> listResponse = restTemplate.getForEntity(itemurl, List.class);
		Map<String, Object> listItemMap = (Map) listResponse.getBody().get(0);
		String id = listItemMap.get("id").toString();
		int quantity = (int) listItemMap.get("stock");
		if (id != null) {
			String orderUrl = "http://localhost:" + randomPort + "/orders";
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "Bearer " + token);
			HttpEntity<OrderForm> httpEntity = new HttpEntity<>(
					OrderForm.builder().id(Long.valueOf(id)).quantity(quantity).build(), headers);
			ResponseEntity<Order> response = restTemplate.exchange(orderUrl, HttpMethod.POST, httpEntity, Order.class);
			assertEquals(HttpStatus.CREATED, response.getStatusCode());
			assertEquals(jwtTokenProvider.getUsername(token), response.getBody().getUserName());

		}

	}

	@Test
	public void shouldgiveOrderDetailsWhenauthorized() {
		String itemurl = "http://localhost:" + randomPort + "/items";
		ResponseEntity<List> listResponse = restTemplate.getForEntity(itemurl, List.class);
		Map<String, Object> listItemMap = (Map) listResponse.getBody().get(1);
		String id = listItemMap.get("id").toString();
		int quantity = (int) listItemMap.get("stock");
		if (id != null) {
			String orderUrl = "http://localhost:" + randomPort + "/orders/";
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "Bearer " + token);
			HttpEntity<OrderForm> httpEntity = new HttpEntity<>(
					OrderForm.builder().id(Long.valueOf(id)).quantity(quantity).build(), headers);
			ResponseEntity<Order> orderResponse = restTemplate.exchange(orderUrl, HttpMethod.POST, httpEntity,
					Order.class);
			ResponseEntity<Order> orderDetailresponse = restTemplate
					.exchange(orderUrl + orderResponse.getBody().getOrderId(), HttpMethod.GET, httpEntity, Order.class);
			assertEquals(orderResponse.getBody().getOrderId(), orderDetailresponse.getBody().getOrderId());
			assertEquals(orderResponse.getBody().getUserName(), orderDetailresponse.getBody().getUserName());
		}

	}

	@Test
	public void shouldFailOrderWhenNotAuthorized() {
		String itemurl = "http://localhost:" + randomPort + "/items";
		ResponseEntity<List> listResponse = restTemplate.getForEntity(itemurl, List.class);
		Map<String, Object> listItemMap = (Map) listResponse.getBody().get(0);
		String id = listItemMap.get("id").toString();
		int quantity = (int) listItemMap.get("stock");
		if (id != null) {
			String orderUrl = "http://localhost:" + randomPort + "/orders";
			HttpEntity<OrderForm> httpEntity = new HttpEntity<>(
					OrderForm.builder().id(Long.valueOf(id)).quantity(quantity).build());
			ResponseEntity<Order> response = restTemplate.exchange(orderUrl, HttpMethod.POST, httpEntity, Order.class);
			assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
		}
	}

	@Test
	public void shouldApplySurgeWhenPlaceOrderWithSurgePrice() {
		String itemurl = "http://localhost:" + randomPort + "/items";
		List<String> prices = new ArrayList<String>();
		ResponseEntity<List> listResponse;
		Map<String, Object> listItemMap = new HashMap<>();
		for (int i = 0; i < 10; i++) {
			listResponse = restTemplate.getForEntity(itemurl, List.class);
			listItemMap = (Map) listResponse.getBody().get(0);
			prices.add(listItemMap.get("price").toString());
		}
		String orderUrl = "http://localhost:" + randomPort + "/orders";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);
		HttpEntity<OrderForm> httpEntity = new HttpEntity<>(
				OrderForm.builder().id(Long.valueOf(listItemMap.get("id").toString())).quantity(1).build(), headers);
		ResponseEntity<Order> response = restTemplate.exchange(orderUrl, HttpMethod.POST, httpEntity, Order.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(jwtTokenProvider.getUsername(token), response.getBody().getUserName());
		assertEquals(prices.get(9), String.valueOf(response.getBody().getItemPrice()));

	}

	@Test
	public void shouldFailWhenQuantityNeededIsGreaterThanAvailable() {
		String itemurl = "http://localhost:" + randomPort + "/items";
		ResponseEntity<List> listResponse = restTemplate.getForEntity(itemurl, List.class);
		Map<String, Object> listItemMap = (Map) listResponse.getBody().get(0);
		String id = listItemMap.get("id").toString();
		int quantity = (int) listItemMap.get("stock") + 10;
		if (id != null) {
			String orderUrl = "http://localhost:" + randomPort + "/orders";
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "Bearer " + token);
			HttpEntity<OrderForm> httpEntity = new HttpEntity<>(
					OrderForm.builder().id(Long.valueOf(id)).quantity(quantity).build(), headers);
			ResponseEntity<Order> response = restTemplate.exchange(orderUrl, HttpMethod.POST, httpEntity, Order.class);
			assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		}
	}

}
