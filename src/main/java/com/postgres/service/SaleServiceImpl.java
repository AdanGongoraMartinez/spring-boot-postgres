package com.postgres.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.postgres.model.Sale;
import com.postgres.repo.SaleRepo;

import jakarta.transaction.Transactional;

@Service
public class SaleServiceImpl implements SaleService {

    @Autowired
    @Lazy
    private SaleRepo saleRepo;

    @Override
    @Transactional
    public List<Sale> findAll() {
        return (List<Sale>) saleRepo.findAll();
    }

    @Override
    @Transactional
    public Sale save(Sale sale) {
        return saleRepo.save(sale);
    }

    @Override
    @Transactional
    public Sale findById(Long id) {
        return saleRepo.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void delete(Sale sale) {
        saleRepo.delete(sale);
    }

}
