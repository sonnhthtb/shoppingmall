package com.example.shoppingmall.model.request.product;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


@Data
public class ProductRequest {

    @NotBlank(message = "Name cannot blank")
    private String name;

    @NotBlank(message = "Sku cannot blank")
    private String sku;

    private String summary;

    @NotNull(message = "Unit price cannot null")
    private float unitPrice;

    @NotBlank(message = "Image cannot blank")
    private String imageUrl;

    @NotNull(message = "Unit in stock cannot blank")
    private int unitInStock;

    private String content;

    @NotNull(message = "Category Id cannot blank")
    private List<Long> categoryIds;
}
