package com.zest.productapi.product_management_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zest.productapi.product_management_api.dto.ItemRequest;
import com.zest.productapi.product_management_api.dto.ProductRequest;
import com.zest.productapi.product_management_api.dto.ProductResponse;
import com.zest.productapi.product_management_api.security.JwtService;
import com.zest.productapi.product_management_api.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @MockBean
    private JwtService jwtService;

    private ProductRequest productRequest;
    private ProductResponse productResponse;

    @BeforeEach
    void setUp() {
        productRequest = ProductRequest.builder()
                .productName("Test Product")
                .items(List.of(
                        ItemRequest.builder().quantity(10).build()
                ))
                .build();

        productResponse = ProductResponse.builder()
                .id(1)
                .productName("Test Product")
                .createdBy("admin")
                .items(List.of())
                .build();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldCreateProduct() throws Exception {
        when(productService.createProduct(any(ProductRequest.class), any(String.class)))
                .thenReturn(productResponse);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.productName").value("Test Product"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldGetAllProducts() throws Exception {
        when(productService.getAllProducts(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(productResponse)));

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].productName").value("Test Product"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldGetProductById() throws Exception {
        when(productService.getProductById(1)).thenReturn(productResponse);

        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldUpdateProduct() throws Exception {
        when(productService.updateProduct(eq(1), any(ProductRequest.class), any(String.class)))
                .thenReturn(productResponse);

        mockMvc.perform(put("/api/v1/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldDeleteProduct() throws Exception {
        mockMvc.perform(delete("/api/v1/products/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnUnauthorizedWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isUnauthorized());
    }
}