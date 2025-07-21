package com.incomex.product_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class CustomersDTO {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    public Long customerId;
    public String companyName;
    public String contactName;
    public String contactTitle;
    public String address;
    public String city;
    public String region;
    public String postalCode;
    public String country;
    public String phone;
    public String fax;
}