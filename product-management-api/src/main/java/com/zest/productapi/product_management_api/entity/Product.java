package com.zest.productapi.product_management_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @CreationTimestamp
    @Column(name = "created_on", nullable = false, updatable = false)
    private LocalDateTime createdOn;

    @Column(name = "modified_by")
    private String modifiedBy;

    @UpdateTimestamp
    @Column(name = "modified_on")
    private LocalDateTime modifiedOn;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items = new ArrayList<>();
}