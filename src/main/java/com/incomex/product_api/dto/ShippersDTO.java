package com.incomex.product_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class ShippersDTO {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    public Long shipperId;
    public String companyName;
    public String phone;
}