package com.nic.jwt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="users", schema="master")
public class UserMasterEntity {

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="user_id")
	private int userId;
	
	
	@Column(name="email_id")
	private String emailId;
	
	@Column(name="password")
	private String password;
	
	
	@Column(name="is_expired")
	private boolean isExpired;
	
	@Column(name="role")
	private String role;
}
