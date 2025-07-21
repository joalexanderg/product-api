package com.incomex.product_api.service;

import com.incomex.product_api.dto.CustomersDTO;

import java.util.List;

public interface CustomersService {
    CustomersDTO create(CustomersDTO dto);
    CustomersDTO getById(Long id);
    List<CustomersDTO> getAll();
    CustomersDTO update(Long id, CustomersDTO dto);
    void delete(Long id);
}