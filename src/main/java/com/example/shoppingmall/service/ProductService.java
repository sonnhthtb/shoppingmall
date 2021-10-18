package com.example.shoppingmall.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.shoppingmall.model.request.product.ProductRequest;
import com.example.shoppingmall.model.response.product.ProductResponse;

public interface ProductService {
    ProductResponse save(ProductRequest request);
    Page<ProductResponse> findAll(Pageable pageable);
    ProductResponse findById(Long id);
    Page<ProductResponse> findByCategoryCode(String code, Pageable pageable);
    Page<ProductResponse> searchByName(String name, Pageable pageable);
    ProductResponse update(ProductRequest request, Long id);
    Boolean deleteById(Long id);
}
