package com.postgres.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.postgres.exceptions.ResourceNotFoundException;
import com.postgres.model.SalesDetails;
import com.postgres.repo.SalesDetailsRepo;

@RestController
@RequestMapping("/SalesDetails")
public class SalesDetailsController {

    @Autowired
    @Lazy
    private SalesDetailsRepo salesDetailsRepo;

    @GetMapping
    public List<SalesDetails> getSalesDetails() {
        return salesDetailsRepo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesDetails> getSalesDetailsById(@PathVariable Long id) {
        SalesDetails salesDetails = salesDetailsRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sales Details not found with id: " + id));
        return ResponseEntity.ok(salesDetails);
    }

    @GetMapping("/sales/{id}")
    public List<SalesDetails> getSalesDetailsBySalesId(@PathVariable Long id) {
        return salesDetailsRepo.findAllBySalesId(id);
    }

    @PostMapping
    public SalesDetails createSalesDetails(@RequestBody SalesDetails salesDetails) {
        return salesDetailsRepo.save(salesDetails);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalesDetails> updateSalesDetails(@PathVariable Long id,
            @RequestBody SalesDetails salesDetailsNew) {

        SalesDetails salesDetails = salesDetailsRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sales Details not found with id: " + id));
        salesDetails.setSale(salesDetailsNew.getSale());
    }
}
