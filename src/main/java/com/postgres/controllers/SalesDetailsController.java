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

import com.postgres.DTO.SalesDetailsDTO;
import com.postgres.model.Product;
import com.postgres.model.Sale;
import com.postgres.model.SalesDetails;
import com.postgres.service.SalesDetailsServiceImpl;

@RestController
@RequestMapping("/SalesDetails")
public class SalesDetailsController {

    @Autowired
    @Lazy
    private SalesDetailsServiceImpl salesDetailsServiceImpl;

    @GetMapping
    public ResponseEntity<List<SalesDetails>> getSalesDetails() {
        try {
            List<SalesDetails> salesDetails = salesDetailsServiceImpl.findAll();
            if (salesDetails.isEmpty()) {
                return ResponseEntity.noContent().build(); // 204
            }
            return ResponseEntity.ok(salesDetails); // 200
        } catch (DataAccessException ex) {
            LoggerFactory.getLogger(SalesDetailsController.class)
                    .error("Unexpected error ocured", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesDetails> getSalesDetailsById(@PathVariable Long id) {
        try {
            SalesDetails salesDetails = salesDetailsServiceImpl.findById(id);
            return ResponseEntity.ok(salesDetails);
        } catch (NoSuchElementException ex) {
            LoggerFactory.getLogger(SalesDetailsController.class)
                    .error("Sale detail not found with id: " + id, ex);
            return ResponseEntity.notFound().build(); // 404 Not Found
        } catch (Exception ex) {

            LoggerFactory.getLogger(SalesDetailsController.class)
                    .error("Unexpected error ocured with sales details id:" + id, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build(); // 500 Internal Server Error
        }

    }

    @GetMapping("/Sales/{id}")
    public ResponseEntity<List<SalesDetails>> getSalesDetailsBySaleId(@PathVariable Long id) {
        try {
            List<SalesDetails> salesDetails = salesDetailsServiceImpl.findAllBySaleId(id);
            if (salesDetails.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(salesDetails);
        } catch (NoSuchElementException ex) {
            LoggerFactory.getLogger(SalesDetailsController.class)
                    .error("Sale detail not found with id: " + id, ex);
            return ResponseEntity.notFound().build(); // 404 Not Found
        } catch (Exception ex) {
            LoggerFactory.getLogger(SalesDetailsController.class)
                    .error("Unexpected error ocured with sales details id:" + id, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build(); // 500 Internal Server Error
        }

    }

    @PostMapping
    public ResponseEntity<SalesDetails> createSalesDetails(@RequestBody SalesDetailsDTO salesDetailsDTO) {
        try {
            // Create and serialize
            Sale sale = new Sale();
            sale.setId(salesDetailsDTO.getSale());
            Product product = new Product();
            product.setId(salesDetailsDTO.getProduct());
            // Translate data
            SalesDetails salesDetails = new SalesDetails();
            salesDetails.setSale(sale);
            salesDetails.setProduct(product);
            salesDetails.setQuantity(salesDetailsDTO.getQuantity());
            // Save SalesDetails
            SalesDetails savedSalesDetails = salesDetailsServiceImpl.save(salesDetails);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(savedSalesDetails);
        } catch (DataIntegrityViolationException ex) {
            LoggerFactory.getLogger(SalesDetailsController.class)
                    .error("Integrity violation while creating the sales details", ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null); // 400 Bad Request
        } catch (Exception ex) {
            LoggerFactory.getLogger(SalesDetailsController.class)
                    .error("Unexpected error while creating the sales details", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalesDetails> updateSalesDetails(
            @RequestBody SalesDetails salesDetailsBody,
            @PathVariable Long id) {
        try {
            SalesDetails salesDetails = salesDetailsServiceImpl.findById(id);
            salesDetails.setSale(salesDetailsBody.getSale());
            salesDetails.setProduct(salesDetailsBody.getProduct());
            SalesDetails updatedSalesDetails = salesDetailsServiceImpl.save(salesDetails);
            return ResponseEntity.ok(updatedSalesDetails);
        } catch (IllegalArgumentException ex) {
            LoggerFactory.getLogger(SalesDetailsController.class)
                    .error("Unexpected error while creating the sales details", ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (NoSuchElementException ex) {
            LoggerFactory.getLogger(SalesDetailsController.class)
                    .error("Sale detail not found with id: " + id, ex);
            return ResponseEntity.notFound().build(); // 404 Not Found
        } catch (Exception ex) {
            LoggerFactory.getLogger(SalesDetailsController.class)
                    .error("Unexpected error while updating the sales details", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SalesDetails> deleteSalesDetails(@PathVariable Long id) {
        try {
            SalesDetails salesDetails = salesDetailsServiceImpl.findById(id);
            salesDetailsServiceImpl.delete(salesDetails);
            return ResponseEntity.ok(salesDetails);
        } catch (NoSuchElementException ex) {
            LoggerFactory.getLogger(SalesDetailsController.class)
                    .error("Sale detail not found with id: " + id, ex);
            return ResponseEntity.notFound().build(); // 404 Not Found
        } catch (Exception ex) {
            LoggerFactory.getLogger(SalesDetailsController.class)
                    .error("Unexpected error while deleting the sales details", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }
}
