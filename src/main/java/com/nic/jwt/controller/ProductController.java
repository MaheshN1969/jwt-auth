package com.nic.jwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nic.jwt.entity.ResponseEntity;
import com.nic.jwt.service.ProductServiceImpl;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {
	
	@Autowired
	ProductServiceImpl productService;

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping(value="")
	public ResponseEntity getAllProducts()
	{
		System.out.println("getAllProducts() of Controller");
		
		return productService.getProductsList();
	}
	
	@PreAuthorize("hasAuthority('USER')")
	@GetMapping(value="/{id}")
	public ResponseEntity getProductByProductId(@PathVariable("id") int productId)
	{
		System.out.println("getProductByProductId() of Controller");
		return productService.getProductById(productId);
	}

}
