package com.gildedrose.server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gildedrose.server.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	public Optional<User> findByUserName(String userName);
}
