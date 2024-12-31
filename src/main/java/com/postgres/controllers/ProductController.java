package com.postgres.controllers;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.postgres.model.Product;
import com.postgres.service.ProductServiceImpl;

@RestController
@RequestMapping("/Products")
public class ProductController {

    @Autowired
    @Lazy
    private ProductServiceImpl productServiceImpl;

    @GetMapping
    public ResponseEntity<List<Product>> getProducts() {
        try {
            List<Product> products = productServiceImpl.findAll();
            if (products.isEmpty()) {
                return ResponseEntity.noContent().build(); // 204
            }
            return ResponseEntity.ok(products); // 200
        } catch (DataAccessException ex) {
            // Database error
            // Register error in logs
            LoggerFactory.getLogger(ProductController.class)
                    .error("Unexpected error ocured", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        try {
            Product product = productServiceImpl.findById(id);
            return ResponseEntity.ok(product); // 200 OK
        } catch (NoSuchElementException ex) {
            LoggerFactory.getLogger(ProductController.class)
                    .error("Product not found with id: " + id, ex);
            return ResponseEntity.notFound().build(); // 404 Not Found
        } catch (Exception ex) {
            // Unexpected error
            LoggerFactory.getLogger(ProductController.class)
                    .error("Unexpected error ocured with product id:" + id, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        // Check if the client provided an ID (which is not allowed for new products)
        if (product.getId() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null); // 400 Bad Request: The client must not send an ID
        }
        try {
            // Save the product
            Product savedProduct = productServiceImpl.save(product);

            // Return 201 Created with the saved product
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(savedProduct);
        } catch (DataIntegrityViolationException ex) {
            // Log errors related to database constraints
            LoggerFactory.getLogger(ProductController.class)
                    .error("Integrity violation while creating the product", ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null); // 400 Bad Request
        } catch (Exception ex) {
            // Log unexpected errors
            LoggerFactory.getLogger(ProductController.class)
                    .error("Unexpected error while creating the product", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id,
            @RequestBody Product productDetails) {
        try {
            Product product = productServiceImpl.findById(id);
            product.setName(productDetails.getName());
            product.setPrice(productDetails.getPrice());
            product.setStock(productDetails.getStock());
            Product updatedProduct = productServiceImpl.save(product);
            return ResponseEntity.ok(updatedProduct);
        } catch (NoSuchElementException ex) {
            LoggerFactory.getLogger(ProductController.class).error("Product not found with id: " + id, ex);
            return ResponseEntity.notFound().build(); // 404 Not Found
        } catch (IllegalArgumentException ex) {
            // Log and return 400 Bad Request for invalid input
            LoggerFactory.getLogger(ProductController.class)
                    .error("Invalid input while updating product with id: " + id, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception ex) {
            // Unexpected error
            LoggerFactory.getLogger(ProductController.class)
                    .error("Unexpected error ocured with product id:" + id, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable Long id) {
        try {
            Product product = productServiceImpl.findById(id);
            productServiceImpl.delete(product);
            return ResponseEntity.ok(product);
        } catch (NoSuchElementException ex) {
            LoggerFactory.getLogger(ProductController.class).error("Product not found with id: " + id, ex);
            return ResponseEntity.notFound().build(); // 404 Not Found
        } catch (Exception ex) {
            // Unexpected error
            LoggerFactory.getLogger(ProductController.class)
                    .error("Unexpected error ocured with product id:" + id, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }
}
