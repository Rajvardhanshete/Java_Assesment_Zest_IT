package com.zest.productapi.product_management_api.service;

import com.zest.productapi.product_management_api.dto.ItemResponse;
import com.zest.productapi.product_management_api.dto.ProductRequest;
import com.zest.productapi.product_management_api.dto.ProductResponse;
import com.zest.productapi.product_management_api.entity.Item;
import com.zest.productapi.product_management_api.entity.Product;
import com.zest.productapi.product_management_api.exception.ResourceNotFoundException;
import com.zest.productapi.product_management_api.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public ProductResponse createProduct(ProductRequest request, String username) {
        Product product = Product.builder()
                .productName(request.getProductName())
                .createdBy(username)
                .build();

        List<Item> items = new ArrayList<>();
        for (var itemReq : request.getItems()) {
            Item item = Item.builder()
                    .product(product)
                    .quantity(itemReq.getQuantity())
                    .build();
            items.add(item);
        }

        product.setItems(items);
        Product saved = productRepository.save(product);
        return mapToResponse(saved);
    }

    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(this::mapToResponse);
    }

    public ProductResponse getProductById(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return mapToResponse(product);
    }

    @Transactional
    public ProductResponse updateProduct(Integer id, ProductRequest request, String username) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.setProductName(request.getProductName());
        product.setModifiedBy(username);

        // Clear existing items
        product.getItems().clear();

        // Add new items
        List<Item> newItems = new ArrayList<>();
        for (var itemReq : request.getItems()) {
            Item item = Item.builder()
                    .product(product)
                    .quantity(itemReq.getQuantity())
                    .build();
            newItems.add(item);
        }
        product.getItems().addAll(newItems);

        Product updated = productRepository.save(product);
        return mapToResponse(updated);
    }

    public void deleteProduct(Integer id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    private ProductResponse mapToResponse(Product product) {
        List<ItemResponse> itemResponses = new ArrayList<>();

        if (product.getItems() != null) {
            for (Item item : product.getItems()) {
                ItemResponse itemResponse = ItemResponse.builder()
                        .id(item.getId())
                        .quantity(item.getQuantity())
                        .build();
                itemResponses.add(itemResponse);
            }
        }

        return ProductResponse.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .createdBy(product.getCreatedBy())
                .createdOn(product.getCreatedOn())
                .modifiedBy(product.getModifiedBy())
                .modifiedOn(product.getModifiedOn())
                .items(itemResponses)
                .build();
    }
}