package com.postgres.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.postgres.model.SalesDetails;
import com.postgres.repo.SalesDetailsRepo;

import jakarta.transaction.Transactional;

@Service
public class SalesDetailsServiceImpl implements SalesDetailsService {

    @Autowired
    @Lazy
    private SalesDetailsRepo salesDetailsRepo;

    @Override
    @Transactional
    public List<SalesDetails> findAll() {
        return (List<SalesDetails>) salesDetailsRepo.findAll();
    }

    @Override
    @Transactional
    public SalesDetails save(SalesDetails salesDetails) {
        return salesDetailsRepo.save(salesDetails);
    }

    @Override
    @Transactional
    public SalesDetails findById(Long id) {
        return salesDetailsRepo.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void delete(SalesDetails salesDetails) {
        salesDetailsRepo.delete(salesDetails);
    }
}
