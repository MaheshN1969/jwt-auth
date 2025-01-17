package com.nic.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nic.jwt.entity.ProductMasterEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductMasterEntity, Integer>{

	public ProductMasterEntity findByProductId(int productId);
}
