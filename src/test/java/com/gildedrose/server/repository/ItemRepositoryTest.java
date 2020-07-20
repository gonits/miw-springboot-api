package com.gildedrose.server.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gildedrose.server.domain.Item;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ItemRepositoryTest {
	@Autowired
	ItemRepository itemRepo;

	@Test
	public void ShouldfindByItemName() {
		Item saved = this.itemRepo.save(Item.builder().name("test").description("test").price(10).stock(10).build());
		Item i = this.itemRepo.findByName(saved.getName()).get();
		assertThat(i.getName()).isEqualTo("test");
		assertThat(i.getId()).isNotNull();
		assertThat(i.getDescription()).isEqualTo("test");
		assertThat(i.getPrice()).isEqualTo(10);
		assertThat(i.getStock()).isEqualTo(10);
	}

}
