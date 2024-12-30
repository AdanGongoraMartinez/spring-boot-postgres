package com.postgres.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.postgres.model.Product;

public interface ProductRepo extends JpaRepository<Product, Long> {

}
