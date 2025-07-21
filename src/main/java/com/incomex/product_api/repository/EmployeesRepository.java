package com.incomex.product_api.repository;

import com.incomex.product_api.model.entity.Employees;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeesRepository extends JpaRepository<Employees, Long> {
}