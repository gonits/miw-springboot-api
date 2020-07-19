package com.gildedrose.server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gildedrose.server.domain.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

	public Optional<Item> findByName(String name);

}
