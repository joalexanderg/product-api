package com.incomex.product_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class ProductDTO {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long productId;
    private String productName;
    private Long supplierID;
    private Long categoryID;
    private String quantityPerUnit;
    private BigDecimal unitPrice;
    private Integer unitsInStock;
    private Integer unitsOnOrder;
    private Integer reorderLevel;
    private boolean discontinued;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String categoryPicture;
}
