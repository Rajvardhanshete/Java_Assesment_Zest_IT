package com.zest.productapi.product_management_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Integer id;
    private String productName;
    private String createdBy;
    private LocalDateTime createdOn;
    private String modifiedBy;
    private LocalDateTime modifiedOn;
    private List<ItemResponse> items;
}