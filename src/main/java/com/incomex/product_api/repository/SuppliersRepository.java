package com.incomex.product_api.repository;

import com.incomex.product_api.model.entity.Suppliers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuppliersRepository extends JpaRepository<Suppliers, Long> {
}