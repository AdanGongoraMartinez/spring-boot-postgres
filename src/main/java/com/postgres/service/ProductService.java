package com.postgres.service;

import java.util.List;

import com.postgres.model.Product;

public interface ProductService {

    public List<Product> findAll();

    public Product save(Product product);

    public Product findById(Long id);

    public void delete(Product product);
}
