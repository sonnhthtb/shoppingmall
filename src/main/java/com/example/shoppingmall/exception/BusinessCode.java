package com.example.shoppingmall.exception;

import org.springframework.http.HttpStatus;

public class BusinessCode {

    private BusinessCode() {
    }

    public static final ErrorResponse SUCCESS =
            new ErrorResponse("SUCCESS-01", "SUCCESS", HttpStatus.OK);

    public static final ErrorResponse INTERNAL_SERVER =
            new ErrorResponse("INTERNAL-SERVER", "Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);

    public static final ErrorResponse NOT_FOUND_USER =
            new ErrorResponse("NOT-FOUND-USER", "Not found user", HttpStatus.BAD_REQUEST);
    public static final ErrorResponse NOT_FOUND_ROLE =
            new ErrorResponse("NOT-FOUND-ROLE", "Not found role", HttpStatus.BAD_REQUEST);
    public static final ErrorResponse NOT_FOUND_CATEGORY =
            new ErrorResponse("NOT_FOUND_CATEGORY", "Not found category", HttpStatus.BAD_REQUEST);
    public static final ErrorResponse NOT_FOUND_ITEM =
            new ErrorResponse("NOT_FOUND_ITEM", "Not found product", HttpStatus.BAD_REQUEST);

}
