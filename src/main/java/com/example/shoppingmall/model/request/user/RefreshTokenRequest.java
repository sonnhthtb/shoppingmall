package com.example.shoppingmall.model.request.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RefreshTokenRequest {

    @NotBlank(message = "Username can not blank")
    String username;

    @NotBlank(message = "Refresh token can not blank")
    String refreshToken;
}
