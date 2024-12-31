package com.postgres.service;

import java.util.List;

import com.postgres.model.Sale;

public interface SaleService {

    public List<Sale> findAll();

    public Sale save(Sale sale);

    public Sale findById(Long id);

    public void delete(Sale sale);
}
