package com.postgres.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.postgres.exceptions.ResourceNotFoundException;
import com.postgres.model.Product;
import com.postgres.repo.ProductRepo;

@RestController
@RequestMapping("/Products")
public class ProductController {

    @Autowired
    @Lazy
    private ProductRepo productRepo;

    @GetMapping
    public List<Product> getProducts() {
        return productRepo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productRepo.save(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        product.setName(productDetails.getName());
        product.setPrice(productDetails.getPrice());
        product.setStock(productDetails.getStock());
        Product updatedProduct = productRepo.save(product);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public Product deleteProduct(@PathVariable Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        productRepo.delete(product);
        return product;
    }
}
