package com.example.shoppingmall.controller;

import com.example.shoppingmall.model.BaseResponse;
import com.example.shoppingmall.model.request.user.LoginRequest;
import com.example.shoppingmall.model.request.user.RefreshTokenRequest;
import com.example.shoppingmall.model.response.user.JwtResponse;
import com.example.shoppingmall.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.example.shoppingmall.entity.User;
import com.example.shoppingmall.model.request.user.RegisterRequest;
import com.example.shoppingmall.service.UserService;

import javax.validation.Valid;


@RestController
@RequestMapping("public/api/v1/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/register")
    public BaseResponse<User> signup(@Valid @RequestBody RegisterRequest request) {
        return BaseResponse.ofSuccess(HttpStatus.CREATED, userService.save(request));
    }

    @PostMapping("/login")
    public BaseResponse<JwtResponse> login(@Valid @RequestBody LoginRequest request) {
        return BaseResponse.ofSuccess(authService.login(request));
    }

    @PostMapping("/refresh_token")
    public BaseResponse<JwtResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return BaseResponse.ofSuccess(authService.refreshToken(request));
    }

}
