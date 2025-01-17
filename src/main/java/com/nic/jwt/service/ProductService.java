package com.nic.jwt.service;

import com.nic.jwt.entity.ResponseEntity;

public interface ProductService {

	public ResponseEntity getProductsList();
	public ResponseEntity getProductById(int productId);
}
