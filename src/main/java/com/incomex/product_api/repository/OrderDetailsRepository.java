package com.incomex.product_api.repository;

import com.incomex.product_api.model.entity.OrderDetails;
import com.incomex.product_api.model.entity.OrderDetailsId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, OrderDetailsId> {
}