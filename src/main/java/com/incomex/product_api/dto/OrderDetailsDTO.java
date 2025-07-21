package com.incomex.product_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public class OrderDetailsDTO {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    public Long orderId;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    public Long productId;
    public BigDecimal unitPrice;
    public Integer quantity;
    public Double discount;
}