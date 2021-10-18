package com.example.shoppingmall.exception;

import lombok.Getter;
import lombok.Setter;

public final class BusinessException extends RuntimeException {
    @Getter
    @Setter
    private transient ErrorResponse errorResponse;

    public BusinessException(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

    public BusinessException(ErrorResponse errorResponse, String key) {
        this.errorResponse = errorResponse;
        this.errorResponse.setMessage(this.errorResponse.getMessage() + " with [" + key + "]");
    }

    public BusinessException(ErrorResponse errorResponse, Long key) {
        this.errorResponse = errorResponse;
        this.errorResponse.setMessage(this.errorResponse.getMessage() + " with [" + key + "]");
    }

}
