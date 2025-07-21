package com.incomex.product_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OrdersDTO {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    public Long orderId;
    public Long customerId;
    public Long employeeId;
    public LocalDate orderDate;
    public LocalDate requiredDate;
    public LocalDate shippedDate;
    public Long shipViaId;
    public BigDecimal freight;
    public String shipName;
    public String shipAddress;
    public String shipCity;
    public String shipRegion;
    public String shipPostalCode;
    public String shipCountry;
}