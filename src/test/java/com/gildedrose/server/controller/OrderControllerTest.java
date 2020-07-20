package com.gildedrose.server.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.net.URISyntaxException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.gildedrose.server.domain.Item;
import com.gildedrose.server.domain.Order;
import com.gildedrose.server.domain.SurgePrice;
import com.gildedrose.server.domain.User;
import com.gildedrose.server.exceptionhandler.BadRequestException;
import com.gildedrose.server.exceptionhandler.ItemLowInStockException;
import com.gildedrose.server.exceptionhandler.ItemNotFoundException;
import com.gildedrose.server.exceptionhandler.OrderNotFoundException;
import com.gildedrose.server.repository.ItemRepository;
import com.gildedrose.server.repository.OrderRepository;

/**
 * This class contains Unit test cases for OrderController.
 * 
 * @author Nitika Goel
 *
 */
public class OrderControllerTest {
	@Mock
	ItemRepository itemRepo;
	@Mock
	OrderRepository orderRepo;
	@Mock
	SurgePrice surgePriceImpl;

	@InjectMocks
	OrderController controller;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldPlaceOrderAndReturnOrderCreated()
			throws ItemNotFoundException, ItemLowInStockException, BadRequestException, URISyntaxException {
		Item item1 = Item.builder().name("Fitness-Tracker-Band").price(100).stock(20)
				.description("Fitness tracker,24 hour continuous Heart rate and Blood pressure monitor").id(1L).build();
		User user = User.builder().userName("Nitika").password("password").build();
		Order newOrder = Order.builder().itemId(item1.getId()).orderId(100L)
				.itemPrice(surgePriceImpl.getSurgePriceForItem(item1, false)).userName(user.getUsername()).quantity(1)
				.build();
		when(itemRepo.findById(1L)).thenReturn(Optional.of(item1));
		item1.setPrice(99);
		when(itemRepo.save(item1)).thenReturn(item1);
		when(orderRepo.save(newOrder)).thenReturn(newOrder);
		OrderForm form = new OrderForm(1L, 1);
		ResponseEntity<Order> response = controller.buy(form, user);
		assertEquals(201, response.getStatusCodeValue());

	}

	@Test
	public void shouldReturnOrderForOrderId() throws ItemNotFoundException, ItemLowInStockException,
			BadRequestException, URISyntaxException, OrderNotFoundException {
		Item item1 = Item.builder().name("Fitness-Tracker-Band").price(100).stock(20)
				.description("Fitness tracker,24 hour continuous Heart rate and Blood pressure monitor").id(1L).build();

		User user = User.builder().userName("Nitika").password("password").build();
		Order newOrder = Order.builder().itemId(item1.getId())
				.itemPrice(surgePriceImpl.getSurgePriceForItem(item1, false)).userName(user.getUsername()).quantity(1)
				.orderId(10L).build();
		when(orderRepo.findById(10L)).thenReturn(Optional.of(newOrder));
		ResponseEntity<Order> response = controller.order(10L);
		assertEquals(10L, response.getBody().getOrderId());

	}

}
