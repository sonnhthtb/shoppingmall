package com.example.shoppingmall.service;

import com.example.shoppingmall.model.request.category.CategoryRequest;
import com.example.shoppingmall.model.response.category.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse save(CategoryRequest request);
    List<CategoryResponse> findAll();
    CategoryResponse findById(Long id);
    CategoryResponse update(CategoryRequest request, Long id);
    Boolean deleteById(Long id);
}
