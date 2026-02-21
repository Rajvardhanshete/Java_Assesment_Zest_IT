package com.zest.productapi.product_management_api.controller;

import com.zest.productapi.product_management_api.dto.ProductRequest;
import com.zest.productapi.product_management_api.dto.ProductResponse;
import com.zest.productapi.product_management_api.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Product Management")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @Operation(summary = "Create a new Product")
    @ApiResponse(responseCode = "201", description = "Product created successfully")
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductRequest request,
            Authentication authentication) {
        String username = authentication.getName();
        return new ResponseEntity<>(
                productService.createProduct(request, username),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    @Operation(summary = "Get all products with pagination")
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        PageRequest pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update product by ID")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Integer id,
            @Valid @RequestBody ProductRequest request,
            Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(productService.updateProduct(id, request, username));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product by ID")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}