package com.example.shoppingmall.service;

import org.springframework.security.core.Authentication;

public interface JwtService {

    String generateTokenLogin(Authentication authentication);

    String generateRefreshToken(Authentication authentication);

    Boolean validateJwtToken(String authToken);

    public String getUserNameFromJwtToken(String token);
}
