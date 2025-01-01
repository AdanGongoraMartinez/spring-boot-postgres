package com.postgres.service;

import java.util.List;

import com.postgres.model.SalesDetails;

public interface SalesDetailsService {

    public List<SalesDetails> findAll();

    public SalesDetails save(SalesDetails salesDetails);

    public SalesDetails findById(Long id);

    public void delete(SalesDetails salesDetails);

    List<SalesDetails> findAllBySaleId(Long saleId);
}
