package com.gildedrose.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gildedrose.server.domain.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
