package com.gildedrose.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gildedrose.server.controller.OrderForm;
import com.gildedrose.server.repository.ItemRepository;
import com.gildedrose.server.repository.OrderRepository;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Slf4j
class ServerApplicationTests {

	private MockMvc mockMvc;

	@Autowired
	WebApplicationContext applicationContext;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	ItemRepository itemRepo;
	@Autowired
	OrderRepository orderRepo;

	@BeforeEach
	public void setup() {
		this.mockMvc = webAppContextSetup(this.applicationContext).apply(springSecurity()).build();
	}

	@Test
	public void getAllItems() throws Exception {
		MvcResult result = this.mockMvc.perform(get("/items").accept(MediaType.APPLICATION_JSON)).andReturn();
		MockHttpServletResponse response = result.getResponse();
		String content = response.getContentAsString();
		assertNotNull(content);
		assertEquals(200, result.getResponse().getStatus());
	}

	@Test
	public void getItemsWithId() throws Exception {
		itemRepo.findAll().forEach(item -> {
			Long id = item.getId();
			try {
				this.mockMvc.perform(get("/items/" + id).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
			} catch (Exception e) {
				log.error("exception in getting item details");
			}
		});

	}

	@Test
	@WithUserDetails()
	public void placeOrder() throws Exception {
		this.mockMvc.perform(post("/orders")
				.content(this.objectMapper.writeValueAsBytes(OrderForm.builder().id(1L).quantity(1).build()))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
	}

	@Test
	@WithUserDetails()
	public void placeOrderwithWrongItemId() throws Exception {
		this.mockMvc.perform(post("/orders")
				.content(this.objectMapper.writeValueAsBytes(OrderForm.builder().id(100L).quantity(1).build()))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(404));
	}

	@Test
	@WithUserDetails()
	public void placeOrderwithZeroItemQuantity() throws Exception {
		this.mockMvc.perform(post("/orders")
				.content(this.objectMapper.writeValueAsBytes(OrderForm.builder().id(1L).quantity(0).build()))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(400));
	}

	@Test
	@WithUserDetails()
	public void getOrderWithId() throws Exception {
		orderRepo.findAll().forEach(order -> {
			Long id = order.getOrderId();
			try {
				this.mockMvc.perform(get("/orders/" + id).accept(MediaType.APPLICATION_JSON))
						.andExpect(status().isOk());
			} catch (Exception e) {
				log.error("exception in getting order details");
			}
		});

	}

	@Test
	@WithUserDetails()
	public void getOrderWithWrongId() throws Exception {
		orderRepo.findAll().forEach(order -> {
			Long id = order.getOrderId() + 10;
			try {
				this.mockMvc.perform(get("/orders/" + id).accept(MediaType.APPLICATION_JSON))
						.andExpect(status().is(404));
			} catch (Exception e) {
				log.error("exception in getting order details");
			}
		});

	}

	@Test
	public void getAuthToken() throws Exception {
		try {
			this.mockMvc.perform(post("/auth/signIn")
					.content(
							this.objectMapper.writeValueAsBytes(User.builder().username("Nitika").password("Password")))
					.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
		} catch (Exception ex) {
			log.error("exception in getting user details");
		}
	}

}
