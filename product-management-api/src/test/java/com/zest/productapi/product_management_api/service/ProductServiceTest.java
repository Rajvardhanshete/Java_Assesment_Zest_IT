package com.zest.productapi.product_management_api.service;

import com.zest.productapi.product_management_api.dto.ItemRequest;
import com.zest.productapi.product_management_api.dto.ProductRequest;
import com.zest.productapi.product_management_api.dto.ProductResponse;
import com.zest.productapi.product_management_api.entity.Product;
import com.zest.productapi.product_management_api.exception.ResourceNotFoundException;
import com.zest.productapi.product_management_api.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductRequest productRequest;
    private ProductResponse productResponse;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(1)
                .productName("Laptop")
                .createdBy("admin")
                .build();

        productRequest = ProductRequest.builder()
                .productName("Laptop")
                .items(List.of(
                        ItemRequest.builder().quantity(5).build()
                ))
                .build();

        productResponse = ProductResponse.builder()
                .id(1)
                .productName("Laptop")
                .createdBy("admin")
                .items(List.of())
                .build();
    }

    @Test
    void getProductById_Success() {
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        ProductResponse response = productService.getProductById(1);

        assertEquals("Laptop", response.getProductName());
        assertEquals(1, response.getId());
    }

    @Test
    void getProductById_NotFound() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(1));
    }

    @Test
    void createProduct_Success() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponse response = productService.createProduct(productRequest, "admin");

        assertNotNull(response);
        assertEquals("Laptop", response.getProductName());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void getAllProducts_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        when(productRepository.findAll(pageable))
                .thenReturn(new PageImpl<>(List.of(product)));

        Page<ProductResponse> response = productService.getAllProducts(pageable);

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        assertEquals("Laptop", response.getContent().get(0).getProductName());
    }

    @Test
    void updateProduct_Success() {
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponse response = productService.updateProduct(1, productRequest, "admin");

        assertNotNull(response);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void updateProduct_NotFound() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.updateProduct(1, productRequest, "admin"));
    }

    @Test
    void deleteProduct_Success() {
        when(productRepository.existsById(1)).thenReturn(true);

        productService.deleteProduct(1);

        verify(productRepository).deleteById(1);
    }

    @Test
    void deleteProduct_NotFound() {
        when(productRepository.existsById(1)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> productService.deleteProduct(1));
    }
}