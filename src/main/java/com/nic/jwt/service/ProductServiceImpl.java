package com.nic.jwt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.nic.jwt.entity.ProductMasterEntity;
import com.nic.jwt.entity.ResponseEntity;
import com.nic.jwt.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	ProductRepository productRepo;

	@Override
	public ResponseEntity getProductsList() {
		
		System.out.println("getProductsList() of Service");
		
		List<ProductMasterEntity> products = productRepo.findAll();
		
		String msg = "Products fetched successfully";
		int statusCode = HttpStatus.OK.value();
		
		ResponseEntity response = new ResponseEntity();
		response.setData(products);
		response.setMessage(msg);
		response.setStatusCode(statusCode);
		
		return response;
	}

	@Override
	public ResponseEntity getProductById(int productId) {
		
		System.out.println("getProductById() of Service");
		
		ProductMasterEntity product = productRepo.findByProductId(productId);
		
		String msg;
		int statusCode;
		ResponseEntity response = new ResponseEntity();
		
		if(product == null)
		{
			msg = "Product not found";
			statusCode = HttpStatus.NOT_FOUND.value();
		}
		else 
		{
			msg="Product fetched successfully";
			statusCode=HttpStatus.OK.value();
		}
		response.setData(product);
		response.setMessage(msg);
		response.setStatusCode(statusCode);
		
		
		return response;
	}

	
	
}
