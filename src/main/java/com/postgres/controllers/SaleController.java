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
import com.postgres.model.Sale;
import com.postgres.repo.SaleRepo;

@RestController
@RequestMapping("/Sales")
public class SaleController {

    @Autowired
    @Lazy
    private SaleRepo saleRepo;

    @GetMapping
    public List<Sale> getSales() {
        return saleRepo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sale> getSaleById(@PathVariable Long id) {
        Sale sale = saleRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found with id: " + id));
        return ResponseEntity.ok(sale);
    }

    @PostMapping
    public Sale createSale(@RequestBody Sale sale) {
        return saleRepo.save(sale);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sale> updateSale(@PathVariable Long id, @RequestBody Sale saleDetails) {
        Sale sale = saleRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found with id: " + id));
        sale.setDate(saleDetails.getDate());
        sale.setTotal(saleDetails.getTotal());
        Sale updatedSale = saleRepo.save(sale);
        return ResponseEntity.ok(updatedSale);
    }

    @DeleteMapping("/{id}")
    public Sale deleteSale(@PathVariable Long id) {
        Sale sale = saleRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found with id: " + id));
        saleRepo.delete(sale);
        return sale;

    }

}
