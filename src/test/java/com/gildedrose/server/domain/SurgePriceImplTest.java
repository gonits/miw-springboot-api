package com.gildedrose.server.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * This class contains Unit test cases for SurgePriceImpl.
 * 
 * @author Nitika Goel
 *
 */
@ExtendWith(MockitoExtension.class)
public class SurgePriceImplTest {

	@Test
	public void shouldApplySurgeAfterThreeItemViews() {
		SurgePriceImpl surgePrice = new SurgePriceImpl(Duration.ofSeconds(100), 3, 0.1);
		Item item = Item.builder().name("test").description("test").price(10).stock(10).id(1L).build();
		int price1 = surgePrice.getSurgePriceForItem(item, true);
		assertEquals(item.getPrice(), price1);
		int price2 = surgePrice.getSurgePriceForItem(item, true);
		assertEquals(item.getPrice(), price2);
		int price3 = surgePrice.getSurgePriceForItem(item, true);
		assertEquals(item.getPrice() * (1 + 0.1), price3);

	}

	@Test
	public void shouldNotApplySurgeAfterSurgeWindowIsOver() throws InterruptedException {
		SurgePriceImpl surgePrice = new SurgePriceImpl(Duration.ofSeconds(3), 3, 0.1);
		Item item = Item.builder().name("test").description("test").price(10).stock(10).id(1L).build();
		int price1 = surgePrice.getSurgePriceForItem(item, true);
		assertEquals(item.getPrice(), price1);
		int price2 = surgePrice.getSurgePriceForItem(item, true);
		assertEquals(item.getPrice(), price2);
		Thread.sleep(3000);
		int price3 = surgePrice.getSurgePriceForItem(item, true);
		assertNotEquals(item.getPrice() * (1 + 0.1), price3);

	}

	@Test
	public void shouldReturnSurgePriceWhenPlacingOrder() throws InterruptedException {
		SurgePriceImpl surgePrice = new SurgePriceImpl(Duration.ofSeconds(3), 3, 0.1);
		Item item = Item.builder().name("test").description("test").price(10).stock(10).id(1L).build();
		int price1 = surgePrice.getSurgePriceForItem(item, true);
		assertEquals(item.getPrice(), price1);
		int price2 = surgePrice.getSurgePriceForItem(item, true);
		assertEquals(item.getPrice(), price2);
		int price3 = surgePrice.getSurgePriceForItem(item, true);
		surgePrice.getSurgePriceForItem(item, false);
		assertEquals(item.getPrice() * (1 + 0.1), price3);

	}

}
