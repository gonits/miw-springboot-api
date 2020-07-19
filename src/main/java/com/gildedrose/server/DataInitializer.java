package com.gildedrose.server;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.gildedrose.server.domain.Item;
import com.gildedrose.server.domain.User;
import com.gildedrose.server.repository.ItemRepository;
import com.gildedrose.server.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {

	@Autowired
	ItemRepository items;
	@Autowired
	UserRepository userRepo;
	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
		log.debug("Initializing Items test data");
		Item item1 = Item.builder().name("Fitness-Tracker-Band").price(100).stock(20)
				.description("Fitness tracker,24 hour continuous Heart rate and Blood pressure monitor").build();
		Item item2 = Item.builder().name("Cycling-Gloves").price(50).stock(30)
				.description("Inner dazzle fabric ensures breathability and pleasantness to the hands").build();
		Item item3 = Item.builder().name("Gatorade Bottle").price(80).stock(0)
				.description("Fuel your game with Gatorade’s new customizable hydration platform").build();
		Arrays.asList(item1, item2, item3).forEach(item -> this.items.save(item));
		this.items.findAll().forEach(item -> log.debug(item.toString()));

		log.debug("Initializing Users test data");
		User user1 = User.builder().userName("Ryan Snow").password(this.passwordEncoder.encode("ryansnow"))
				.roles(Arrays.asList("ROLES_USER")).build();
		User user2 = User.builder().userName("John Snow").password(this.passwordEncoder.encode("johnsnow"))
				.roles(Arrays.asList("ROLES_USER")).build();
		User user3 = User.builder().userName("Arya Stark").password(this.passwordEncoder.encode("aryastark"))
				.roles(Arrays.asList("ROLES_USER", "ROLES_ADMIN")).build();
		User user4 = User.builder().userName("user").password(this.passwordEncoder.encode("password"))
				.roles(Arrays.asList("ROLE_USER")).build();
		User user5 = User.builder().userName("Nitika").password(this.passwordEncoder.encode("Password"))
				.roles(Arrays.asList("ROLE_USER")).build();
		Arrays.asList(user1, user2, user3, user4, user5).forEach(user -> this.userRepo.save(user));
		this.userRepo.findAll().forEach(user -> log.debug(user.toString()));

	}
}
