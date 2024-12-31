package com.postgres.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.postgres.model.SalesDetails;

public interface SalesDetailsRepo extends JpaRepository<SalesDetails, Long> {
    List<SalesDetails> findAllBySaleId(Long id);
}
