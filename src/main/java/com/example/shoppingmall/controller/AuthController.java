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

    @PostMapping("/register")
    public BaseResponse<Boolean> signup(@Valid @RequestBody RegisterRequest request) {
        return BaseResponse.ofSuccess(HttpStatus.CREATED, authService.register(request));
    }

    @PostMapping("/login")
    public BaseResponse<JwtResponse> login(@Valid @RequestBody LoginRequest request) {
        return BaseResponse.ofSuccess(authService.login(request));
    }

    @PostMapping("/refresh_token")
    public BaseResponse<JwtResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return BaseResponse.ofSuccess(authService.refreshToken(request));
    }

    @GetMapping("/accountVerification/{token}")
    public BaseResponse<Boolean> verifyAccount(@PathVariable String token) {
        return BaseResponse.ofSuccess(authService.verifyAccount(token));
    }

    @PostMapping("/checkUserName")
    public BaseResponse<Boolean> checkUsernameExisted(@Valid @RequestBody RegisterRequest request) {
        return BaseResponse.ofSuccess(authService.checkUsernameExisted(request));
    }

    @PostMapping("/checkEmail")
    public BaseResponse<Boolean> checkEmailExisted(@Valid @RequestBody RegisterRequest request) {
        return BaseResponse.ofSuccess(authService.checkEmailExisted(request));
    }

}
