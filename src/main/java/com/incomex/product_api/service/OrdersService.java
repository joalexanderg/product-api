package com.incomex.product_api.service;

import com.incomex.product_api.dto.OrdersDTO;

import java.util.List;

public interface OrdersService {
    OrdersDTO create(OrdersDTO dto);
    OrdersDTO getById(Long id);
    List<OrdersDTO> getAll();
    OrdersDTO update(Long id, OrdersDTO dto);
    void delete(Long id);
}