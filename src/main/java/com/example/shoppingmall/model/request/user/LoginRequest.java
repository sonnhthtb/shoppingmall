package com.example.shoppingmall.model.request.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@Data
public class LoginRequest {

    @NotBlank(message = "Username can not blank")
    private String username;

    @NotBlank(message = "Password can not blank")
    private String password;
}
