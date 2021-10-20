package com.example.shoppingmall.config.security;

import com.example.shoppingmall.exception.AuthCode;
import com.example.shoppingmall.exception.ErrorResponse;
import com.example.shoppingmall.model.BaseResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


//custom response when not authentication
@Log4j2
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException e)
            throws IOException {

        String errorMessage = "Unauthorized";
        ErrorResponse errorResponse = new ErrorResponse(AuthCode.UNAUTHORIZED, errorMessage, HttpStatus.UNAUTHORIZED);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), BaseResponse.ofFailed(errorResponse));
    }
}
