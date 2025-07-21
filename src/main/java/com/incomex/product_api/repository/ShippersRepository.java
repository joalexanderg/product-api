package com.incomex.product_api.repository;

import com.incomex.product_api.model.entity.Shippers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippersRepository extends JpaRepository<Shippers, Long> {
}
