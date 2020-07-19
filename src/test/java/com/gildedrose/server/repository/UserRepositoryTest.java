package com.gildedrose.server.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gildedrose.server.domain.User;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UserRepositoryTest {
	@Autowired
	UserRepository userRepo;

	@Test
	public void findByUserNameTest() {
		User saved = this.userRepo.save(User.builder().userName("test").password("password").build());
		User u = this.userRepo.getOne(saved.getId());
		assertThat(u.getUsername()).isEqualTo("test");
		assertThat(u.getId()).isNotNull();
		assertThat(u.getPassword()).isEqualTo("password");
	}
}
