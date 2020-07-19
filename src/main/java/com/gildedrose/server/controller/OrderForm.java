package com.gildedrose.server.controller;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderForm implements Serializable {

	private static final long serialVersionUID = -7660992837888946276L;

	private Long id;

	private int quantity;
}
