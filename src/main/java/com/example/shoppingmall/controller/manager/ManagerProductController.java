package com.example.shoppingmall.controller.manager;

import com.example.shoppingmall.model.BaseResponse;
import com.example.shoppingmall.model.request.product.ProductRequest;
import com.example.shoppingmall.model.response.product.ProductResponse;
import com.example.shoppingmall.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/manager/api/v1/product")
@AllArgsConstructor
public class ManagerProductController {

    private final ProductService productService;

    @PostMapping
    public BaseResponse<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        return BaseResponse.ofSuccess(HttpStatus.CREATED, productService.save(request));
    }

    @PutMapping("/{id}")
    public BaseResponse<ProductResponse> updateProduct(@Valid @RequestBody ProductRequest request, @PathVariable Long id) {
        return BaseResponse.ofSuccess(productService.update(request, id));
    }

    @DeleteMapping("/{id}")
    public BaseResponse<Boolean> deleteProduct(@PathVariable Long id) {
        return BaseResponse.ofSuccess(productService.deleteById(id));
    }
}
