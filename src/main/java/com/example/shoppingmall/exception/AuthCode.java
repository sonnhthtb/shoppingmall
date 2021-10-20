package com.example.shoppingmall.exception;

import org.springframework.http.HttpStatus;

public class AuthCode {

    public static final String LOGIN_FAIL = "LOGIN-ERROR";
    public static final String ACCESS_DENIED = "ACCESS-DENIED";
    public static final String UNAUTHORIZED = "UNAUTHORIZED";

    public static final ErrorResponse USERNAME_EXISTED =
            new ErrorResponse("EXISTED-USERNAME", "Username is existed", HttpStatus.BAD_REQUEST);
}
