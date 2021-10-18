package com.example.shoppingmall.model.request.category;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CategoryRequest {

    @NotBlank(message = "Name cannot blank")
    private String name;

    @NotBlank(message = "Code cannot blank")
    private String code;

}
