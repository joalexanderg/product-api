package com.incomex.product_api.service;

import com.incomex.product_api.dto.OrderDetailsDTO;

import java.util.List;

public interface OrderDetailsService {
    OrderDetailsDTO create(OrderDetailsDTO dto);
    OrderDetailsDTO getById(Long orderId, Long productId);
    List<OrderDetailsDTO> getAll();
    OrderDetailsDTO update(Long orderId, Long productId, OrderDetailsDTO dto);
    void delete(Long orderId, Long productId);
}