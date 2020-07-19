package com.gildedrose.server.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order implements Serializable {

	private static final long serialVersionUID = 6262268476020102974L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long orderId;
	@Column(nullable = false)
	private Long itemId;
	@Column(nullable = false)
	private int itemPrice;
	@Column(nullable = false)
	private int quantity;
	@Column(nullable = false)
	private String userName;

}
