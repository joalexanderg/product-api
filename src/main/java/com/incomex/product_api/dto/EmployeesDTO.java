package com.incomex.product_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public class EmployeesDTO {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    public Long employeeId;
    public String lastName;
    public String firstName;
    public String title;
    public String titleOfCourtesy;
    public LocalDate birthDate;
    public LocalDate hireDate;
    public String address;
    public String city;
    public String region;
    public String postalCode;
    public String country;
    public String homePhone;
    public String extension;
    public String photo;
    public String notes;
    public Long reportsToId;
}