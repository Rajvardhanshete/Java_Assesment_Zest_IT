package com.zest.productapi.product_management_api.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductRequestDTO {

    @NotBlank
    private String name;

    @Positive
    private double price;
}