package com.example.shoppingmall.controller;

import com.example.shoppingmall.model.BaseResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.example.shoppingmall.model.response.category.CategoryResponse;
import com.example.shoppingmall.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/public/api/v1/category")
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping()
    public BaseResponse<List<CategoryResponse>> getAllCategories() {
        return BaseResponse.ofSuccess(categoryService.findAll());
    }

    @GetMapping("/{id}")
    public BaseResponse<CategoryResponse> getCategoryById(@PathVariable Long id) {
        return BaseResponse.ofSuccess(categoryService.findById(id));
    }

}
