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

import com.postgres.model.Sale;
import com.postgres.service.SaleServiceImpl;

@RestController
@RequestMapping("/Sales")
public class SaleController {

    @Autowired
    @Lazy
    private SaleServiceImpl saleServiceImpl;

    @GetMapping
    public ResponseEntity<List<Sale>> getSales() {
        try {
            List<Sale> sales = saleServiceImpl.findAll();
            if (sales.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(sales);
        } catch (DataAccessException ex) {
            LoggerFactory.getLogger(SaleController.class)
                    .error("Unexpected error ocured while getting sales" + ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sale> getSaleById(@PathVariable Long id) {
        try {
            Sale sale = saleServiceImpl.findById(id);
            return ResponseEntity.ok(sale);
        } catch (NoSuchElementException ex) {
            LoggerFactory.getLogger(SaleController.class)
                    .error("Sale not found with id: " + id, ex);
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            LoggerFactory.getLogger(SaleController.class)
                    .error("Unexpected error ocured with sale id:" + id, ex);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Sale> createSale(@RequestBody Sale sale) {
        if (sale.getId() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
        try {
            Sale savedSale = saleServiceImpl.save(sale);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(savedSale);
        } catch (DataIntegrityViolationException ex) {
            LoggerFactory.getLogger(SaleController.class)
                    .error("Integrity violation while creating the product", ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        } catch (Exception ex) {
            LoggerFactory.getLogger(ProductController.class)
                    .error("Unexpected error while creating the sale", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build(); // 500 Internal Server Error
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sale> updateSale(@PathVariable Long id,
            @RequestBody Sale saleReq) {
        try {
            Sale sale = saleServiceImpl.findById(id);
            sale.setTotal(saleReq.getTotal());
            sale.setDate(saleReq.getDate());
            Sale updatedSale = saleServiceImpl.save(sale);
            return ResponseEntity.ok(updatedSale);
        } catch (NoSuchElementException ex) {
            LoggerFactory.getLogger(ProductController.class)
                    .error("Sale not found with id: " + id, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build(); // 404
        } catch (IllegalArgumentException ex) {
            LoggerFactory.getLogger(ProductController.class)
                    .error("Invalid input while updating product with id: " + id, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .build();
        } catch (Exception ex) {
            LoggerFactory.getLogger(ProductController.class)
                    .error("Unexpected error ocured with sale id: " + id, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build(); // 500 Internal Server Error
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Sale> deleteSale(@PathVariable Long id) {
        try {
            Sale sale = saleServiceImpl.findById(id);
            saleServiceImpl.delete(sale);
            return ResponseEntity.ok(sale);
        } catch (NoSuchElementException ex) {
            LoggerFactory.getLogger(ProductController.class)
                    .error("Sale not found with id: " + id, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build(); // 404
        } catch (Exception ex) {
            LoggerFactory.getLogger(ProductController.class)
                    .error("Unexpected error ocured with sale id: " + id, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build(); // 500 Internal Server Error
        }

    }

}
