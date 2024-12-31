package com.postgres.controllers;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
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
            LoggerFactory.getLogger(ProductController.class)
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
            LoggerFactory.getLogger(ProductController.class)
                    .error("Sale detail not found with id: " + id, ex);
            return ResponseEntity.notFound().build(); // 404 Not Found
        } catch (Exception ex) {
            LoggerFactory.getLogger(ProductController.class)
                    .error("Unexpected error ocured with product id:" + id, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build(); // 500 Internal Server Error
        }

    }

    @GetMapping("/sales/{id}")
    public ResponseEntity<SalesDetails> getSalesDetailsBySaleId(@PathVariable Long id) {
        // TODO
        return null;
    }

    @PostMapping
    public ResponseEntity<SalesDetails> createSalesDetails(@RequestBody SalesDetails salesDetails) {
        // TODO
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalesDetails> updateSalesDetails(@RequestBody SalesDetails salesDetails,
            @PathVariable Long id) {
        // TODO
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SalesDetails> deleteSalesDetails(@PathVariable Long id) {
        // TODO
        return null;
    }
}
