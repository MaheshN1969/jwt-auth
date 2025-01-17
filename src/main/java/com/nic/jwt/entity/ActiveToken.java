package com.nic.jwt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="active_token",schema="master")
@AllArgsConstructor
@NoArgsConstructor
public class ActiveToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="active_token_id")
	private int activeTokenId;
	
	@Column(name="token")
	private String token;
	
	@Column(name="email_id")
	private String emailId;
	
	@Column(name="active_flag")
	private boolean activeFlag;
	
}
