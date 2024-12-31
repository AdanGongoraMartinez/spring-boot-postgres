package com.postgres.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import com.postgres.model.Product;
import com.postgres.repo.ProductRepo;

import jakarta.transaction.Transactional;

public class ProductServiceImpl implements ProductService {

    @Autowired
    @Lazy
    private ProductRepo productRepo;

    @Override
    @Transactional
    public List<Product> findAll() {
        return (List<Product>) productRepo.findAll();
    }

    @Override
    @Transactional
    public Product save(Product product) {
        return productRepo.save(product);
    }

    @Override
    @Transactional
    public Product findById(Long id) {
        return productRepo.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void delete(Product product) {
        productRepo.delete(product);
    }

}
