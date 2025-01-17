package com.nic.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nic.jwt.entity.ActiveToken;

public interface ActiveTokenRepository extends JpaRepository<ActiveToken, Integer> {

	public ActiveToken findByToken(String token);
	
}
