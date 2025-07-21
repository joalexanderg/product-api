package com.incomex.product_api.service;

import com.incomex.product_api.dto.SuppliersDTO;
import java.util.List;

public interface SuppliersService {
    SuppliersDTO create(SuppliersDTO dto);
    SuppliersDTO getById(Long id);
    List<SuppliersDTO> getAll();
    SuppliersDTO update(Long id, SuppliersDTO dto);
    void delete(Long id);
}