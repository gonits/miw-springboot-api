package com.gildedrose.server.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gildedrose.server.domain.Item;

/**
 * This class contains integration test cases for ItemController.
 * 
 * @author Nitika Goel
 *
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ItemControllerITTest {
	@LocalServerPort
	private int randomPort;
	TestRestTemplate restTemplate = new TestRestTemplate();

	@Test
	public void shouldReturnItemsListWithQuanityGreaterThanZero() {
		String url = "http://localhost:" + randomPort + "/items";
		ResponseEntity<List> listResponse = restTemplate.getForEntity(url, List.class);
		Map<String, Object> listItemMap = (Map) listResponse.getBody().get(0);
		assertNotNull(listItemMap.get("id"));
		assertEquals(HttpStatus.OK, listResponse.getStatusCode());
	}

	@Test
	public void shouldReturnItemWithIdPassedInRequest() {
		String url = "http://localhost:" + randomPort + "/items/";
		ResponseEntity<List> listResponse = restTemplate.getForEntity(url, List.class);
		Map<String, Object> listItemMap = (Map) listResponse.getBody().get(0);
		ResponseEntity<Item> itemResponse = restTemplate.getForEntity(url + listItemMap.get("id"), Item.class);
		assertEquals(HttpStatus.OK, itemResponse.getStatusCode());
		assertEquals(listItemMap.get("id").toString(), Long.toString(itemResponse.getBody().getId()));
	}

	@Test
	public void shouldThrowNotFoundExceptionWhenItemNotFound() {
		String url = "http://localhost:" + randomPort + "/items/";
		ResponseEntity<Item> itemResponse = restTemplate.getForEntity(url + 100L, Item.class);
		assertEquals(HttpStatus.NOT_FOUND, itemResponse.getStatusCode());
	}

	@Test
	public void shouldApplySurgeWhenHitTenTimes() {
		String itemurl = "http://localhost:" + randomPort + "/items";
		List<String> prices = new ArrayList<String>();
		ResponseEntity<List> listResponse;
		Map<String, Object> listItemMap;
		for (int i = 0; i < 10; i++) {
			listResponse = restTemplate.getForEntity(itemurl, List.class);
			listItemMap = (Map) listResponse.getBody().get(0);
			prices.add(listItemMap.get("price").toString());
		}
		assertEquals(prices.get(0), prices.get(1));
		assertNotEquals(prices.get(0), prices.get(9));

	}

}
