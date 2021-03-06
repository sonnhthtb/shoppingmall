package com.example.shoppingmall.model.request.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class RegisterRequest {

    @NotBlank(message = "Username can not blank")
    private String username;

    @Email(message = "Email is invalid")
    @NotBlank(message = "Email can not blank")
    private String email;

    @NotBlank(message = "Password can not blank")
    private String password;

    @NotBlank(message = "Full name can not blank")
    private String fullName;
}
