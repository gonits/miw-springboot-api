package com.gildedrose.server.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gildedrose.server.domain.Item;
import com.gildedrose.server.domain.SurgePrice;
import com.gildedrose.server.exceptionhandler.ItemNotFoundException;
import com.gildedrose.server.repository.ItemRepository;

/**
 * This is rest api to show items list and details of individual items. This api
 * gives name, id, price, stock, description for each items whose stock is
 * greater than zero.
 * 
 * @author Nitika Goel
 *
 */
@RestController
@RequestMapping("items")
@CrossOrigin
public class ItemController {

	@Autowired
	ItemRepository itemRepo;

	@Autowired
	SurgePrice surgePriceImpl;

	@GetMapping("")
	public ResponseEntity<List> allItems() {
		return ResponseEntity
				.ok().body(
						itemRepo.findAll().stream().filter(item -> item.getStock() > 0)
								.map(item -> new Item(item.getId(), item.getName(), item.getDescription(),
										item.getStock(), surgePriceImpl.getSurgePriceForItem(item, true)))
								.collect(Collectors.toList()));
	}

	@GetMapping("{id}")
	public ResponseEntity<Item> item(@PathVariable Long id) throws ItemNotFoundException {
		return ResponseEntity.ok().body(
				itemRepo.findById(id).orElseThrow(() -> new ItemNotFoundException("Request Item Id does not exist")));
	}

}
