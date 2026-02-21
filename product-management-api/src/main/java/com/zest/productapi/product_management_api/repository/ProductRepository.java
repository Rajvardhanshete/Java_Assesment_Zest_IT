package com.zest.productapi.product_management_api.repository;

import com.zest.productapi.product_management_api.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findByProductNameContaining(String name, Pageable pageable);
}
