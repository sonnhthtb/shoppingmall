package com.example.shoppingmall.service;

import com.example.shoppingmall.model.request.user.LoginRequest;
import com.example.shoppingmall.model.request.user.RegisterRequest;
import com.example.shoppingmall.model.response.user.JwtResponse;

public interface AuthService {


    JwtResponse login(LoginRequest request);

    Boolean register (RegisterRequest request);
}
