package com.example.shoppingmall.model.response.product;

import lombok.Data;
import com.example.shoppingmall.model.response.category.CategoryResponse;

import java.util.List;

@Data
public class ProductResponse {

    private Long id;

    private String name;

    private String sku;

    private String summary;

    private float unitPrice;

    private String imageUrl;

    private int unitInStock;

    private String content;

    private List<CategoryResponse> categoryResponseList;

}
