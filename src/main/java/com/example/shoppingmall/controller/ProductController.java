package com.example.shoppingmall.controller;


import com.example.shoppingmall.model.BaseResponse;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import com.example.shoppingmall.model.response.product.ProductResponse;
import com.example.shoppingmall.service.ProductService;

@RestController
@RequestMapping("/public/api/v1/product")
@AllArgsConstructor
public class ProductController {

    private ProductService productService;

    @GetMapping()
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)", defaultValue = "0"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page.", defaultValue = "15"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string",
                    paramType = "query", value = "Sorting criteria in the format: property(,asc|desc). "
                    + "Default sort order is ascending. Multiple sort criteria are supported.")})
    public BaseResponse<?> getAll(@ApiIgnore Pageable pageable) {
        return BaseResponse.ofSuccess(productService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public BaseResponse<ProductResponse> getProductById(@PathVariable Long id) {
        return BaseResponse.ofSuccess(productService.findById(id));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)", defaultValue = "0"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page.", defaultValue = "15"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string",
                    paramType = "query", value = "Sorting criteria in the format: property(,asc|desc). "
                    + "Default sort order is ascending. Multiple sort criteria are supported.")})
    @GetMapping("/category/{code}")
    public BaseResponse<?> getProductByCategoryCode(@PathVariable String code,@ApiIgnore Pageable pageable) {
        return BaseResponse.ofSuccess(productService.findByCategoryCode(code, pageable));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)", defaultValue = "0"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page.", defaultValue = "15"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string",
                    paramType = "query", value = "Sorting criteria in the format: property(,asc|desc). "
                    + "Default sort order is ascending. Multiple sort criteria are supported.")})
    @GetMapping("/search")
    public BaseResponse<?> searchProduct(@RequestParam String name, @ApiIgnore Pageable pageable) {
        return BaseResponse.ofSuccess(productService.searchByName(name, pageable));
    }
}
