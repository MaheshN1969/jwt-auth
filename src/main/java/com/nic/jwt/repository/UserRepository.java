package com.nic.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nic.jwt.entity.UserMasterEntity;

public interface UserRepository extends JpaRepository<UserMasterEntity, Integer>{

		public UserMasterEntity findByEmailId(String emailId);
}
