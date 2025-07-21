package com.incomex.product_api.service;

import com.incomex.product_api.dto.ShippersDTO;

import java.util.List;

public interface ShippersService {
    ShippersDTO create(ShippersDTO dto);
    ShippersDTO getById(Long id);
    List<ShippersDTO> getAll();
    ShippersDTO update(Long id, ShippersDTO dto);
    void delete(Long id);
}