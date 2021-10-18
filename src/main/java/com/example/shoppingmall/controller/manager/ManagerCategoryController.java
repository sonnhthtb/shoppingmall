package com.example.shoppingmall.controller.manager;

import com.example.shoppingmall.model.BaseResponse;
import com.example.shoppingmall.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.example.shoppingmall.model.request.category.CategoryRequest;
import com.example.shoppingmall.model.response.category.CategoryResponse;

import javax.validation.Valid;

@RestController
@RequestMapping("/manager/api/v1/category")
@AllArgsConstructor
public class ManagerCategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public BaseResponse<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
        return BaseResponse.ofSuccess(HttpStatus.CREATED, categoryService.save(request));
    }

    @PutMapping("/{id}")
    public BaseResponse<CategoryResponse> updateCategory(@Valid @RequestBody CategoryRequest request,
                                                         @PathVariable Long id) {
        return BaseResponse.ofSuccess(categoryService.update(request,id));
    }

    @DeleteMapping("/{id}")
    public BaseResponse<Boolean> deleteById(@PathVariable Long id) {
        return BaseResponse.ofSuccess(categoryService.deleteById(id));
    }
}
