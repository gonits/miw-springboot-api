package com.gildedrose.server.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.gildedrose.server.domain.Item;
import com.gildedrose.server.domain.SurgePrice;
import com.gildedrose.server.exceptionhandler.ItemNotFoundException;
import com.gildedrose.server.repository.ItemRepository;

/**
 * This class contains Unit test cases for ItemController.
 * 
 * @author Nitika Goel
 *
 */
public class ItemControllerTest {
	@Mock
	ItemRepository itemRepo;
	@Mock
	SurgePrice surgePriceImpl;

	@InjectMocks
	ItemController controller;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldReturnAllItemsWithQuantityGreaterThanZero() {
		Item item1 = Item.builder().name("Fitness-Tracker-Band").price(100).stock(20)
				.description("Fitness tracker,24 hour continuous Heart rate and Blood pressure monitor").id(1L).build();
		Item item2 = Item.builder().name("Cycling-Gloves").price(50).stock(30)
				.description("Inner dazzle fabric ensures breathability and pleasantness to the hands").id(2L).build();
		Item item3 = Item.builder().name("Gatorade Bottle").price(80).stock(0)
				.description("Fuel your game with Gatorade’s new customizable hydration platform").id(3L).build();
		List<Item> items = Arrays.asList(item1, item2, item3);
		when(itemRepo.findAll()).thenReturn(items);
		ResponseEntity<List> response = controller.allItems();
		assertEquals(2, response.getBody().size());

	}

	@Test
	public void shouldReturnItemWithCorrectId() throws ItemNotFoundException {
		Item item1 = Item.builder().name("Fitness-Tracker-Band").price(100).stock(20)
				.description("Fitness tracker,24 hour continuous Heart rate and Blood pressure monitor").id(1L).build();
		when(itemRepo.findById(1L)).thenReturn(Optional.of(item1));
		ResponseEntity<Item> response = controller.item(1L);
		assertEquals(1L, response.getBody().getId());

	}

	@Test
	public void ShouldReturnItemsForSurgePrice() throws ItemNotFoundException {
		Item item1 = Item.builder().name("Fitness-Tracker-Band").price(100).stock(20)
				.description("Fitness tracker,24 hour continuous Heart rate and Blood pressure monitor").id(1L).build();
		Item item2 = Item.builder().name("Fitness-Tracker-Band").price(50).stock(20)
				.description("Fitness tracker,24 hour continuous Heart rate and Blood pressure monitor").id(1L).build();
		List<Item> items = Arrays.asList(item1, item2);
		when(itemRepo.findAll()).thenReturn(items);
		when(surgePriceImpl.getSurgePriceForItem(item1, true)).thenReturn(110);
		when(surgePriceImpl.getSurgePriceForItem(item2, true)).thenReturn(55);
		ResponseEntity<List> response = controller.allItems();
		Item itemresponse1 = (Item) response.getBody().get(0);
		Item itemresponse2 = (Item) response.getBody().get(1);
		assertEquals(110, itemresponse1.getPrice());
		assertEquals(55, itemresponse2.getPrice());

	}

}
