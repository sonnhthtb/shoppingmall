package com.example.shoppingmall.controller;

import com.example.shoppingmall.exception.BindingCode;
import com.example.shoppingmall.exception.BusinessCode;
import com.example.shoppingmall.exception.BusinessException;
import com.example.shoppingmall.exception.ErrorResponse;
import com.example.shoppingmall.model.BaseResponse;
import com.example.shoppingmall.utils.StringUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.apache.commons.lang3.StringUtils;

@Log4j2
@ControllerAdvice
public final class HandleExceptionController {

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)  // Nếu validate fail thì trả về 400
    public ResponseEntity<BaseResponse<ErrorResponse>> handleBindException(BindException e) {
        String errorMessage = "Request invalid";
        if (e.getBindingResult().hasErrors()) {
            errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        }
        ErrorResponse errorResponse = new ErrorResponse(BindingCode.BINDING_ERROR, errorMessage, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(BaseResponse.ofFailed(errorResponse), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<BaseResponse<ErrorResponse>> handleBusinessException(BusinessException businessException) {
        HttpStatus httpStatus = businessException.getErrorResponse().getStatus();
        log.error("Exception Detail: {} and rootCause: {}",
                businessException.getErrorResponse().getMessage(),
                StringUtil.stackTraceToString(businessException)
        );
        return new ResponseEntity<>(BaseResponse.ofFailed(businessException.getErrorResponse()), httpStatus);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<ErrorResponse>> handleBusinessException(Exception exception) {
        BusinessException businessException = new BusinessException(BusinessCode.INTERNAL_SERVER);
        if (!StringUtils.isBlank(exception.getMessage())) {
            log.error("Exception Detail: {} and rootCause: {}",
                    exception.getMessage(),
                    StringUtil.stackTraceToString(exception));
        }
        HttpStatus httpStatus = businessException.getErrorResponse().getStatus();

        return new ResponseEntity<>(BaseResponse.ofFailed(businessException.getErrorResponse()), httpStatus);
    }


    @ExceptionHandler(Throwable.class)
    public ResponseEntity<BaseResponse<ErrorResponse>> handleBusinessException(Throwable exception) {
        BusinessException businessException = new BusinessException(BusinessCode.INTERNAL_SERVER);
        HttpStatus httpStatus = businessException.getErrorResponse().getStatus();
        log.error("Exception global with message: {} status: {} exception: {}", exception.getMessage(), httpStatus, StringUtil.stackTraceToString(exception));
        return new ResponseEntity<>(BaseResponse.ofFailed(businessException.getErrorResponse()), httpStatus);
    }
}
