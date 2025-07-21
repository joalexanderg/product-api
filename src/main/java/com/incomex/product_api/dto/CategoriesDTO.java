package com.incomex.product_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CategoriesDTO {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long categoryId;
    private String categoryName;
    private String description;
    private String picture;
}