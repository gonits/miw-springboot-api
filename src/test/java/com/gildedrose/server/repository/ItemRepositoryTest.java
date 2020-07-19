package com.gildedrose.server.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gildedrose.server.domain.Item;

public class ItemRepositoryTest {
	@Autowired
	ItemRepository itemRepo;

	@Test
	public void findByItemNameTest() {
		Item saved = this.itemRepo.save(Item.builder().name("test").description("test").price(10).stock(10).build());
		Item i = this.itemRepo.getOne(saved.getId());
		assertThat(i.getName()).isEqualTo("test");
		assertThat(i.getId()).isNotNull();
		assertThat(i.getDescription()).isEqualTo("test");
		assertThat(i.getPrice()).isEqualTo(10);
		assertThat(i.getStock()).isEqualTo(10);
	}

}
