package com.postgres.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.postgres.model.Sale;

public interface SaleRepo extends JpaRepository<Sale, Long> {

}
