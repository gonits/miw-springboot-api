package com.gildedrose.server.controller;

import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
 * This is rest api to place order for individual item. It accepts an order form
 * which contains Item id and quantity needed for that item. This api call is
 * authenticated. Caller must send authorization header with value of auth
 * token. If auth token is not present, the api call will fail giving 401
 * unauthorized error.
 * 
 * @author Nitika Goel
 *
 */
@RestController
@RequestMapping("orders")
@CrossOrigin
public class OrderController {
	@Autowired
	ItemRepository itemRepo;

	@Autowired
	OrderRepository orderRepo;

	@Autowired
	SurgePrice surgePriceImpl;

	@PostMapping("")
	public ResponseEntity<Order> buy(@Valid @RequestBody OrderForm orderForm, @AuthenticationPrincipal User user)
			throws ItemNotFoundException, ItemLowInStockException, BadRequestException, URISyntaxException {
		Item requestedItem = itemRepo.findById(orderForm.getId())
				.orElseThrow(() -> new ItemNotFoundException("Request Item Id does not exist"));

		if (orderForm.getQuantity() <= 0) {
			throw new BadRequestException("Quantity should be positive value greater than zero");
		}
		if ((requestedItem.getStock() - orderForm.getQuantity()) < 0) {
			throw new ItemLowInStockException(
					"Order can not be placed with the quantity requested.Available quantity is:"
							+ requestedItem.getStock());
		}
		updateItemStock(orderForm, requestedItem);
		Order order = createOrder(orderForm, user, requestedItem);
		URI location = new URI("/orders/id");
		return ResponseEntity.created(location).body(order);
	}

	@GetMapping("{id}")
	public ResponseEntity<Order> order(@PathVariable Long id) throws OrderNotFoundException {
		return ResponseEntity.ok().body(orderRepo.findById(id)
				.orElseThrow(() -> new OrderNotFoundException("Request Order Id does not exist")));
	}

	private Order createOrder(OrderForm orderForm, User user, Item requestedItem) {
		Order newOrder = Order.builder().itemId(requestedItem.getId())
				.itemPrice(surgePriceImpl.getSurgePriceForItem(requestedItem, false)).userName(user.getUsername())
				.quantity(orderForm.getQuantity()).build();
		orderRepo.save(newOrder);
		return newOrder;
	}

	private void updateItemStock(OrderForm orderForm, Item requestedItem) {
		requestedItem.setStock(requestedItem.getStock() - orderForm.getQuantity());
		itemRepo.save(requestedItem);
	}

}
